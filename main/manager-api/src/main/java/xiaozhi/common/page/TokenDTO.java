package xiaozhi.common.page;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "")
public class TokenDTO implements Serializable {

    @Schema(description = "")
    private String token;

    @Schema(description = "")
    private int expire;

    @Schema(description = "")
    private String clientHash;
}
