package xiaozhi.modules.agent.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("ai_agent")
@Schema(description = "")
public class AgentEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    @Schema(description = "")
    private String id;

    @Schema(description = "ID")
    private Long userId;

    @Schema(description = "")
    private String agentCode;

    @Schema(description = "")
    private String agentName;

    @Schema(description = "")
    private String asrModelId;

    @Schema(description = "")
    private String vadModelId;

    @Schema(description = "")
    private String llmModelId;

    @Schema(description = "")
    private String slmModelId;

    @Schema(description = "VLLM")
    private String vllmModelId;

    @Schema(description = "")
    private String ttsModelId;

    @Schema(description = "")
    private String ttsVoiceId;

    @Schema(description = "")
    private String ttsLanguage;

    @Schema(description = "TTS")
    private Integer ttsVolume;

    @Schema(description = "TTS")
    private Integer ttsRate;

    @Schema(description = "TTS")
    private Integer ttsPitch;

    @Schema(description = "")
    private String memModelId;

    @Schema(description = "")
    private String intentModelId;

    @Schema(description = "（0 1 2）")
    private Integer chatHistoryConf;

    @Schema(description = "")
    private String systemPrompt;

    @Schema(description = "", example = "，，\n" +
            "，user，", required = false)
    private String summaryMemory;

    @Schema(description = "")
    private String langCode;

    @Schema(description = "")
    private String language;

    @Schema(description = "")
    private Integer sort;

    @Schema(description = "")
    private Long creator;

    @Schema(description = "")
    private Date createdAt;

    @Schema(description = "")
    private Long updater;

    @Schema(description = "")
    private Date updatedAt;
}