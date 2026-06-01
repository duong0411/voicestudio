package xiaozhi.modules.knowledge.dto.document;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;


@Schema(description = " DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RetrievalDTO {

    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DocAggVO implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "")
        @JsonProperty("doc_name")
        private String docName;

        @Schema(description = " ID")
        @JsonProperty("doc_id")
        private String docId;

        @Schema(description = "")
        private Integer count;
    }

    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TestReq implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = " ID ", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("dataset_ids")
        @NotEmpty(message = "ID")
        private List<String> datasetIds;

        @Schema(description = " ID  (，)")
        @JsonProperty("document_ids")
        private List<String> documentIds;

        @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "")
        private String question;

        @Schema(description = " ( 1)")
        private Integer page;

        @Schema(description = " ( 10)")
        @JsonProperty("page_size")
        private Integer pageSize;

        @Schema(description = " ( 0.2)")
        @JsonProperty("similarity_threshold")
        private Float similarityThreshold;

        @Schema(description = " ( 0.3)")
        @JsonProperty("vector_similarity_weight")
        private Float vectorSimilarityWeight;

        @Schema(description = " Top K  ( 1024)")
        @JsonProperty("top_k")
        private Integer topK;

        @Schema(description = " ID")
        @JsonProperty("rerank_id")
        private String rerankId;

        @Schema(description = "")
        private Boolean highlight;

        @Schema(description = "")
        private Boolean keyword;

        @Schema(description = " ()")
        @JsonProperty("cross_languages")
        private List<String> crossLanguages;

        @Schema(description = " (JSON )")
        @JsonProperty("metadata_condition")
        private Map<String, Object> metadataCondition;
    }

    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HitVO implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = " ID", requiredMode = Schema.RequiredMode.REQUIRED)
        private String id;

        @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED)
        private String content;

        @Schema(description = " ID", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("document_id")
        private String documentId;

        @Schema(description = " ID")
        @JsonProperty("dataset_id")
        private String datasetId;

        @Schema(description = "")
        @JsonProperty("document_name")
        private String documentName;

        @Schema(description = "")
        @JsonProperty("document_keyword")
        private String documentKeyword;

        @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED)
        private Float similarity;

        @Schema(description = "")
        @JsonProperty("vector_similarity")
        private Float vectorSimilarity;

        @Schema(description = "")
        @JsonProperty("term_similarity")
        private Float termSimilarity;

        @Schema(description = "")
        private Integer index;

        @Schema(description = "")
        private String highlight;

        @Schema(description = "")
        @JsonProperty("important_keywords")
        private List<String> importantKeywords;

        @Schema(description = "")
        private List<String> questions;

        @Schema(description = " ID")
        @JsonProperty("image_id")
        private String imageId;

        @Schema(description = " (RAGFlow,  [[start, end, filename]])")
        private Object positions;
    }

    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MetaSummaryVO implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("total_doc_count")
        private Long totalDocCount;

        @Schema(description = "Token ", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("total_token_count")
        private Long totalTokenCount;

        @Schema(description = " (key: , value: )")
        @JsonProperty("file_type_distribution")
        private Map<String, Long> fileTypeDistribution;

        @Schema(description = " (key: , value: )")
        @JsonProperty("status_distribution")
        private Map<String, Long> statusDistribution;

        @Schema(description = " (key: , value: /)")
        @JsonProperty("custom_metadata")
        private Map<String, Object> customMetadata;
    }

    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MetaBatchReq implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = ":  ()")
        private Selector selector;

        @Schema(description = "")
        private List<UpdateItem> updates;

        @Schema(description = "")
        private List<DeleteItem> deletes;

        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @Schema(description = "")
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Selector implements Serializable {
            private static final long serialVersionUID = 1L;

            @Schema(description = " ID ")
            @JsonProperty("document_ids")
            private List<String> documentIds;

            @Schema(description = " (key: , value: )")
            @JsonProperty("metadata_condition")
            private Map<String, Object> metadataCondition;
        }

        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @Schema(description = "")
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class UpdateItem implements Serializable {
            private static final long serialVersionUID = 1L;

            @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED)
            private String key;

            @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED)
            private Object value;
        }

        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @Schema(description = "")
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DeleteItem implements Serializable {
            private static final long serialVersionUID = 1L;

            @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED)
            private String key;
        }
    }

    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResultVO implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "")
        private List<HitVO> chunks;

        @Schema(description = "")
        @JsonProperty("doc_aggs")
        private List<DocAggVO> docAggs;

        @Schema(description = "")
        private Long total;
    }
}
