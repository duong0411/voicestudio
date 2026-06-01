package xiaozhi.modules.voiceclone.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ai_voice_clone")
@Schema(description = "")
public class VoiceCloneEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    @Schema(description = "")
    private String id;

    @Schema(description = "")
    private String name;

    @Schema(description = "id")
    private String modelId;

    @Schema(description = "id")
    private String voiceId;

    @Schema(description = "")
    private String languages;

    @Schema(description = " ID（）")
    private Long userId;

    @Schema(description = "")
    private byte[] voice;

    @Schema(description = "：0 1 2 3")
    private Integer trainStatus;

    @Schema(description = "")
    private String trainError;

    @Schema(description = "")
    @TableField(fill = FieldFill.INSERT)
    private Long creator;

    @Schema(description = "")
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;
}
