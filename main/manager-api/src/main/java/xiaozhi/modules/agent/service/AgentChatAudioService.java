package xiaozhi.modules.agent.service;

import com.baomidou.mybatisplus.extension.service.IService;

import xiaozhi.modules.agent.entity.AgentChatAudioEntity;


public interface AgentChatAudioService extends IService<AgentChatAudioEntity> {
    
    String saveAudio(byte[] audioData);

    
    byte[] getAudio(String audioId);
}
