package xiaozhi.modules.knowledge.dto.bot;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

@Schema(description = " (Bot)  DTO")
public class BotDTO {




    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "SearchBot ")
    public static class SearchAskReq implements Serializable {
        @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED, example = "What is RAG?")
        @NotBlank(message = "")
        @JsonProperty("question")
        private String question;

        @Schema(description = "", defaultValue = "false")
        @JsonProperty("quote")
        @Builder.Default
        private Boolean quote = false;

        @Schema(description = "", defaultValue = "true")
        @JsonProperty("stream")
        @Builder.Default
        private Boolean stream = true;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "SearchBot ")
    public static class SearchAskVO implements Serializable {
        @Schema(description = "")
        @JsonProperty("answer")
        private String answer;

        @Schema(description = " (Value  RetrievalDTO.HitVO)")
        @JsonProperty("reference")
        private Map<String, Object> reference;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "")
    public static class RelatedQuestionReq implements Serializable {
        @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "")
        @JsonProperty("question")
        private String question;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "")
    public static class MindMapReq implements Serializable {
        @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "")
        @JsonProperty("question")
        private String question;
    }




    @Data
    @Builder
    @AllArgsConstructor
    @Schema(description = "AgentBot ")
    public static class AgentInputsReq implements Serializable {
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "AgentBot ")
    public static class AgentInputsVO implements Serializable {
        @Schema(description = "")
        @JsonProperty("variables")
        private List<Map<String, Object>> variables;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "AgentBot ")
    public static class AgentCompletionReq implements Serializable {
        @Schema(description = "")
        @JsonProperty("inputs")
        private Map<String, Object> inputs;

        @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "")
        @JsonProperty("question")
        private String question;

        @Schema(description = "", defaultValue = "true")
        @JsonProperty("stream")
        @Builder.Default
        private Boolean stream = true;

        @Schema(description = " ID")
        @JsonProperty("session_id")
        private String sessionId;
    }
}
