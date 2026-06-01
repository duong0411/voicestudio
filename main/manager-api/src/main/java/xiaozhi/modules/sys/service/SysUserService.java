package xiaozhi.modules.sys.service;

import xiaozhi.common.page.PageData;
import xiaozhi.common.service.BaseService;
import xiaozhi.modules.sys.dto.AdminPageUserDTO;
import xiaozhi.modules.sys.dto.PasswordDTO;
import xiaozhi.modules.sys.dto.SysUserDTO;
import xiaozhi.modules.sys.entity.SysUserEntity;
import xiaozhi.modules.sys.vo.AdminPageUserVO;


public interface SysUserService extends BaseService<SysUserEntity> {

    SysUserDTO getByUsername(String username);

    SysUserDTO getByUserId(Long userId);

    void save(SysUserDTO dto);

    
    void deleteById(Long ids);

    
    void changePassword(Long userId, PasswordDTO passwordDTO);

    
    void changePasswordDirectly(Long userId, String password);

    
    String resetPassword(Long userId);

    
    PageData<AdminPageUserVO> page(AdminPageUserDTO dto);

    
    void changeStatus(Integer status, String[] userIds);

    
    boolean getAllowUserRegister();
}
