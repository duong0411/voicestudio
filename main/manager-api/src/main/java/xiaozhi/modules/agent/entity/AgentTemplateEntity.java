package xiaozhi.modules.agent.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;


@TableName(value = "ai_agent_template")
@Data
public class AgentTemplateEntity implements Serializable {
    
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    
    private String agentCode;

    
    private String agentName;

    
    private String asrModelId;

    
    private String vadModelId;

    
    private String llmModelId;

    
    private String vllmModelId;

    
    private String ttsModelId;

    
    private String ttsVoiceId;

    
    private String ttsLanguage;

    
    private Integer ttsVolume;

    
    private Integer ttsRate;

    
    private Integer ttsPitch;

    
    private String memModelId;

    
    private String intentModelId;

    
    private Integer chatHistoryConf;

    
    private String systemPrompt;

    
    private String summaryMemory;
    
    private String langCode;

    
    private String language;

    
    private Integer sort;

    
    private Long creator;

    
    private Date createdAt;

    
    private Long updater;

    
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}