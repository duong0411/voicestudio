package xiaozhi.modules.voiceclone.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "DTO")
public class VoiceCloneResponseDTO {

    @Schema(description = "")
    private String id;

    @Schema(description = "")
    private String name;

    @Schema(description = "id")
    private String modelId;

    @Schema(description = "")
    private String modelName;

    @Schema(description = "id")
    private String voiceId;

    @Schema(description = "")
    private String languages;

    @Schema(description = "ID（）")
    private Long userId;

    @Schema(description = "")
    private String userName;

    @Schema(description = "：0 1 2 3")
    private Integer trainStatus;

    @Schema(description = "")
    private String trainError;

    @Schema(description = "")
    private Date createDate;

    @Schema(description = "")
    private Boolean hasVoice;
}