package xiaozhi.modules.sys.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@Schema(description = "")
public class PasswordDTO implements Serializable {

    @Schema(description = "")
    @NotBlank(message = "{sysuser.password.require}")
    private String password;

    @Schema(description = "")
    @NotBlank(message = "{sysuser.password.require}")
    private String newPassword;

}