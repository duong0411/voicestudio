package xiaozhi.modules.agent.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@Schema(description = "")
public class AgentChatHistoryReportDTO {
    @Schema(description = "MAC", example = "00:11:22:33:44:55")
    @NotBlank
    private String macAddress;
    @Schema(description = "ID", example = "79578c31-f1fb-426a-900e-1e934215f05a")
    @NotBlank
    private String sessionId;
    @Schema(description = ": 1-, 2-", example = "1")
    @NotNull
    private Byte chatType;
    @Schema(description = "", example = "")
    @NotBlank
    private String content;
    @Schema(description = "base64opus", example = "")
    private String audioBase64;
    @Schema(description = "，，", example = "1745657732")
    private Long reportTime;
}
