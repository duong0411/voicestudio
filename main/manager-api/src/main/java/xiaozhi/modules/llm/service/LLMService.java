package xiaozhi.modules.llm.service;


public interface LLMService {

    
    String generateSummary(String conversation, String promptTemplate);

    
    String generateSummary(String conversation);

    
    String generateSummaryWithModel(String conversation, String modelId);

    
    String generateSummary(String conversation, String promptTemplate, String modelId);

    
    String generateSummaryWithHistory(String conversation, String historyMemory, String promptTemplate, String modelId);

    
    boolean isAvailable();

    
    boolean isAvailable(String modelId);

    
    String generateTitle(String conversation, String modelId);
}