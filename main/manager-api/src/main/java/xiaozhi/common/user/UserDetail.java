package xiaozhi.common.user;

import java.io.Serializable;

import lombok.Data;


@Data
public class UserDetail implements Serializable {
    private Long id;
    private String username;
    private Integer superAdmin;
    private String token;
    private Integer status;
}