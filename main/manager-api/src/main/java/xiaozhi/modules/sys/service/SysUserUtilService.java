package xiaozhi.modules.sys.service;


import java.util.function.Consumer;


public interface SysUserUtilService {
    
    void assignUsername( Long userId, Consumer<String> setter);
}
