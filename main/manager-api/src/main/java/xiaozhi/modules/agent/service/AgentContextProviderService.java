package xiaozhi.modules.agent.service;

import xiaozhi.common.service.BaseService;
import xiaozhi.modules.agent.entity.AgentContextProviderEntity;

public interface AgentContextProviderService extends BaseService<AgentContextProviderEntity> {
    
    AgentContextProviderEntity getByAgentId(String agentId);

    
    void saveOrUpdateByAgentId(AgentContextProviderEntity entity);

    
    void deleteByAgentId(String agentId);
}
