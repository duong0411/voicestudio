package xiaozhi.modules.correctword.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "VO（）")
public class CorrectWordSimpleVO {

    @Schema(description = "")
    private String sourceWord;

    @Schema(description = "")
    private String targetWord;
}
