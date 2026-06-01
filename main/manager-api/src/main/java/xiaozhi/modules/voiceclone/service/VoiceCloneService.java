package xiaozhi.modules.voiceclone.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import xiaozhi.common.page.PageData;
import xiaozhi.common.service.BaseService;
import xiaozhi.modules.voiceclone.dto.VoiceCloneDTO;
import xiaozhi.modules.voiceclone.dto.VoiceCloneResponseDTO;
import xiaozhi.modules.voiceclone.entity.VoiceCloneEntity;


public interface VoiceCloneService extends BaseService<VoiceCloneEntity> {

    
    PageData<VoiceCloneEntity> page(Map<String, Object> params);

    
    void save(VoiceCloneDTO dto);

    
    void delete(String[] ids);

    
    List<VoiceCloneEntity> getByUserId(Long userId);

    
    PageData<VoiceCloneResponseDTO> pageWithNames(Map<String, Object> params);

    
    VoiceCloneResponseDTO getByIdWithNames(String id);

    
    List<VoiceCloneResponseDTO> getByUserIdWithNames(Long userId);

    
    void uploadVoice(String id, MultipartFile voiceFile) throws Exception;

    
    void updateName(String id, String name);

    
    byte[] getVoiceData(String id);

    
    void cloneAudio(String cloneId);
}
