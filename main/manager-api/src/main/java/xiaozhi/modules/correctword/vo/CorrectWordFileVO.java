package xiaozhi.modules.correctword.vo;

import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "VO")
public class CorrectWordFileVO {

    @Schema(description = "ID")
    private String id;

    @Schema(description = "")
    private String fileName;

    @Schema(description = "")
    private Integer wordCount;

    @Schema(description = "，")
    private List<String> content;

    @Schema(description = "")
    private Date createdAt;

    @Schema(description = "")
    private Date updatedAt;
}
