package xiaozhi.modules.model.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("ai_model_provider")
@Schema(description = "")
public class ModelProviderEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    @Schema(description = "")
    private String id;

    @Schema(description = "(Memory/ASR/VAD/LLM/TTS)")
    private String modelType;

    @Schema(description = "， openai、")
    private String providerCode;

    @Schema(description = "")
    private String name;

    @Schema(description = "(JSON)")
    private String fields;

    @Schema(description = "")
    private Integer sort;

    @Schema(description = "")
    private Long creator;

    @Schema(description = "")
    private Date createDate;

    @Schema(description = "")
    private Long updater;

    @Schema(description = "")
    private Date updateDate;
}
