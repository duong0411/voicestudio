package xiaozhi.modules.sys.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;


@Data
@Schema(description = "")
public class RetrievePasswordDTO implements Serializable {

    @Schema(description = "")
    @NotBlank(message = "{sysuser.password.require}")
    private String phone;

    @Schema(description = "")
    @NotBlank(message = "{sysuser.password.require}")
    private String code;

    @Schema(description = "")
    @NotBlank(message = "{sysuser.password.require}")
    private String password;

    @Schema(description = "ID")
    private String captchaId;



}