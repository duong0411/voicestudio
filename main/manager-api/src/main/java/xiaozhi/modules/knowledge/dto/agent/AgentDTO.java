package xiaozhi.modules.knowledge.dto.agent;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

@Schema(description = " (Agent)  DTO")
public class AgentDTO {


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Agent ")
    public static class CreateReq implements Serializable {
        @Schema(description = "Agent ", requiredMode = Schema.RequiredMode.REQUIRED, example = "My Agent")
        @NotBlank(message = "Agent ")
        @JsonProperty("title")
        private String title;

        @Schema(description = "DSL  ( JSON)", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "DSL ")
        @JsonProperty("dsl")
        private Map<String, Object> dsl;

        @Schema(description = "", example = " Agent")
        @JsonProperty("description")
        private String description;

        @Schema(description = " URL", example = "http://example.com/avatar.png")
        @JsonProperty("avatar")
        private String avatar;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Agent ")
    public static class UpdateReq implements Serializable {
        @Schema(description = "Agent ", example = "Updated Agent")
        @JsonProperty("title")
        private String title;

        @Schema(description = "DSL  ( JSON)")
        @JsonProperty("dsl")
        private Map<String, Object> dsl;

        @Schema(description = "")
        @JsonProperty("description")
        private String description;

        @Schema(description = " URL")
        @JsonProperty("avatar")
        private String avatar;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Agent ")
    public static class ListReq implements Serializable {
        @Schema(description = "", defaultValue = "1")
        @JsonProperty("page")
        @Builder.Default
        private Integer page = 1;

        @Schema(description = "", defaultValue = "10")
        @JsonProperty("page_size")
        @Builder.Default
        private Integer pageSize = 10;

        @Schema(description = "", defaultValue = "update_time")
        @JsonProperty("orderby")
        @Builder.Default
        private String orderby = "update_time";

        @Schema(description = "", defaultValue = "true")
        @JsonProperty("desc")
        @Builder.Default
        private Boolean desc = true;

        @Schema(description = "Agent ID ")
        @JsonProperty("id")
        private String id;

        @Schema(description = "")
        @JsonProperty("title")
        private String title;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Agent ")
    public static class AgentVO implements Serializable {
        @Schema(description = "Agent ID")
        @JsonProperty("id")
        private String id;

        @Schema(description = "")
        @JsonProperty("title")
        private String title;

        @Schema(description = "")
        @JsonProperty("description")
        private String description;

        @Schema(description = "")
        @JsonProperty("avatar")
        private String avatar;

        @Schema(description = "DSL ")
        @JsonProperty("dsl")
        private Map<String, Object> dsl;

        @Schema(description = " ID")
        @JsonProperty("user_id")
        private String userId;

        @Schema(description = "")
        @JsonProperty("canvas_category")
        private String canvasCategory;

        @Schema(description = " ()")
        @JsonProperty("create_time")
        private Long createTime;

        @Schema(description = " ()")
        @JsonProperty("update_time")
        private Long updateTime;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Webhook  ()")
    public static class WebhookTriggerReq implements Serializable {
        @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "")
        @JsonProperty("inputs")
        private Map<String, Object> inputs;

        @Schema(description = "", example = "Hello")
        @JsonProperty("query")
        private String query;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Webhook ")
    public static class WebhookTraceReq implements Serializable {
        @Schema(description = "", example = "1700000000.0")
        @JsonProperty("since_ts")
        private Double sinceTs;

        @Schema(description = "Webhook ID")
        @JsonProperty("webhook_id")
        private String webhookId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Webhook ")
    public static class WebhookTraceVO implements Serializable {
        @Schema(description = "Webhook ID")
        @JsonProperty("webhook_id")
        private String webhookId;

        @Schema(description = "")
        @JsonProperty("finished")
        private Boolean finished;

        @Schema(description = "")
        @JsonProperty("next_since_ts")
        private Double nextSinceTs;

        @Schema(description = "")
        @JsonProperty("events")
        private List<TraceEvent> events;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @Schema(description = "")
        public static class TraceEvent implements Serializable {
            @Schema(description = "")
            @JsonProperty("ts")
            private Double ts;

