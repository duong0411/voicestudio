package xiaozhi.modules.agent.service;

import java.util.List;
import java.util.Map;

import xiaozhi.common.page.PageData;
import xiaozhi.common.service.BaseService;
import xiaozhi.modules.agent.dto.AgentCreateDTO;
import xiaozhi.modules.agent.dto.AgentDTO;
import xiaozhi.modules.agent.dto.AgentUpdateDTO;
import xiaozhi.modules.agent.entity.AgentEntity;
import xiaozhi.modules.agent.vo.AgentInfoVO;


public interface AgentService extends BaseService<AgentEntity> {
    
    PageData<AgentEntity> adminAgentList(Map<String, Object> params);

    
    AgentInfoVO getAgentById(String id);

    
    boolean insert(AgentEntity entity);

    
    void deleteAgentByUserId(Long userId);

    
    List<AgentDTO> getUserAgents(Long userId, String keyword, String searchType);

    
    Integer getDeviceCountByAgentId(String agentId);

    
    AgentEntity getDefaultAgentByMacAddress(String macAddress);

    
    boolean checkAgentPermission(String agentId, Long userId);

    
    void updateAgentById(String agentId, AgentUpdateDTO dto);

    
    String createAgent(AgentCreateDTO dto);


}
