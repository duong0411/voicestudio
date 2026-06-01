package xiaozhi.modules.knowledge.dto.file;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;


@Schema(description = " DTO")
public class FileDTO {



    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class UploadReq implements Serializable {

        @NotNull(message = "")
        @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED)
        private MultipartFile file;

        @Schema(description = " ID ()", example = "folder_001")
        @JsonProperty("parent_id")
        private String parentId;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class CreateReq implements Serializable {

        @NotBlank(message = "")
        @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
        private String name;

        @Schema(description = " ID ()", example = "folder_001")
        @JsonProperty("parent_id")
        private String parentId;

        @NotBlank(message = "")
        @Schema(description = ": FOLDER", requiredMode = Schema.RequiredMode.REQUIRED, example = "FOLDER")
        @Builder.Default
        private String type = "FOLDER";
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class RenameReq implements Serializable {

        @NotBlank(message = " ID ")
        @Schema(description = "/ ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "file_001")
        @JsonProperty("file_id")
        private String fileId;

        @NotBlank(message = "")
        @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
        private String name;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class MoveReq implements Serializable {

        @NotEmpty(message = " ID ")
        @Schema(description = "/ ID ", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"file_001\", \"file_002\"]")
        @JsonProperty("src_file_ids")
        private List<String> srcFileIds;

        @NotBlank(message = " ID ")
        @Schema(description = " ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "folder_002")
        @JsonProperty("dest_file_id")
        private String destFileId;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class RemoveReq implements Serializable {

        @NotEmpty(message = " ID ")
        @Schema(description = "/ ID ", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"file_001\", \"file_002\"]")
        @JsonProperty("file_ids")
        private List<String> fileIds;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class ConvertReq implements Serializable {

        @NotEmpty(message = " ID ")
        @Schema(description = " ID ", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"file_001\", \"file_002\"]")
        @JsonProperty("file_ids")
        private List<String> fileIds;

        @NotEmpty(message = " ID ")
        @Schema(description = " ID ", requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"kb_001\"]")
        @JsonProperty("kb_ids")
        private List<String> kbIds;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class ListReq implements Serializable {

        @Schema(description = " ID ()", example = "folder_001")
        @JsonProperty("parent_id")
        private String parentId;

        @Schema(description = "", example = "")
        private String keywords;

        @Schema(description = " ( 1 )", example = "1")
        private Integer page;

        @Schema(description = "", example = "30")
        @JsonProperty("page_size")
        private Integer pageSize;

        @Schema(description = ": create_time / update_time / name / size", example = "create_time")
        private String orderby;

        @Schema(description = "", example = "true")
        private Boolean desc;
    }



    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "/")
    public static class InfoVO implements Serializable {

        @Schema(description = "/ ID", example = "file_001")
        private String id;

        @Schema(description = " ID", example = "folder_001")
        @JsonProperty("parent_id")
        private String parentId;

        @Schema(description = " ID", example = "tenant_001")
        @JsonProperty("tenant_id")
        private String tenantId;

        @Schema(description = " ID", example = "user_001")
        @JsonProperty("created_by")
        private String createdBy;

        @Schema(description = ": FOLDER / FILE", example = "FOLDER")
        private String type;

        @Schema(description = "", example = "")
        private String name;

        @Schema(description = "", example = "/root/folder")
        private String location;

        @Schema(description = " ()", example = "1024")
        private Long size;

        @Schema(description = "", example = "local")
        @JsonProperty("source_type")
        private String sourceType;

        @Schema(description = " ()", example = "1700000000000")
        @JsonProperty("create_time")
        private Long createTime;

        @Schema(description = " ()", example = "2024-01-15 10:30:00")
        @JsonProperty("create_date")
        private String createDate;

        @Schema(description = " ()", example = "1700000001000")
        @JsonProperty("update_time")
        private Long updateTime;

        @Schema(description = " ()", example = "2024-01-15 11:00:00")
        @JsonProperty("update_date")
        private String updateDate;

        @Schema(description = "", example = "pdf")
        private String extension;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class ListVO implements Serializable {

        @Schema(description = "", example = "100")
        private Long total;

        @Schema(description = "")
        @JsonProperty("parent_folder")
        private InfoVO parentFolder;

        @Schema(description = "/")
        private List<InfoVO> files;

        @Schema(description = "")
        private List<InfoVO> breadcrumb;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class ConvertVO implements Serializable {

        @Schema(description = " ID", example = "convert_001")
        private String id;

        @Schema(description = " ID", example = "file_001")
        @JsonProperty("file_id")
        private String fileId;

        @Schema(description = " ID", example = "doc_001")
        @JsonProperty("document_id")
        private String documentId;

        @Schema(description = " ()", example = "1700000000000")
        @JsonProperty("create_time")
        private Long createTime;

        @Schema(description = " ()", example = "2024-01-15 10:30:00")
        @JsonProperty("create_date")
        private String createDate;

        @Schema(description = " ()", example = "1700000001000")
        @JsonProperty("update_time")
        private Long updateTime;

        @Schema(description = " ()", example = "2024-01-15 11:00:00")
        @JsonProperty("update_date")
        private String updateDate;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class ConvertStatusVO implements Serializable {

        @Schema(description = ": pending / processing / completed / failed", example = "completed")
        private String status;

        @Schema(description = " (0.0 - 1.0)", example = "1.0")
        private Float progress;

        @Schema(description = "", example = "")
        private String message;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = " ()")
    public static class BreadcrumbVO implements Serializable {

        @Schema(description = " ()")
        @JsonProperty("parent_folders")
        private List<InfoVO> parentFolders;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class RootFolderVO implements Serializable {

        @Schema(description = "")
        @JsonProperty("root_folder")
        private InfoVO rootFolder;
    }

    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "")
    public static class ParentFolderVO implements Serializable {

        @Schema(description = "")
        @JsonProperty("parent_folder")
        private InfoVO parentFolder;
    }
}
