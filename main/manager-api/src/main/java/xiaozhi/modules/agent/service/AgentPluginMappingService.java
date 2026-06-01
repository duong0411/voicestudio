package xiaozhi.modules.agent.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import xiaozhi.modules.agent.entity.AgentPluginMapping;


public interface AgentPluginMappingService extends IService<AgentPluginMapping> {

    
    List<AgentPluginMapping> agentPluginParamsByAgentId(String agentId);

    
    void deleteByAgentId(String agentId);
}
