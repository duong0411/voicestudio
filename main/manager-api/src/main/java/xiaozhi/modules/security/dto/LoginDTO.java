package xiaozhi.modules.security.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@Schema(description = "")
public class LoginDTO implements Serializable {

    @Schema(description = "")
    @NotBlank(message = "{sysuser.username.require}")
    private String username;

    @Schema(description = "")
    @NotBlank(message = "{sysuser.password.require}")
    private String password;

    @Schema(description = "")
    private String mobileCaptcha;

    @Schema(description = "")
    private String captchaId;

}