package xiaozhi.modules.timbre.service;

import java.util.List;

import xiaozhi.common.page.PageData;
import xiaozhi.common.service.BaseService;
import xiaozhi.modules.model.dto.VoiceDTO;
import xiaozhi.modules.timbre.dto.TimbreDataDTO;
import xiaozhi.modules.timbre.dto.TimbrePageDTO;
import xiaozhi.modules.timbre.entity.TimbreEntity;
import xiaozhi.modules.timbre.vo.TimbreDetailsVO;


public interface TimbreService extends BaseService<TimbreEntity> {
    
    PageData<TimbreDetailsVO> page(TimbrePageDTO dto);

    
    TimbreDetailsVO get(String timbreId);

    
    void save(TimbreDataDTO dto);

    
    void update(String timbreId, TimbreDataDTO dto);

    
    void delete(String[] ids);

    List<VoiceDTO> getVoiceNames(String ttsModelId, String voiceName);

    
    String getTimbreNameById(String id);

    
    VoiceDTO getByVoiceCode(String ttsModelId, String voiceCode);
}