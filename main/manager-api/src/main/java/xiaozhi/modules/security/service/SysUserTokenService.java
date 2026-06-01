package xiaozhi.modules.security.service;

import xiaozhi.common.page.TokenDTO;
import xiaozhi.common.service.BaseService;
import xiaozhi.common.utils.Result;
import xiaozhi.modules.security.entity.SysUserTokenEntity;
import xiaozhi.modules.sys.dto.PasswordDTO;
import xiaozhi.modules.sys.dto.SysUserDTO;


public interface SysUserTokenService extends BaseService<SysUserTokenEntity> {

    
    Result<TokenDTO> createToken(Long userId);

    SysUserDTO getUserByToken(String token);

    
    void logout(Long userId);

    
    void changePassword(Long userId, PasswordDTO passwordDTO);

}