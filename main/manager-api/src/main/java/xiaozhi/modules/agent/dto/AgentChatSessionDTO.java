package xiaozhi.modules.agent.dto;

import java.time.LocalDateTime;

import lombok.Data;


@Data
public class AgentChatSessionDTO {
    
    private String sessionId;

    
    private LocalDateTime createdAt;

    
    private Integer chatCount;

    
    private String title;
}