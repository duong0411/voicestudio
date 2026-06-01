package xiaozhi.modules.agent.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import xiaozhi.modules.agent.entity.AgentChatAudioEntity;


@Mapper
public interface AiAgentChatAudioDao extends BaseMapper<AgentChatAudioEntity> {
}