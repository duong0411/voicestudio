package xiaozhi.modules.correctword.service;

import java.util.List;
import java.util.Map;

import xiaozhi.common.page.PageData;
import xiaozhi.modules.correctword.dto.CorrectWordFileCreateDTO;
import xiaozhi.modules.correctword.vo.CorrectWordFileVO;
import xiaozhi.modules.correctword.vo.CorrectWordSimpleVO;

public interface CorrectWordFileService {

    
    CorrectWordFileVO createFile(CorrectWordFileCreateDTO dto);

    
    void updateFile(String fileId, CorrectWordFileCreateDTO dto);

    
    PageData<CorrectWordFileVO> listFiles(Map<String, Object> params);

    
    List<CorrectWordFileVO> listAllFiles();

    
    CorrectWordFileVO getFileContent(String fileId);

    
    void deleteFile(String fileId);

    
    void deleteMappingsByAgentId(String agentId);

    
    List<CorrectWordSimpleVO> getAllItemsByAgentId(String agentId);

    
    List<String> getAgentCorrectWordFileIds(String agentId);

    
    void saveAgentCorrectWords(String agentId, List<String> fileIds);

    
    void batchDeleteFiles(List<String> fileIds);
}
