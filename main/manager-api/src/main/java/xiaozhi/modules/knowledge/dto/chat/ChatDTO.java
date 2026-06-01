package xiaozhi.modules.knowledge.dto.chat;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;


@Schema(description = " DTO")
public class ChatDTO {



    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class PromptConfig implements Serializable {

        @Schema(description = "", example = "...")
        @JsonProperty("prompt")
        private String systemPrompt;

        @Schema(description = "", example = "，，？")
        private String opener;

        @Schema(description = "", example = "，。")
        @JsonProperty("empty_response")
        private String emptyResponse;

        @Schema(description = "", example = "true")
        @JsonProperty("show_quote")
        private Boolean quote;

        @Schema(description = " TTS", example = "false")
        private Boolean tts;

        @Schema(description = " (0.0 - 1.0)", example = "0.2")
        @JsonProperty("similarity_threshold")
        private Float similarityThreshold;

        @Schema(description = " (0.0 - 1.0)", example = "0.7")
        @JsonProperty("keywords_similarity_weight")
        private Float vectorSimilarityWeight;

        @Schema(description = " Top N", example = "6")
        @JsonProperty("top_n")
        private Integer topK;

        @Schema(description = "Rerank ", example = "rerank_model_001")
        @JsonProperty("rerank_model")
        private String rerankId;

        @Schema(description = "", example = "false")
        @JsonProperty("refine_multiturn")
        private Boolean refineMultigraph;

        @Schema(description = "")
        private List<Map<String, Object>> variables;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "LLM ")
    public static class LLMConfig implements Serializable {

        @NotBlank(message = "")
        @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED, example = "gpt-4")
        @JsonProperty("model_name")
        private String modelName;

        @Schema(description = " (0.0 - 2.0)", example = "0.7")
        private Float temperature;

        @Schema(description = "Top P ", example = "0.9")
        @JsonProperty("top_p")
        private Float topP;

        @Schema(description = " Token ", example = "4096")
        @JsonProperty("max_tokens")
        private Integer maxTokens;

        @Schema(description = "", example = "0.0")
        @JsonProperty("presence_penalty")
        private Float presencePenalty;

