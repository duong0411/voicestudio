package xiaozhi.modules.agent.service;


import java.util.List;


public interface AgentMcpAccessPointService {
    
   String getAgentMcpAccessAddress(String id);

    
   List<String> getAgentMcpToolsList(String id);
}
