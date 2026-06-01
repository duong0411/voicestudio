package xiaozhi.modules.agent.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import xiaozhi.modules.agent.entity.AgentChatHistoryEntity;
import xiaozhi.modules.agent.entity.AgentVoicePrintEntity;


@Mapper
public interface AgentVoicePrintDao extends BaseMapper<AgentVoicePrintEntity> {

}
