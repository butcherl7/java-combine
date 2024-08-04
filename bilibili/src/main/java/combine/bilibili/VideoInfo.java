package combine.bilibili;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 哔哩哔哩视频信息。
 *
 * @author Butcher
 * @since 2023-06-27
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoInfo {

    private Integer p;

    private String groupTitle;

    private String title;

}