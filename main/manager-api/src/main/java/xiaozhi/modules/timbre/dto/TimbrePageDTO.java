package xiaozhi.modules.timbre.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@Schema(description = "")
public class TimbrePageDTO {

    @Schema(description = " TTS ")
    @NotBlank(message = "{timbre.ttsModelId.require}")
    private String ttsModelId;

    @Schema(description = "")
    private String name;

    @Schema(description = "")
    private String page;

    @Schema(description = "")
    private String limit;
}
