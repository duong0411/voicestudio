package xiaozhi.modules.knowledge.dto.chat;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "")
public class ChatCompletionRequest implements Serializable {

    @Schema(description = " ( agent_id  bot_id)", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("model")
    private String model;

    @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("messages")
    private List<Message> messages;

    @Schema(description = "", defaultValue = "false")
    @JsonProperty("stream")
    private Boolean stream = false;

    @Schema(description = " (0-1)", defaultValue = "0.7")
    @JsonProperty("temperature")
    private Double temperature;

    @Schema(description = "Session ID (，)")
    @JsonProperty("session_id")
    private String sessionId;

    @Schema(description = "RAGFlow ()")
    private Map<String, Object> extra;

    @Data
    public static class Message implements Serializable {
        @Schema(description = " (system, user, assistant)", requiredMode = Schema.RequiredMode.REQUIRED)
        private String role;

        @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED)
        private String content;
    }
}
