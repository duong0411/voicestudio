package xiaozhi.modules.knowledge.rag;

import java.util.List;
import java.util.Map;

import xiaozhi.modules.knowledge.dto.dataset.DatasetDTO;

import xiaozhi.common.page.PageData;
import xiaozhi.modules.knowledge.dto.KnowledgeFilesDTO;
import xiaozhi.modules.knowledge.dto.document.DocumentDTO;
import xiaozhi.modules.knowledge.dto.document.ChunkDTO;
import xiaozhi.modules.knowledge.dto.document.RetrievalDTO;
import java.util.function.Consumer;


public abstract class KnowledgeBaseAdapter {

        
        public abstract String getAdapterType();

        
        public abstract void initialize(Map<String, Object> config);

        
        public abstract boolean validateConfig(Map<String, Object> config);

        
        public abstract PageData<KnowledgeFilesDTO> getDocumentList(String datasetId,
                        DocumentDTO.ListReq req);

        
        public abstract DocumentDTO.InfoVO getDocumentById(String datasetId, String documentId);

        
        public abstract KnowledgeFilesDTO uploadDocument(DocumentDTO.UploadReq req);

        
        public abstract PageData<KnowledgeFilesDTO> getDocumentListByStatus(String datasetId,
                        Integer status,
                        Integer page,
                        Integer limit);

        
        public abstract void deleteDocument(String datasetId, DocumentDTO.BatchIdReq req);

        
        public abstract boolean parseDocuments(String datasetId, List<String> documentIds);

        
        public abstract ChunkDTO.ListVO listChunks(String datasetId,
                        String documentId,
                        ChunkDTO.ListReq req);

        
        public abstract RetrievalDTO.ResultVO retrievalTest(
                        RetrievalDTO.TestReq req);

        
        public abstract boolean testConnection();

        
        public abstract Map<String, Object> getStatus();

        
        public abstract Map<String, Object> getSupportedConfig();

        
        public abstract Map<String, Object> getDefaultConfig();

        
        public abstract DatasetDTO.InfoVO createDataset(DatasetDTO.CreateReq req);

        
        public abstract DatasetDTO.InfoVO updateDataset(String datasetId, DatasetDTO.UpdateReq req);

        
        public abstract DatasetDTO.BatchOperationVO deleteDataset(DatasetDTO.BatchIdReq req);

        
        public abstract Integer getDocumentCount(String datasetId);

        
        public abstract DatasetDTO.InfoVO getDatasetInfo(String datasetId);

        
        public abstract void postStream(String endpoint, Object body, Consumer<String> onData);

        
        public abstract Object postSearchBotAsk(Map<String, Object> config, Object body,
                        Consumer<String> onData);

        
        public abstract void postAgentBotCompletion(Map<String, Object> config, String agentId, Object body,
                        Consumer<String> onData);
}