        @Schema(description = "", example = "0.0")
        @JsonProperty("frequency_penalty")
        private Float frequencyPenalty;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class AssistantCreateReq implements Serializable {

        @NotBlank(message = "")
        @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
        private String name;

        @Schema(description = " (Base64 )", example = "")
        private String avatar;

        @Schema(description = " ID ", example = "[\"kb_001\", \"kb_002\"]")
        @JsonProperty("dataset_ids")
        private List<String> datasetIds;

        @Schema(description = "", example = "")
        private String description;

        @Schema(description = "LLM ")
        @JsonProperty("llm")
        private LLMConfig llm;

        @Schema(description = "")
        @JsonProperty("prompt")
        private PromptConfig promptConfig;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class AssistantUpdateReq implements Serializable {

        @Schema(description = "", example = " V2")
        private String name;

        @Schema(description = " (Base64 )", example = "")
        private String avatar;

        @Schema(description = " ID ", example = "[\"kb_001\", \"kb_002\"]")
        @JsonProperty("dataset_ids")
        private List<String> datasetIds;

        @Schema(description = "", example = "")
        private String description;

        @Schema(description = "LLM ")
        @JsonProperty("llm")
        private LLMConfig llm;

        @Schema(description = "")
        @JsonProperty("prompt")
        private PromptConfig promptConfig;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class AssistantListReq implements Serializable {

        @Schema(description = " ( 1 )", example = "1")
        private Integer page;

        @Schema(description = "", example = "30")
        @JsonProperty("page_size")
        private Integer pageSize;

        @Schema(description = " ()", example = "")
        private String name;

        @Schema(description = ": create_time / update_time", example = "create_time")
        private String orderby;

        @Schema(description = "", example = "true")
        private Boolean desc;

        @Schema(description = " ID ", example = "assistant_001")
        private String id;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = " VO")
    public static class AssistantVO implements Serializable {

        @Schema(description = " ID", example = "assistant_001")
        private String id;

        @Schema(description = " ID", example = "tenant_001")
        @JsonProperty("tenant_id")
        private String tenantId;

        @Schema(description = "", example = "")
        private String name;

        @Schema(description = "", example = "")
        private String avatar;

        @Schema(description = " ID ")
        @JsonProperty("dataset_ids")
        private List<String> datasetIds;

        @Schema(description = " ()")
        private List<SimpleDatasetVO> datasets;

        @Schema(description = "")
        private String description;

        @Schema(description = "LLM ")
        @JsonProperty("llm")
        private LLMConfig llm;

        @Schema(description = "")
        @JsonProperty("prompt")
        private PromptConfig promptConfig;

        @Schema(description = " ()", example = "1700000000000")
        @JsonProperty("create_time")
        private Long createTime;

        @Schema(description = " ()", example = "1700000001000")
        @JsonProperty("update_time")
        private Long updateTime;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class AssistantDeleteReq implements Serializable {

        @Schema(description = " ID ", example = "[\"assistant_001\", \"assistant_002\"]")
        private List<String> ids;
    }



    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class SessionCreateReq implements Serializable {

        @Schema(description = "", example = "")
        private String name;

        @Schema(description = " ID", example = "user_001")
        @JsonProperty("user_id")
        private String userId;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class SessionUpdateReq implements Serializable {

        @Schema(description = "", example = " - ")
        private String name;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class SessionListReq implements Serializable {

        @Schema(description = " ID", example = "assistant_001")
        @JsonProperty("assistant_id")
        private String assistantId;

        @Schema(description = " ( 1 )", example = "1")
        private Integer page;

        @Schema(description = "", example = "30")
        @JsonProperty("page_size")
        private Integer pageSize;

        @Schema(description = "", example = "")
        private String name;

        @Schema(description = "", example = "create_time")
        private String orderby;

        @Schema(description = "", example = "true")
        private Boolean desc;

        @Schema(description = " ID ", example = "session_001")
        private String id;

        @Schema(description = "", example = "user_001")
        @JsonProperty("user_id")
        private String userId;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = " VO")
    public static class SessionVO implements Serializable {

        @Schema(description = " ID", example = "session_001")
        private String id;

        @Schema(description = " ID", example = "assistant_001")
        @JsonProperty("chat_id")
        private String chatId;

        @Schema(description = " ID ()", example = "assistant_001")
        @JsonProperty("assistant_id")
        private String assistantId;

        @Schema(description = "", example = "")
        private String name;

        @Schema(description = " ()", example = "1700000000000")
        @JsonProperty("create_time")
        private Long createTime;

        @Schema(description = " ()", example = "1700000001000")
        @JsonProperty("update_time")
        private Long updateTime;

        @Schema(description = "", example = "2024-05-01 10:00:00")
        @JsonProperty("create_date")
        private String createDate;

        @Schema(description = "", example = "2024-05-01 10:00:00")
        @JsonProperty("update_date")
        private String updateDate;

        @Schema(description = " ID", example = "user_001")
        @JsonProperty("user_id")
        private String userId;

        @Schema(description = "")
        private List<Map<String, Object>> messages;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class SessionDeleteReq implements Serializable {

        @Schema(description = " ID ", example = "[\"session_001\", \"session_002\"]")
        private List<String> ids;
    }



    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class CompletionReq implements Serializable {

        @NotBlank(message = "")
        @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
        private String question;

        @Schema(description = " (SSE)", example = "true")
        @Builder.Default
        private Boolean stream = true;

        @NotBlank(message = " ID ")
        @Schema(description = " ID (，)", example = "session_001")
        @JsonProperty("session_id")
        private String sessionId;

        @Schema(description = "", example = "true")
        private Boolean quote;

        @Schema(description = " ID  ()", example = "doc_001,doc_002")
        @JsonProperty("doc_ids")
        private String docIds;

        @Schema(description = "")
        @JsonProperty("metadata_condition")
        private Map<String, Object> metadataCondition;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = " VO")
    public static class CompletionVO implements Serializable {

        @Schema(description = "AI ")
        private String answer;

        @Schema(description = "")
        private Reference reference;

        @Schema(description = " ID", example = "session_001")
        @JsonProperty("session_id")
        private String sessionId;

        @Schema(description = " ID ()", example = "task_001")
        @JsonProperty("task_id")
        private String taskId;

        
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        @Schema(description = "")
        public static class Reference implements Serializable {

            @Schema(description = "")
            private List<xiaozhi.modules.knowledge.dto.document.RetrievalDTO.HitVO> chunks;

            @Schema(description = "")
            @JsonProperty("doc_aggs")
            private List<DocAgg> docAggs;
        }

        
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        @Schema(description = "")
        public static class DocAgg implements Serializable {

            @Schema(description = " ID", example = "doc_001")
            @JsonProperty("doc_id")
            private String docId;

            @Schema(description = "", example = ".pdf")
            @JsonProperty("doc_name")
            private String docName;

            @Schema(description = "", example = "3")
            private Integer count;
        }
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = " VO")
    public static class SimpleDatasetVO implements Serializable {
        @Schema(description = " ID")
        private String id;
        @Schema(description = "")
        private String name;
        @Schema(description = "")
        private String avatar;
        @Schema(description = "")
        @JsonProperty("chunk_num")
        private Integer chunkNum;
    }
}