            @Schema(description = "")
            @JsonProperty("event")
            private String event;

            @Schema(description = "")
            @JsonProperty("data")
            private Object data;
        }
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Session ")
    public static class SessionCreateReq implements Serializable {
        @Schema(description = " ID")
        @JsonProperty("user_id")
        private String userId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Session ")
    public static class SessionListReq implements Serializable {
        @Schema(description = "", defaultValue = "1")
        @JsonProperty("page")
        @Builder.Default
        private Integer page = 1;

        @Schema(description = "", defaultValue = "10")
        @JsonProperty("page_size")
        @Builder.Default
        private Integer pageSize = 10;

        @Schema(description = "", defaultValue = "create_time")
        @JsonProperty("orderby")
        @Builder.Default
        private String orderby = "create_time";

        @Schema(description = "", defaultValue = "true")
        @JsonProperty("desc")
        @Builder.Default
        private Boolean desc = true;

        @Schema(description = "Session ID")
        @JsonProperty("id")
        private String id;

        @Schema(description = " ID")
        @JsonProperty("user_id")
        private String userId;

        @Schema(description = " DSL")
        @JsonProperty("dsl")
        @Builder.Default
        private Boolean dsl = false;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Session ")
    public static class SessionBatchDeleteReq implements Serializable {
        @Schema(description = " ID ", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("ids")
        @NotEmpty(message = "ID")
        private List<String> ids;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Session ")
    public static class SessionVO implements Serializable {
        @Schema(description = "Session ID")
        @JsonProperty("id")
        private String id;

        @Schema(description = "Agent ID")
        @JsonProperty("agent_id")
        private String agentId;

        @Schema(description = " ID")
        @JsonProperty("user_id")
        private String userId;

        @Schema(description = "")
        @JsonProperty("source")
        private String source;

        @Schema(description = "DSL ")
        @JsonProperty("dsl")
        private Map<String, Object> dsl;

        @Schema(description = "")
        @JsonProperty("messages")
        private List<Map<String, Object>> messages;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Completion ")
    public static class CompletionReq implements Serializable {
        @Schema(description = " ID", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = " ID ")
        @JsonProperty("session_id")
        private String sessionId;

        @Schema(description = "")
        @JsonProperty("question")
        private String question;

        @Schema(description = "", defaultValue = "true")
        @JsonProperty("stream")
        @Builder.Default
        private Boolean stream = true;

        @Schema(description = "", defaultValue = "false")
        @JsonProperty("return_trace")
        @Builder.Default
        private Boolean returnTrace = false;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Completion ")
    public static class CompletionVO implements Serializable {
        @Schema(description = " ID")
        @JsonProperty("id")
        private String id;

        @Schema(description = "")
        @JsonProperty("content")
        private String content;

        @Schema(description = "")
        @JsonProperty("reference")
        private Map<String, Object> reference;

        @Schema(description = "")
        @JsonProperty("trace")
        private List<Object> trace;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Dify ")
    public static class DifyRetrievalReq implements Serializable {
        @Schema(description = " ID")
        @JsonProperty("knowledge_id")
        private String knowledgeId;

        @Schema(description = "")
        @JsonProperty("query")
        private String query;

        @Schema(description = "")
        @JsonProperty("retrieval_setting")
        private Map<String, Object> retrievalSetting;

        @Schema(description = "")
        @JsonProperty("metadata_condition")
        private Map<String, Object> metadataCondition;

        @Schema(description = "")
        @JsonProperty("use_kg")
        private Boolean useKg;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Dify ")
    public static class DifyRetrievalVO implements Serializable {
        @Schema(description = "")
        @JsonProperty("records")
        private List<Record> records;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @Schema(description = "")
        public static class Record implements Serializable {
            @Schema(description = "")
            @JsonProperty("content")
            private String content;

            @Schema(description = "")
            @JsonProperty("score")
            private Double score;

            @Schema(description = "")
            @JsonProperty("title")
            private String title;

            @Schema(description = "")
            @JsonProperty("metadata")
            private Map<String, Object> metadata;
        }
    }
}
