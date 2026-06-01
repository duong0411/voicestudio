package xiaozhi.modules.correctword.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Schema(description = "DTO")
public class CorrectWordFileCreateDTO {

    @NotBlank(message = "")
    @Schema(description = "")
    private String fileName;

    @NotEmpty(message = "")
    @Schema(description = "，：|")
    private List<String> content;

    @Schema(description = "（），1MB")
    private Long fileSize;
}
