package xiaozhi.modules.agent.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import xiaozhi.modules.agent.entity.AgentChatHistoryEntity;


@Mapper
public interface AiAgentChatHistoryDao extends BaseMapper<AgentChatHistoryEntity> {

    
    void deleteHistoryByAgentId(String agentId);

    
    void deleteAudioIdByAgentId(String agentId);

    
    List<String> getAudioIdsByAgentId(String agentId);

    
    void deleteAudioByIds(@Param("audioIds") List<String> audioIds);
}
