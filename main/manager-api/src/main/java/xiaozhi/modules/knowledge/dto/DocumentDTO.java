package xiaozhi.modules.knowledge.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "")
public class DocumentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private String id;

    @Schema(description = "ID")
    private String datasetId;

    @Schema(description = "RAGFlowID")
    private String documentId;

    @Schema(description = "")
    private String name;

    @Schema(description = "")
    private Long size;

    @Schema(description = "")
    private String type;

    @Schema(description = "")
    private String chunkMethod;

    @Schema(description = "")
    private Map<String, Object> parserConfig;

    @Schema(description = " (1: 3: 4:)")
    private Integer status;

    @Schema(description = "")
    private String error;

    @Schema(description = "")
    private Integer chunkCount;

    @Schema(description = "Token")
    private Long tokenCount;

    @Schema(description = "")
    private Integer enabled;

    @Schema(description = "")
    private Date createdAt;

    @Schema(description = "")
    private Date updatedAt;

    @Schema(description = " ()")
    private Double progress;

    @Schema(description = "/ ()")
    private String thumbnail;
}
