package xiaozhi.modules.knowledge.service;

import java.util.List;
import java.util.Map;

import xiaozhi.common.page.PageData;
import xiaozhi.common.service.BaseService;
import xiaozhi.modules.knowledge.dto.KnowledgeBaseDTO;
import xiaozhi.modules.knowledge.entity.KnowledgeBaseEntity;
import xiaozhi.modules.model.entity.ModelConfigEntity;


public interface KnowledgeBaseService extends BaseService<KnowledgeBaseEntity> {

    
    PageData<KnowledgeBaseDTO> getPageList(KnowledgeBaseDTO knowledgeBaseDTO, Integer page, Integer limit);

    
    KnowledgeBaseDTO getById(String id);

    
    KnowledgeBaseDTO save(KnowledgeBaseDTO knowledgeBaseDTO);

    
    KnowledgeBaseDTO update(KnowledgeBaseDTO knowledgeBaseDTO);

    
    KnowledgeBaseDTO getByDatasetId(String datasetId);

    
    List<KnowledgeBaseDTO> getByDatasetIdList(List<String> datasetIdList);

    
    void deleteByDatasetId(String datasetId);

    
    Map<String, Object> getRAGConfig(String ragModelId);

    
    Map<String, Object> getRAGConfigByDatasetId(String datasetId);

    
    List<ModelConfigEntity> getRAGModels();

    
    void updateStatistics(String datasetId, Integer docDelta, Long chunkDelta, Long tokenDelta);
}