package xiaozhi.modules.agent.dto;

import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import xiaozhi.modules.agent.dto.AgentTagDTO;


@Data
@Schema(description = "")
public class AgentDTO {
    @Schema(description = "", example = "AGT_1234567890")
    private String id;

    @Schema(description = "", example = "")
    private String agentName;

    @Schema(description = "", example = "tts_model_01")
    private String ttsModelName;

    @Schema(description = "", example = "voice_01")
    private String ttsVoiceName;

    @Schema(description = "", example = "llm_model_01")
    private String llmModelName;

    @Schema(description = "", example = "vllm_model_01")
    private String vllmModelName;

    @Schema(description = "ID", example = "mem_model_01")
    private String memModelId;

    @Schema(description = "", example = "，")
    private String systemPrompt;

    @Schema(description = "", example = "，，\n" +
            "，user，", required = false)
    private String summaryMemory;

    @Schema(description = "", example = "2024-03-20 10:00:00")
    private Date lastConnectedAt;

    @Schema(description = "", example = "10")
    private Integer deviceCount;

    @Schema(description = "")
    private List<AgentTagDTO> tags;
}