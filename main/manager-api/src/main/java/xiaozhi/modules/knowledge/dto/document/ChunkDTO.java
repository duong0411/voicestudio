package xiaozhi.modules.knowledge.dto.document;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;


@Schema(description = " DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChunkDTO {

    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AddReq implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "")
        private String content;

        @Schema(description = "")
        @JsonProperty("important_keywords")
        private List<String> importantKeywords;

        @Schema(description = "")
        private List<String> questions;
    }

    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdateReq implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "")
        private String content;

        @Schema(description = " ()")
        @JsonProperty("important_keywords")
        private List<String> importantKeywords;

        @Schema(description = "/ (true: , false: )")
        private Boolean available;
    }

    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListReq implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = " ( 1)")
        private Integer page;

        @Schema(description = " ( 30)")
        @JsonProperty("page_size")
        private Integer pageSize;

        @Schema(description = " ()")
        private String keywords;

        @Schema(description = " ID")
        private String id;
    }

    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RemoveReq implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = " ID ", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("chunk_ids")
        @NotEmpty(message = "ID")
        private List<String> chunkIds;
    }

    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InfoVO implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = " ID ( document_id + )", requiredMode = Schema.RequiredMode.REQUIRED)
        private String id;

        @Schema(description = " ()", requiredMode = Schema.RequiredMode.REQUIRED)
        private String content;

        @Schema(description = " ID", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("document_id")
        private String documentId;

        @Schema(description = " / ")
        @JsonProperty("docnm_kwd")
        private String docnmKwd;

        @Schema(description = " ()")
        @JsonProperty("important_keywords")
        private List<String> importantKeywords;

        @Schema(description = " ( Q&A )")
        private List<String> questions;

        @Schema(description = " ID")
        @JsonProperty("image_id")
        private String imageId;

        @Schema(description = " ID")
        @JsonProperty("dataset_id")
        private String datasetId;

        @Schema(description = " (true: , false: )")
        private Boolean available;

        @Schema(description = " (RAGFlow,  [[start, end, filename]])")
        private List<List<Object>> positions;

        @Schema(description = "Token ID ")
        @JsonProperty("token")
        private List<Integer> token;
    }

    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListVO implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "")
        private List<InfoVO> chunks;

        @Schema(description = "")
        private DocumentDTO.InfoVO doc;

        @Schema(description = "")
        private Long total;
    }
}
