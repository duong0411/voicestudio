package xiaozhi.modules.knowledge.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@Schema(description = "")
@JsonIgnoreProperties(ignoreUnknown = true)
public class KnowledgeFilesDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Schema(description = "")
    private String id;

    @Schema(description = "ID")
    private String documentId;

    @Schema(description = "ID")
    private String datasetId;

    @Schema(description = "")
    private String name;

    @Schema(description = "")
    private String fileType;

    @Schema(description = "（）")
    private Long fileSize;

    @Schema(description = "")
    private String filePath;

    @Schema(description = " (0.0 ~ 1.0)")
    private Double progress;

    @Schema(description = " (Base64  URL)")
    private String thumbnail;

    @Schema(description = " (: )")
    private Double processDuration;

    @Schema(description = " (local, s3, url )")
    private String sourceType;

    @Schema(description = " (Map )")
    private Map<String, Object> metaFields;

    @Schema(description = "")
    private String chunkMethod;

    @Schema(description = "")
    private Map<String, Object> parserConfig;

    @Schema(description = " (1: /, 0: /)")
    private String status;

    @Schema(description = " (UNSTART/RUNNING/CANCEL/DONE/FAIL)")
    private String run;

    @Schema(description = "")
    private Long creator;

    @Schema(description = "")
    private Date createdAt;

    @Schema(description = "")
    private Long updater;

    @Schema(description = "")
    private Date updatedAt;

    @Schema(description = "")
    private Integer chunkCount;

    @Schema(description = "Token")
    private Long tokenCount;

    @Schema(description = "")
    private String error;


    private static final Integer STATUS_UNSTART = 0;
    private static final Integer STATUS_RUNNING = 1;
    private static final Integer STATUS_CANCEL = 2;
    private static final Integer STATUS_DONE = 3;
    private static final Integer STATUS_FAIL = 4;

    
    public Integer getParseStatusCode() {
        if (run == null) {
            return STATUS_UNSTART;
        }


        switch (run.toUpperCase()) {
            case "RUNNING":
                return STATUS_RUNNING;
            case "CANCEL":
                return STATUS_CANCEL;
            case "DONE":
                return STATUS_DONE;
            case "FAIL":
                return STATUS_FAIL;
            case "UNSTART":
            default:
                return STATUS_UNSTART;
        }
    }

}