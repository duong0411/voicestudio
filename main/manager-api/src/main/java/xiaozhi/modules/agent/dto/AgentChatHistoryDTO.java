package xiaozhi.modules.agent.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "")
public class AgentChatHistoryDTO {
    @Schema(description = "")
    private Date createdAt;

    @Schema(description = ": 1-, 2-")
    private Byte chatType;

    @Schema(description = "")
    private String content;

    @Schema(description = "ID")
    private String audioId;

    @Schema(description = "MAC")
    private String macAddress;
}