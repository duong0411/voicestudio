package xiaozhi.modules.knowledge.dto.document;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;


@Schema(description = " DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentDTO {

    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UploadReq implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = " ID ()", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("dataset_id")
        @NotBlank(message = "ID")
        private String datasetId;

        @Schema(description = " (，)")
        private String name;

        @Schema(description = "")
        @JsonProperty("chunk_method")
        private DocumentDTO.InfoVO.ChunkMethod chunkMethod;

        @Schema(description = "")
        @JsonProperty("parser_config")
        private DocumentDTO.InfoVO.ParserConfig parserConfig;

        @Schema(description = " ( /)")
        @JsonProperty("parent_path")
        private String parentPath;

        @Schema(description = "")
        @JsonProperty("meta")
        private Map<String, Object> metaFields;

        @Schema(description = " ( PDF, DOCX, TXT, MD )", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "")
        private org.springframework.web.multipart.MultipartFile file;
    }

    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdateReq implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = " (，)")
        private String name;

        @Schema(description = "/ (true: , false: ; )")
        private Boolean enabled;

        @Schema(description = " ()")
        @JsonProperty("chunk_method")
        private InfoVO.ChunkMethod chunkMethod;

        @Schema(description = " ( chunk_method )")
        @JsonProperty("parser_config")
        private InfoVO.ParserConfig parserConfig;
    }

    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListReq implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = " (: 1)")
        private Integer page;

        @Schema(description = " (: 30)")
        @JsonProperty("page_size")
        private Integer pageSize;

        @Schema(description = " (: create_time, name, size; : create_time)")
        private String orderby;

        @Schema(description = " (true: /; false: /; : true)")
        private Boolean desc;

        @Schema(description = ":  ID")
        private String id;

        @Schema(description = ":  ()")
        private String name;

        @Schema(description = ": ")
        private String keywords;

        @Schema(description = ":  ( ['pdf', 'docx'])")
        private List<String> suffix;

        @Schema(description = ": ")
        private List<InfoVO.RunStatus> run;

        @Schema(description = ":  (, )")
        @JsonProperty("create_time_from")
        private Long createTimeFrom;

        @Schema(description = ":  (, )")
        @JsonProperty("create_time_to")
        private Long createTimeTo;
    }

    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatchIdReq implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = " ID ", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("ids")
        @JsonAlias("document_ids")
        @NotEmpty(message = "ID")
        private List<String> ids;
    }

    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InfoVO implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = " ID ()", requiredMode = Schema.RequiredMode.REQUIRED)
        private String id;

        @Schema(description = " URL (Base64  )")
        private String thumbnail;

        @Schema(description = " ID", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("dataset_id")
        private String datasetId;

        @Schema(description = " ()")
        @JsonProperty("chunk_method")
        private ChunkMethod chunkMethod;

        @Schema(description = " ETL Pipeline ID ()")
        @JsonProperty("pipeline_id")
        private String pipelineId;

        @Schema(description = "")
        @JsonProperty("parser_config")
        private ParserConfig parserConfig;

        @Schema(description = " ( local, s3, url )")
        @JsonProperty("source_type")
        private String sourceType;

        @Schema(description = " ( pdf, docx, txt)", requiredMode = Schema.RequiredMode.REQUIRED)
        private String type;

        @Schema(description = " ID")
        @JsonProperty("created_by")
        private String createdBy;

        @Schema(description = " ()", requiredMode = Schema.RequiredMode.REQUIRED)
        private String name;

        @Schema(description = "")
        private String location;

        @Schema(description = " (: Bytes)")
        private Long size;

        @Schema(description = " Token  ()")
        @JsonProperty("token_count")
        private Long tokenCount;

        @Schema(description = " (Chunk) ")
        @JsonProperty("chunk_count")
        private Long chunkCount;

        @Schema(description = " (0.0 ~ 1.0, 1.0 )")
        private Double progress;

        @Schema(description = "")
        @JsonProperty("progress_msg")
        private String progressMsg;

        @Schema(description = " (RAGFlowRFC1123)")
        @JsonProperty("process_begin_at")
        private String processBeginAt;

        @Schema(description = " (: )")
        @JsonProperty("process_duration")
        private Double processDuration;

        @Schema(description = " (Key-Value )")
        @JsonProperty("meta_fields")
        private Map<String, Object> metaFields;

        @Schema(description = " ()")
        private String suffix;

        @Schema(description = "")
        private RunStatus run;

        @Schema(description = " (1: /, 0: /)", requiredMode = Schema.RequiredMode.REQUIRED)
        private String status;

        @Schema(description = " (, )", requiredMode = Schema.RequiredMode.REQUIRED)
        @JsonProperty("create_time")
        private Long createTime;

        @Schema(description = " (RAGFlowRFC1123)")
        @JsonProperty("create_date")
        private String createDate;

        @Schema(description = " (, )")
        @JsonProperty("update_time")
        private Long updateTime;

        @Schema(description = " (RAGFlowRFC1123)")
        @JsonProperty("update_date")
        private String updateDate;

        
        public enum ChunkMethod {
            @Schema(description = ": ")
            @JsonProperty("naive")
            NAIVE,
            @Schema(description = ": ")
            @JsonProperty("manual")
            MANUAL,
            @Schema(description = ":  Q&A ")
            @JsonProperty("qa")
            QA,
            @Schema(description = ":  Excel  CSV ")
            @JsonProperty("table")
            TABLE,
            @Schema(description = ": ")
            @JsonProperty("paper")
            PAPER,
            @Schema(description = ": ")
            @JsonProperty("book")
            BOOK,
            @Schema(description = ": ")
            @JsonProperty("laws")
            LAWS,
            @Schema(description = ":  PPT ")
            @JsonProperty("presentation")
            PRESENTATION,
            @Schema(description = ":  OCR ")
            @JsonProperty("picture")
            PICTURE,
            @Schema(description = ": ")
            @JsonProperty("one")
            ONE,
            @Schema(description = ": ")
            @JsonProperty("knowledge_graph")
            KNOWLEDGE_GRAPH,
            @Schema(description = ": ")
            @JsonProperty("email")
            EMAIL;
        }

        
        public enum RunStatus {
            @Schema(description = ": ")
            @JsonProperty("UNSTART")
            UNSTART,
            @Schema(description = ": ")
            @JsonProperty("RUNNING")
            RUNNING,
            @Schema(description = ": ")
            @JsonProperty("CANCEL")
            CANCEL,
            @Schema(description = ": ")
            @JsonProperty("DONE")
            DONE,
            @Schema(description = ": ")
            @JsonProperty("FAIL")
            FAIL;
        }

        
        public enum LayoutRecognize {
            @Schema(description = ": ")
            @JsonProperty("DeepDOC")
            DeepDOC,
            @Schema(description = ": ")
            @JsonProperty("Simple")
            Simple;
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @Schema(description = "")
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ParserConfig implements Serializable {
            private static final long serialVersionUID = 1L;

            @Schema(description = " Token  (: 512, 1024, 2048)")
            @JsonProperty("chunk_token_num")
            private Integer chunkTokenNum;

            @Schema(description = " (,  \\n)")
            private String delimiter;

            @Schema(description = " (DeepDOC/Simple)")
            @JsonProperty("layout_recognize")
            private LayoutRecognize layoutRecognize;

            @Schema(description = " Excel  HTML ")
            @JsonProperty("html4excel")
            private Boolean html4excel;

            @Schema(description = " (0 )")
            @JsonProperty("auto_keywords")
            private Integer autoKeywords;

            @Schema(description = " (0 )")
            @JsonProperty("auto_questions")
            private Integer autoQuestions;

            @Schema(description = "")
            @JsonProperty("topn_tags")
            private Integer topnTags;

            @Schema(description = "RAPTOR ")
            private RaptorConfig raptor;

            @Schema(description = "GraphRAG ")
            @JsonProperty("graphrag")
            private GraphRagConfig graphRag;

            @Data
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            @Schema(description = "RAPTOR () ")
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class RaptorConfig implements Serializable {
                private static final long serialVersionUID = 1L;
                @Schema(description = " RAPTOR ")
                @JsonProperty("use_raptor")
                private Boolean useRaptor;
            }

            @Data
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            @Schema(description = "GraphRAG () ")
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class GraphRagConfig implements Serializable {
                private static final long serialVersionUID = 1L;
                @Schema(description = " GraphRAG ")
                @JsonProperty("use_graphrag")
                private Boolean useGraphRag;
            }
        }
    }
}
