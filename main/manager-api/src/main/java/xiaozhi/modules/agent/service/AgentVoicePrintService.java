package xiaozhi.modules.agent.service;

import java.util.List;

import xiaozhi.modules.agent.dto.AgentVoicePrintSaveDTO;
import xiaozhi.modules.agent.dto.AgentVoicePrintUpdateDTO;
import xiaozhi.modules.agent.vo.AgentVoicePrintVO;


public interface AgentVoicePrintService {
    
    boolean insert(AgentVoicePrintSaveDTO dto);

    
    boolean delete(Long userId, String voicePrintId);

    
    List<AgentVoicePrintVO> list(Long userId, String agentId);

    
    boolean update(Long userId, AgentVoicePrintUpdateDTO dto);

}
