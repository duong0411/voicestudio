package xiaozhi.modules.sys;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;
import xiaozhi.modules.security.controller.LoginController;
import xiaozhi.modules.security.dto.LoginDTO;
import xiaozhi.modules.security.dto.SmsVerificationDTO;
import xiaozhi.modules.sys.dto.RetrievePasswordDTO;

@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
class loginControllerTest {

    @Autowired
    LoginController loginController;

    @Test
    public void testRegister() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("");
        loginDTO.setPassword("");
        loginController.register(loginDTO);
    }

    @Test
    public void testSmsVerification() {
        try {
            SmsVerificationDTO smsVerificationDTO = new SmsVerificationDTO();
            smsVerificationDTO.setPhone("");
            smsVerificationDTO.setCaptchaId("123456");
            smsVerificationDTO.setCaptcha("123456");
            loginController.smsVerification(smsVerificationDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testRetrievePassword() {
        try {
            RetrievePasswordDTO retrievePasswordDTO = new RetrievePasswordDTO();
            retrievePasswordDTO.setCode("123456");
            retrievePasswordDTO.setPhone("");
            retrievePasswordDTO.setPassword("");
            loginController.retrievePassword(retrievePasswordDTO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testSM2Utils() {
        java.util.Map<String, String> keyPair = xiaozhi.common.utils.SM2Utils.createKey();
        String pubKey = keyPair.get(xiaozhi.common.utils.SM2Utils.KEY_PUBLIC_KEY);
        String priKey = keyPair.get(xiaozhi.common.utils.SM2Utils.KEY_PRIVATE_KEY);
        org.junit.jupiter.api.Assertions.assertNotNull(pubKey);
        org.junit.jupiter.api.Assertions.assertEquals(64, priKey.length());

        String plainText = "helloWorld123";
        String cipherText = xiaozhi.common.utils.SM2Utils.encrypt(pubKey, plainText);
        String decrypted = xiaozhi.common.utils.SM2Utils.decrypt(priKey, cipherText);
        org.junit.jupiter.api.Assertions.assertEquals(plainText, decrypted);
    }

}