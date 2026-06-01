package xiaozhi.modules.knowledge.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName(value = "ai_rag_dataset", autoResultMap = true)
@Schema(description = "")
public class KnowledgeBaseEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    @Schema(description = "")
    private String id;

    @Schema(description = "ID")
    private String datasetId;

//    @Deprecated
    @Schema(description = "RAGID (RAGFlow)")
    private String ragModelId;

    @Schema(description = "ID")
    private String tenantId;

    @Schema(description = "")
    private String name;

    @Schema(description = "(Base64)")
    private String avatar;

    @Schema(description = "")
    private String description;

    @Schema(description = "")
    private String embeddingModel;

    @Schema(description = ": me/team")
    private String permission;

    @Schema(description = "")
    private String chunkMethod;

    @Schema(description = "(JSON String)")
    private String parserConfig;

    @Schema(description = "")
    private Long chunkCount;

    @Schema(description = "")
    private Long documentCount;

    @Schema(description = "Token")
    private Long tokenNum;

    @Schema(description = "(0: 1:)")
    private Integer status;

    @Schema(description = "")
    @TableField(fill = FieldFill.INSERT)
    private Long creator;

    @Schema(description = "")
    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;

    @Schema(description = "")
    @TableField(fill = FieldFill.UPDATE)
    private Long updater;

    @Schema(description = "")
    @TableField(fill = FieldFill.UPDATE)
    private Date updatedAt;
}