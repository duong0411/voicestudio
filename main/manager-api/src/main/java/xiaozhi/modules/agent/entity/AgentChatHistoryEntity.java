package xiaozhi.modules.agent.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "ai_agent_chat_history")
public class AgentChatHistoryEntity {
    
    @TableId(type = IdType.AUTO)
    private Long id;

    
    @TableField(value = "mac_address")
    private String macAddress;

    
    @TableField(value = "agent_id")
    private String agentId;

    
    @TableField(value = "session_id")
    private String sessionId;

    
    @TableField(value = "chat_type")
    private Byte chatType;

    
    @TableField(value = "content")
    private String content;

    
    @TableField(value = "audio_id")
    private String audioId;

    
    @TableField(value = "created_at")
    private Date createdAt;

    
    @TableField(value = "updated_at")
    private Date updatedAt;
}
