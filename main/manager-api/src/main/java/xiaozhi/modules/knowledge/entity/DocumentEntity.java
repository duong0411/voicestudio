package xiaozhi.modules.knowledge.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@TableName(value = "ai_rag_knowledge_document", autoResultMap = true)
@Schema(description = "")
public class DocumentEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    @Schema(description = "ID")
    private String id;

    @Schema(description = "ID ( ai_rag_dataset.dataset_id)")
    private String datasetId;

    @Schema(description = "RAGFlowID (ID)")
    private String documentId;

    @Schema(description = "")
    private String name;

    @Schema(description = "(Bytes)")
    private Long size;

    @Schema(description = "(pdf/doc/txt)")
    private String type;

    @Schema(description = "")
    private String chunkMethod;

    @Schema(description = "(JSON String)")
    private String parserConfig;

    @Schema(description = " (1: /, 0: /)")
    private String status;

    @Schema(description = " (UNSTART/RUNNING/CANCEL/DONE/FAIL)")
    private String run;

    @Schema(description = " (0.0 ~ 1.0)")
    private Double progress;

    @Schema(description = " (Base64  URL)")
    private String thumbnail;

    @Schema(description = " (: )")
    private Double processDuration;

    @Schema(description = " (JSON )")
    private String metaFields;

    @Schema(description = " (local, s3, url )")
    private String sourceType;

    @Schema(description = "")
    private String error;

    @Schema(description = "")
    private Integer chunkCount;

    @Schema(description = "Token")
    private Long tokenCount;

    @Schema(description = " (0: 1:)")
    private Integer enabled;

    @Schema(description = "")
    @TableField(fill = FieldFill.INSERT)
    private Long creator;

    @Schema(description = "")
    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;

    @Schema(description = "")
    @TableField(fill = FieldFill.UPDATE)
    private Date updatedAt;

    @Schema(description = "")
    private Date lastSyncAt;
}
