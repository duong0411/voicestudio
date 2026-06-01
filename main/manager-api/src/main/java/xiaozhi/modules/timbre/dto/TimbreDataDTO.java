package xiaozhi.modules.timbre.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@Schema(description = "")
public class TimbreDataDTO {

    @Schema(description = "")
    @NotBlank(message = "{timbre.languages.require}")
    private String languages;

    @Schema(description = "")
    @NotBlank(message = "{timbre.name.require}")
    private String name;

    @Schema(description = "")
    private String remark;

    @Schema(description = "")
    private String referenceAudio;

    @Schema(description = "")
    private String referenceText;

    @Schema(description = "")
    @Min(value = 0, message = "{sort.number}")
    private long sort;

    @Schema(description = " TTS ")
    @NotBlank(message = "{timbre.ttsModelId.require}")
    private String ttsModelId;

    @Schema(description = "")
    @NotBlank(message = "{timbre.ttsVoice.require}")
    private String ttsVoice;

    @Schema(description = "")
    private String voiceDemo;
}