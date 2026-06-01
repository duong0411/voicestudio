package xiaozhi.modules.knowledge.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "")
public class KnowledgeBaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "")
    private String id;

    @Schema(description = "ID")
    private String datasetId;

    @Schema(description = "RAGID")
    private String ragModelId;

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

    @Schema(description = "Token")
    private Long tokenNum;

    @Schema(description = "(0: 1:)")
    private Integer status;

    @Schema(description = "")
    private Long creator;

    @Schema(description = "")
    private Date createdAt;

    @Schema(description = "")
    private Long updater;

    @Schema(description = "")
    private Date updatedAt;

    @Schema(description = "")
    private Integer documentCount;
}