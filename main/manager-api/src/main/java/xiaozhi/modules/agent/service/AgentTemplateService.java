package xiaozhi.modules.agent.service;

import com.baomidou.mybatisplus.extension.service.IService;

import xiaozhi.modules.agent.entity.AgentTemplateEntity;


public interface AgentTemplateService extends IService<AgentTemplateEntity> {

    
    AgentTemplateEntity getDefaultTemplate();

    
    void updateDefaultTemplateModelId(String modelType, String modelId);

    
    void reorderTemplatesAfterDelete(Integer deletedSort);

    
    Integer getNextAvailableSort();
}
