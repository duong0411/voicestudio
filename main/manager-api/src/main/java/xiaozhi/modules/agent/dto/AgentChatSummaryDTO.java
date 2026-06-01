package xiaozhi.modules.agent.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "")
public class AgentChatSummaryDTO {

    @Schema(description = "ID")
    private String sessionId;

    @Schema(description = "ID")
    private String agentId;

    @Schema(description = "")
    private String summary;

    @Schema(description = "")
    private boolean success;

    @Schema(description = "")
    private String errorMessage;

    public AgentChatSummaryDTO() {
        this.success = true;
    }

    public AgentChatSummaryDTO(String sessionId, String agentId, String summary) {
        this.sessionId = sessionId;
        this.agentId = agentId;
        this.summary = summary;
        this.success = true;
    }

    public AgentChatSummaryDTO(String sessionId, String errorMessage) {
        this.sessionId = sessionId;
        this.errorMessage = errorMessage;
        this.success = false;
    }

}