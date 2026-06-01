package xiaozhi.modules.agent.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import xiaozhi.common.page.PageData;
import xiaozhi.modules.agent.dto.AgentChatHistoryDTO;
import xiaozhi.modules.agent.dto.AgentChatSessionDTO;
import xiaozhi.modules.agent.entity.AgentChatHistoryEntity;
import xiaozhi.modules.agent.vo.AgentChatHistoryUserVO;


public interface AgentChatHistoryService extends IService<AgentChatHistoryEntity> {

    
    PageData<AgentChatSessionDTO> getSessionListByAgentId(Map<String, Object> params);

    
    List<AgentChatHistoryDTO> getChatHistoryBySessionId(String agentId, String sessionId);

    
    void deleteByAgentId(String agentId, Boolean deleteAudio, Boolean deleteText);

    
    List<AgentChatHistoryUserVO> getRecentlyFiftyByAgentId(String agentId);

    
    String getContentByAudioId(String audioId);


    
    boolean isAudioOwnedByAgent(String audioId,String agentId);
}
