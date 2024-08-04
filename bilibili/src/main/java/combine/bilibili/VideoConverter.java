package combine.bilibili;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 转换哔哩哔哩 PC 端缓存的视频，将 .m4s（有视频和音频两个文件）合成为一个 .mp4 文件。
 *
 * @author Butcher
 * @see <a href="https://www.cnblogs.com/gengduc/p/16962723.html" target="_blank">FFmpeg 合并视频和音频文件</a>
 * @since 2023-06-27
 */
@Slf4j
public class VideoConverter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String DEFAULT_OUTPUT_FOLDER = "bilibiliOutput";

    /**
     * 每个视频的缓存目录。
     */
    @Getter
    private final List<String> savePaths;

    /**
     * 视频输出目录，默认保存到桌面路径下的 {@value #DEFAULT_OUTPUT_FOLDER} 文件夹。
     */
    @Getter
    @Setter
    private String outputPath;

    /**
     * 是否保留被恢复的音视频文件，默认 false。
     */
    @Getter
    @Setter
    private boolean keepRestoredFiles = false;

    /**
     * 前面的干扰字符。
     */
    @Getter
    @Setter
    private String interferenceChars = "000000000";

    private final int interferenceLength = interferenceChars.getBytes().length;

    /**
     * 默认音视频文件的名称。
     */
    private static final String videoName = "video.mp4", audioName = "audio.mp3";

    private File output;

    /**
     * 默认构造函数。
     *
     * @param savePaths 每个视频的缓存目录。
     * @throws IOException 如果发生 I/O 错误。
     */
    public VideoConverter(List<String> savePaths) throws IOException {
        this.savePaths = savePaths;
        init();
    }

    private void init() throws IOException {
        if (StringUtils.isBlank(outputPath)) {
            outputPath = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + File.separator + DEFAULT_OUTPUT_FOLDER;
            log.debug("未指定输出路径，将默认使用 {}", outputPath);
        }
        output = createFileIfNotExist(outputPath, true);
    }

    /**
     * 启动转换。
     *
     * @throws IOException 如果发生 I/O 错误。
     */
    public void start() throws IOException {
        if (savePaths == null || savePaths.isEmpty()) {
            log.error("未指定缓存目录…");
            return;
        }

        for (String cachePath : savePaths) {
            File file = new File(cachePath);
            if (!file.exists() || !file.isDirectory()) {
                continue;
            }
            convert(file);
        }
    }

    /**
     * 转换方法，调用 ffmpeg 进行音视频合并。
     *
     * @param fileDir 每个视频的文件夹。
     * @throws IOException 如果发生 I/O 错误。
     */
    private void convert(File fileDir) throws IOException {
        File[] files = fileDir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        VideoInfo videoInfo = new VideoInfo();

        List<File> m4sList = new ArrayList<>();

        for (File file : files) {
            String name = file.getName();
            if (".videoInfo".equals(name)) {
                videoInfo = OBJECT_MAPPER.readValue(file, VideoInfo.class);
                log.debug(videoInfo.toString());
            }
            if (name.endsWith(".m4s")) {
                m4sList.add(file);
            }
        }

        m4sList.sort(Comparator.comparingLong(File::length).reversed());
        File f1 = m4sList.get(0);
        File f2 = m4sList.get(1);

        File m4sVideo, m4sAudio;

        if (f1.length() > f2.length()) {
            m4sVideo = f1;
            m4sAudio = f2;
        } else {
            m4sVideo = f2;
            m4sAudio = f1;
        }

        File video = restoreMedia(m4sVideo, true);
        File audio = restoreMedia(m4sAudio, false);

        // ffmpeg -i video.mp4 -i audio.mp3 -c:v copy -c:a aac -strict experimental output.mp4
        ProcessBuilder pb = new ProcessBuilder("ffmpeg"
                , "-i"
                , video.getAbsolutePath()
                , "-i"
                , audio.getAbsolutePath()
                , "-c:v"
                , "copy"
                , "-c:a"
                , "aac"
                , "-strict"
                , "experimental"
                , outputPath + File.separator + videoInfo.getP() + ". " + videoInfo.getTitle() + ".mp4"
        );

        log.debug("Final command: " + String.join(" ", pb.command()));

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("正在合并：{}", videoInfo.getTitle());

        pb.redirectErrorStream(true);
        Process process = pb.start();

        String outLine;
        BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while ((outLine = buf.readLine()) != null) {
            log.debug(outLine);
        }

        stopWatch.stop();
        log.info("合并成功，耗时 {} 秒", stopWatch.getTime() / 1000.0);

        if (!keepRestoredFiles) {
            if (!video.delete()) {
                log.warn("还原视频文件删除失败，路径：{}", video.getAbsolutePath());
            }
            if (!audio.delete()) {
                log.warn("还原音频文件删除失败，路径：{}", audio.getAbsolutePath());
            }
        }
    }

    /**
     * 还原被扰乱的音视频文件，视频为 mp4 音频为 mp3 格式写入到 {@link #outputPath} 中。
     *
     * @param m4sFile 源 .m4s 文件。
     * @param isVideo true 当做视频否则就是音频。
     * @return mp3 or mp4.
     * @throws IOException 如果发生 I/O 错误。
     */
    private File restoreMedia(File m4sFile, boolean isVideo) throws IOException {
        File mpFile = createFileIfNotExist(output.getAbsolutePath() + File.separator + (isVideo ? videoName : audioName), false);

        try (RandomAccessFile rw = new RandomAccessFile(m4sFile, "rw");
             RandomAccessFile tempRw = new RandomAccessFile(mpFile, "rw")) {
            rw.seek(interferenceLength);

            byte[] bytes = new byte[1024];
            while (rw.read(bytes) != -1) {
                tempRw.write(bytes);
            }
        }

        return mpFile;
    }

    /**
     * 创建文件（或目录）如果不存在的话。
     *
     * @param pathname 路径名字符串。
     * @param isDir    true 作为目录否则视为文件。
     * @return 已创建的指定文件。
     * @throws IOException 如果发生 I/O 错误。
     */
    private static File createFileIfNotExist(String pathname, boolean isDir) throws IOException {
        File file = new File(pathname);
        if (!file.exists() && !(isDir ? file.mkdirs() : file.createNewFile())) {
            throw new RuntimeException("文件创建失败：" + file.getAbsolutePath());
        }
        return file;
    }

}