package xiaozhi.modules.security.service;

import xiaozhi.modules.security.entity.SysUserTokenEntity;
import xiaozhi.modules.sys.entity.SysUserEntity;


public interface ShiroService {

    SysUserTokenEntity getByToken(String token);

    
    SysUserEntity getUser(Long userId);

}
