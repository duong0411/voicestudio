package xiaozhi.modules.config.service;

import java.util.List;
import java.util.Map;

public interface ConfigService {
    
    Object getConfig(Boolean isCache);

    
    Map<String, Object> getAgentModels(String macAddress, Map<String, String> selectedModule);

    
    List<String> getCorrectWords(String macAddress);
}