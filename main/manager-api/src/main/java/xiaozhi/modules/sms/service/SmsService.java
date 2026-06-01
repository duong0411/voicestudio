package xiaozhi.modules.sms.service;


public interface SmsService {

    
    void sendVerificationCodeSms(String phone, String VerificationCode) ;
}
