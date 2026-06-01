package xiaozhi.modules.agent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;


@Data
@TableName("ai_agent_chat_audio")
public class AgentChatAudioEntity {
    
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    
    private byte[] audio;
}