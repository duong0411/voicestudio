package xiaozhi.modules.knowledge.dto.common;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

@Schema(description = " DTO")
public class CommonDTO {



    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "")
    public static class ReferenceDetailReq implements Serializable {
        @Schema(description = " ID", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = " ID ")
        @JsonProperty("chunk_id")
        private String chunkId;

        @Schema(description = " ID")
        @JsonProperty("knowledge_id")
        private String knowledgeId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "")
    public static class ReferenceDetailVO implements Serializable {
        @Schema(description = " ID")
        @JsonProperty("chunk_id")
        private String chunkId;

        @Schema(description = "")
        @JsonProperty("content_with_weight")
        private String contentWithWeight;

        @Schema(description = "")
        @JsonProperty("doc_name")
        private String docName;

        @Schema(description = " ID ")
        @JsonProperty("img_id")
        private String imageId;

        @Schema(description = " ID")
        @JsonProperty("doc_id")
        private String docId;
    }



    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = " ()")
    public static class AskAboutReq implements Serializable {
        @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED, example = "What is this dataset about?")
        @NotBlank(message = "")
        @JsonProperty("question")
        private String question;

        @Schema(description = " ID ", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "")
        @JsonProperty("dataset_ids")
        private List<String> datasetIds;
    }


}
