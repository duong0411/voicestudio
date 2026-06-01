package xiaozhi.modules.voiceclone.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO")
public class VoiceCloneDTO {

    @Schema(description = "ID")
    private String modelId;

    @Schema(description = "ID")
    private List<String> voiceIds;

    @Schema(description = "ID")
    private Long userId;

    @Schema(description = "")
    private String languages;
}
