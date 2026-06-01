package xiaozhi.modules.config.dto;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO")
public class AgentModelsDTO {

    @NotBlank(message = "MAC")
    @Schema(description = "MAC")
    private String macAddress;

    @NotBlank(message = "ID")
    @Schema(description = "ID")
    private String clientId;

    @NotNull(message = "")
    @Schema(description = "")
    private Map<String, String> selectedModule;
}