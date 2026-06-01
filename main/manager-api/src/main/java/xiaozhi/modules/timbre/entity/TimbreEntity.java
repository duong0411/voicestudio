package xiaozhi.modules.timbre.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ai_tts_voice")
@Schema(description = "")
public class TimbreEntity {

    @Schema(description = "id")
    private String id;

    @Schema(description = "")
    private String languages;

    @Schema(description = "")
    private String name;

    @Schema(description = "")
    private String remark;

    @Schema(description = "")
    private String referenceAudio;

    @Schema(description = "")
    private String referenceText;

    @Schema(description = "")
    private long sort;

    @Schema(description = " TTS ")
    private String ttsModelId;

    @Schema(description = "")
    private String ttsVoice;

    @Schema(description = "")
    private String voiceDemo;

    @Schema(description = "")
    @TableField(fill = FieldFill.UPDATE)
    private Long updater;

    @Schema(description = "")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateDate;

    @Schema(description = "")
    @TableField(fill = FieldFill.INSERT)
    private Long creator;

    @Schema(description = "")
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;

}