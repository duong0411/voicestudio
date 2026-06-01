package xiaozhi.modules.agent.service;


public interface AgentChatSummaryService {

    
    boolean generateAndSaveChatSummary(String sessionId);

    
    boolean generateAndSaveChatTitle(String sessionId);
}