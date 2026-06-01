package xiaozhi.modules.security.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@Schema(description = "")
public class SmsVerificationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "")
    @NotBlank(message = "{sysuser.username.require}")
    private String phone;

    @Schema(description = "")
    private String captcha;

    @Schema(description = "")
    private String captchaId;
}