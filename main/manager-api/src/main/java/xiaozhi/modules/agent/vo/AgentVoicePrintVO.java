package xiaozhi.modules.agent.vo;

import lombok.Data;

import java.util.Date;


@Data
public class AgentVoicePrintVO {

    
    private String id;
    
    private String audioId;
    
    private String sourceName;
    
    private String introduce;
    
    private Date createDate;
}
