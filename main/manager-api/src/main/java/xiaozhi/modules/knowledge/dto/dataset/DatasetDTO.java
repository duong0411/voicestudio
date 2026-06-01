package xiaozhi.modules.knowledge.dto.dataset;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;


@Schema(description = " DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatasetDTO {



    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ParserConfig implements Serializable {

        @Schema(description = " token ", example = "128")
        @JsonProperty("chunk_token_num")
        private Integer chunkTokenNum;

        @Schema(description = "", example = "\\n!?;。；！？")
        private String delimiter;

        @Schema(description = ": DeepDOC / Simple", example = "DeepDOC")
        @JsonProperty("layout_recognize")
        private String layoutRecognize;

        @Schema(description = " Excel  HTML", example = "false")
        private Boolean html4excel;

        @Schema(description = " (0 )", example = "0")
        @JsonProperty("auto_keywords")
        private Integer autoKeywords;

        @Schema(description = " (0 )", example = "0")
        @JsonProperty("auto_questions")
        private Integer autoQuestions;
    }



    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateReq implements Serializable {

        @NotBlank(message = "")
        @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED, example = "my_dataset")
        private String name;

        @Schema(description = " (Base64 )", example = "")
        private String avatar;

        @Schema(description = "", example = "")
        private String description;

        @Schema(description = "", example = "BAAI/bge-large-zh-v1.5")
        @JsonProperty("embedding_model")
        private String embeddingModel;

        @Schema(description = ": me / team", example = "me")
        private String permission;

        @Schema(description = ": naive / manual / qa / table / paper / book / laws / presentation / picture / one / knowledge_graph / email", example = "naive")
        @JsonProperty("chunk_method")
        private String chunkMethod;

        @Schema(description = "")
        @JsonProperty("parser_config")
        private ParserConfig parserConfig;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdateReq implements Serializable {

        @Schema(description = "", example = "updated_dataset")
        private String name;

        @Schema(description = " (Base64 )", example = "")
        private String avatar;

        @Schema(description = "", example = "")
        private String description;

        @Schema(description = ": me / team", example = "team")
        private String permission;

        @Schema(description = "", example = "BAAI/bge-large-zh-v1.5")
        @JsonProperty("embedding_model")
        private String embeddingModel;

        @Schema(description = ": naive / manual / qa / table / paper / book / laws / presentation / picture / one / knowledge_graph / email", example = "naive")
        @JsonProperty("chunk_method")
        private String chunkMethod;

        @Schema(description = "")
        @JsonProperty("parser_config")
        private ParserConfig parserConfig;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Schema(description = "PageRank  (0-100)", example = "50")
        private Integer pagerank;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListReq implements Serializable {

        @Schema(description = " ( 1 )", example = "1")
        private Integer page;

        @Schema(description = "", example = "30")
        @JsonProperty("page_size")
        private Integer pageSize;

        @Schema(description = ": create_time / update_time", example = "create_time")
        private String orderby;

        @Schema(description = "", example = "true")
        private Boolean desc;

        @Schema(description = " ()", example = "my_dataset")
        private String name;

        @Schema(description = " ID ", example = "abc123")
        private String id;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class BatchIdReq implements Serializable {

        @NotNull(message = " ID ")
        @Size(min = 1, message = " ID")
        @Schema(description = " ID ", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"id1\", \"id2\"]")
        private List<String> ids;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = " GraphRAG ")
    public static class RunGraphRagReq implements Serializable {

        @Schema(description = "", example = "[\"person\", \"organization\"]")
        @JsonProperty("entity_types")
        private List<String> entityTypes;

        @Schema(description = ": light / fast / full", example = "light")
        private String method;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = " RAPTOR ")
    public static class RunRaptorReq implements Serializable {

        @Schema(description = "", example = "64")
        @JsonProperty("max_cluster")
        private Integer maxCluster;

        @Schema(description = "", example = "...")
        private String prompt;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = " ID ")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TaskIdVO implements Serializable {

        @Schema(description = "GraphRAG  ID", example = "task_uuid_12345678")
        @JsonProperty("graphrag_task_id")
        private String graphragTaskId;

        @Schema(description = "RAPTOR  ID", example = "task_uuid_87654321")
        @JsonProperty("raptor_task_id")
        private String raptorTaskId;
    }



    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = " VO")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InfoVO implements Serializable {

        @Schema(description = " ID", example = "abc123")
        private String id;

        @Schema(description = "", example = "my_dataset")
        private String name;

        @Schema(description = " (Base64 )", example = "")
        private String avatar;

        @Schema(description = " ID", example = "tenant_001")
        @JsonProperty("tenant_id")
        private String tenantId;

        @Schema(description = "", example = "")
        private String description;

        @Schema(description = "", example = "BAAI/bge-large-zh-v1.5")
        @JsonProperty("embedding_model")
        private String embeddingModel;

        @Schema(description = ": me / team", example = "me")
        private String permission;

        @Schema(description = "", example = "naive")
        @JsonProperty("chunk_method")
        private String chunkMethod;

        @Schema(description = "")
        @JsonProperty("parser_config")
        private ParserConfig parserConfig;

        @Schema(description = "", example = "1024")
        @JsonProperty("chunk_count")
        private Long chunkCount;

        @Schema(description = "", example = "50")
        @JsonProperty("document_count")
        private Long documentCount;

        @Schema(description = " ()", example = "1700000000000")
        @JsonProperty("create_time")
        private Long createTime;

        @Schema(description = " ()", example = "1700000001000")
        @JsonProperty("update_time")
        private Long updateTime;

        @Schema(description = " Token ", example = "102400")
        @JsonProperty("token_num")
        private Long tokenNum;

        @Schema(description = " (: yyyy-MM-dd HH:mm:ss)")
        @JsonProperty("create_date")
        private String createDate;

        @Schema(description = " (: yyyy-MM-dd HH:mm:ss)")
        @JsonProperty("update_date")
        private String updateDate;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = " VO")
    public static class BatchOperationVO implements Serializable {

        @Schema(description = "", example = "5")
        @JsonProperty("success_count")
        private Integer successCount;

        @Schema(description = "")
        private List<Object> errors;
    }



    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = " VO")
    public static class GraphVO implements Serializable {

        @Schema(description = "")
        private List<Node> nodes;

        @Schema(description = "")
        private List<Edge> edges;

        @Schema(description = "")
        @JsonProperty("mind_map")
        private Map<String, Object> mindMap;

        
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        @Schema(description = "")
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Node implements Serializable {

            @Schema(description = " ID", example = "node_001")
            private String id;

            @Schema(description = "", example = "")
            private String label;

            @Schema(description = "PageRank ", example = "0.85")
            private Double pagerank;

            @Schema(description = "", example = "#FF5733")
            private String color;

            @Schema(description = " URL", example = "https://example.com/icon.png")
            private String img;
        }

        
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        @Schema(description = "")
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Edge implements Serializable {

            @Schema(description = " ID", example = "node_001")
            private String source;

            @Schema(description = " ID", example = "node_002")
            private String target;

            @Schema(description = "", example = "0.75")
            private Double weight;

            @Schema(description = " ()", example = "")
            private String label;
        }
    }



    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = " VO")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TaskTraceVO implements Serializable {

        @Schema(description = " ID", example = "task_001")
        private String id;

        @Schema(description = " ID", example = "doc_001")
        @JsonProperty("doc_id")
        private String docId;

        @Schema(description = "", example = "1")
        @JsonProperty("from_page")
        private Integer fromPage;

        @Schema(description = "", example = "10")
        @JsonProperty("to_page")
        private Integer toPage;

        @Schema(description = " (0.0 - 1.0)", example = "0.75")
        private Double progress;

        @Schema(description = "", example = " 5 ...")
        @JsonProperty("progress_msg")
        private String progressMsg;

        @Schema(description = " ()", example = "1700000000000")
        @JsonProperty("create_time")
        private Long createTime;

        @Schema(description = " ()", example = "1700000001000")
        @JsonProperty("update_time")
        private Long updateTime;
    }
}
