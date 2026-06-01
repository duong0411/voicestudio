package xiaozhi.modules.agent.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;


@TableName(value = "ai_agent_voice_print")
@Data
public class AgentVoicePrintEntity {
    
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    private String agentId;
    
    private String audioId;
    
    private String sourceName;
    
    private String introduce;

    
    @TableField(fill = FieldFill.INSERT)
    private Long creator;
    
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;

    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updater;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;
}
