package combine.bilibili;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class VideoConverterTest {

    @Test
    public void testConvert() throws IOException {
        File biPath = new File("/path");
        File[] files = biPath.listFiles();
        if (files == null) {
            return;
        }

        List<String> paths = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                paths.add(file.getAbsolutePath());
            }
        }

        log.info("Paths: {}.", paths.size());

        VideoConverter videoConverter = new VideoConverter(paths);
        videoConverter.start();
    }

}