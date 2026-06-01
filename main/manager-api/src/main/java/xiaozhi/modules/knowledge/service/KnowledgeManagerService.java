package xiaozhi.modules.knowledge.service;

import java.util.List;


public interface KnowledgeManagerService {

    
    void deleteDatasetWithFiles(String datasetId);

    
    void batchDeleteDatasetsWithFiles(List<String> datasetIds);
}
