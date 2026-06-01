package xiaozhi.modules.timbre.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class TimbreDetailsVO implements Serializable {
    @Schema(description = "id")
    private String id;

    @Schema(description = "")
    private String languages;

    @Schema(description = "")
    private String name;

    @Schema(description = "")
    private String remark;

    @Schema(description = "")
    private String referenceAudio;

    @Schema(description = "")
    private String referenceText;

    @Schema(description = "")
    private long sort;

    @Schema(description = " TTS ")
    private String ttsModelId;

    @Schema(description = "")
    private String ttsVoice;

    @Schema(description = "")
    private String voiceDemo;

}
