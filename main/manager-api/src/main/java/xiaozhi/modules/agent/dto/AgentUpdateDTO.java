package xiaozhi.modules.agent.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "")
public class AgentUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "", example = "AGT_1234567890", nullable = true)
    private String agentCode;

    @Schema(description = "", example = "", nullable = true)
    private String agentName;

    @Schema(description = "", example = "asr_model_02", nullable = true)
    private String asrModelId;

    @Schema(description = "", example = "vad_model_02", nullable = true)
    private String vadModelId;

    @Schema(description = "", example = "llm_model_02", nullable = true)
    private String llmModelId;

    @Schema(description = "", example = "slm_model_02", nullable = true)
    private String slmModelId;

    @Schema(description = "VLLM", example = "vllm_model_02", required = false)
    private String vllmModelId;

    @Schema(description = "", example = "tts_model_02", required = false)
    private String ttsModelId;

    @Schema(description = "", example = "voice_02", nullable = true)
    private String ttsVoiceId;

    @Schema(description = "", example = "", nullable = true)
    private String ttsLanguage;

    @Schema(description = "TTS", example = "50", nullable = true)
    private Integer ttsVolume;

    @Schema(description = "TTS", example = "50", nullable = true)
    private Integer ttsRate;

    @Schema(description = "TTS", example = "50", nullable = true)
    private Integer ttsPitch;

    @Schema(description = "", example = "mem_model_02", nullable = true)
    private String memModelId;

    @Schema(description = "", example = "intent_model_02", nullable = true)
    private String intentModelId;

    @Schema(description = "", nullable = true)
    private List<FunctionInfo> functions;

    @Schema(description = "", example = "，", nullable = true)
    private String systemPrompt;

    @Schema(description = "", example = "，，\n"
            + "，user，", nullable = true)
    private String summaryMemory;

    @Schema(description = "（0 1 2）", example = "3", nullable = true)
    private Integer chatHistoryConf;

    @Schema(description = "", example = "zh_CN", nullable = true)
    private String langCode;

    @Schema(description = "", example = "", nullable = true)
    private String language;

    @Schema(description = "", example = "1", nullable = true)
    private Integer sort;

    @Schema(description = "", nullable = true)
    private List<ContextProviderDTO> contextProviders;

    @Schema(description = "ID", nullable = true)
    private List<String> correctWordFileIds;

    @Data
    @Schema(description = "")
    public static class FunctionInfo implements Serializable {
        @Schema(description = "ID", example = "plugin_01")
        private String pluginId;

        @Schema(description = "", nullable = true)
        private HashMap<String, Object> paramInfo;

        private static final long serialVersionUID = 1L;
    }
}