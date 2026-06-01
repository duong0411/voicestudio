package xiaozhi.modules.security.service;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;


public interface CaptchaService {

    
    void create(HttpServletResponse response, String uuid) throws IOException;

    
    boolean validate(String uuid, String code, Boolean delete);

    
    void sendSMSValidateCode(String phone);

    
    boolean validateSMSValidateCode(String phone, String code, Boolean delete);
}
