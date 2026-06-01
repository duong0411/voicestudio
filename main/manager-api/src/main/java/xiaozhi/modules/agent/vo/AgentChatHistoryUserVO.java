package xiaozhi.modules.agent.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class AgentChatHistoryUserVO {
    @Schema(description = "")
    private String content;

    @Schema(description = "ID")
    private String audioId;
}
