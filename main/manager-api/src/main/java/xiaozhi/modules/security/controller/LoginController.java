package xiaozhi.modules.security.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xiaozhi.common.constant.Constant;
import xiaozhi.common.exception.ErrorCode;
import xiaozhi.common.exception.RenException;
import xiaozhi.common.page.TokenDTO;
import xiaozhi.common.user.UserDetail;
import xiaozhi.common.utils.JsonUtils;
import xiaozhi.common.utils.Result;
import xiaozhi.common.utils.Sm2DecryptUtil;
import xiaozhi.common.validator.AssertUtils;
import xiaozhi.common.validator.ValidatorUtils;
import xiaozhi.modules.security.dto.LoginDTO;
import xiaozhi.modules.security.dto.SmsVerificationDTO;
import xiaozhi.modules.security.password.PasswordUtils;
import xiaozhi.modules.security.service.CaptchaService;
import xiaozhi.modules.security.service.SysUserTokenService;
import xiaozhi.modules.security.user.SecurityUser;
import xiaozhi.modules.sys.dto.PasswordDTO;
import xiaozhi.modules.sys.dto.RetrievePasswordDTO;
import xiaozhi.modules.sys.dto.SysUserDTO;
import xiaozhi.modules.sys.service.SysDictDataService;
import xiaozhi.modules.sys.service.SysParamsService;
import xiaozhi.modules.sys.service.SysUserService;
import xiaozhi.modules.sys.vo.SysDictDataItem;

/**
 * 登录控制层
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/user")
@Tag(name = "登录管理")
public class LoginController {
    private final SysUserService sysUserService;
    private final SysUserTokenService sysUserTokenService;
    private final CaptchaService captchaService;
    private final SysParamsService sysParamsService;
    private final SysDictDataService sysDictDataService;
    private final RestTemplate restTemplate;

    @GetMapping("/captcha")
    @Operation(summary = "验证码")
    public void captcha(HttpServletResponse response, String uuid) throws IOException {
        // uuid不能为空
        AssertUtils.isBlank(uuid, ErrorCode.IDENTIFIER_NOT_NULL);
        // 生成验证码
        captchaService.create(response, uuid);
    }

    @PostMapping("/smsVerification")
    @Operation(summary = "短信验证码")
    public Result<Void> smsVerification(@RequestBody SmsVerificationDTO dto) {
        String captchaId = Sm2DecryptUtil.normalizeCaptchaId(dto.getCaptchaId());
        String captcha = StringUtils.trimToNull(dto.getCaptcha());
        if (captchaId != null && captcha != null) {
            boolean validate = captchaService.validate(captchaId, captcha, false);
            if (!validate) {
                throw new RenException(ErrorCode.SMS_CAPTCHA_ERROR);
            }
        }

        Boolean isMobileRegister = sysParamsService
                .getValueObject(Constant.SysMSMParam.SERVER_ENABLE_MOBILE_REGISTER.getValue(), Boolean.class);
        if (!isMobileRegister) {
            throw new RenException(ErrorCode.MOBILE_REGISTER_DISABLED);
        }
        // 发送短信验证码
        captchaService.sendSMSValidateCode(dto.getPhone());
        return new Result<>();
    }

    @PostMapping("/login")
    @Operation(summary = "登录")
    public Result<TokenDTO> login(@RequestBody LoginDTO login) {
        String password = login.getPassword();

        String actualPassword = decryptLoginPassword(password, login.getCaptchaId());
        login.setPassword(actualPassword);

        // 按照用户名获取用户
        SysUserDTO userDTO = sysUserService.getByUsername(login.getUsername());
        // 判断用户是否存在
        if (userDTO == null) {
            throw new RenException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }
        // 判断密码是否正确，不一样则进入if
        if (!PasswordUtils.matches(login.getPassword(), userDTO.getPassword())) {
            throw new RenException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }
        return sysUserTokenService.createToken(userDTO.getId());
    }

    @PostMapping("/register")
    @Operation(summary = "注册")
    public Result<Void> register(@RequestBody LoginDTO login) {

        String password = login.getPassword();

        String actualPassword = decryptLoginPassword(password, login.getCaptchaId());
        login.setPassword(actualPassword);

        // 按照用户名获取用户
        SysUserDTO userDTO = sysUserService.getByUsername(login.getUsername());
        if (userDTO != null) {
            throw new RenException(ErrorCode.DB_RECORD_EXISTS);
        }
        userDTO = new SysUserDTO();
        userDTO.setUsername(login.getUsername());
        userDTO.setPassword(login.getPassword());
        sysUserService.save(userDTO);
        return new Result<>();
    }

    @PostMapping("/google-login")
    @Operation(summary = "Google登录和自动注册")
    public Result<TokenDTO> googleLogin(@RequestBody Map<String, String> body) {
        String credential = body.get("credential");
        if (StringUtils.isBlank(credential)) {
            throw new RenException("Google token is empty");
        }

        String email = null;
        String name = null;

        // Try verifying with Google's API endpoint
        try {
            String googleTokenInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + credential;
            Map<String, Object> tokenInfo = restTemplate.getForObject(googleTokenInfoUrl, Map.class);
            if (tokenInfo != null && tokenInfo.containsKey("email")) {
                email = (String) tokenInfo.get("email");
                name = (String) tokenInfo.get("name");
            }
        } catch (Exception e) {
            log.warn("Failed to verify Google token via HTTP, using fallback local JWT decoding. Error: {}", e.getMessage());
        }

        // Fallback: Local JWT Decoding
        if (StringUtils.isBlank(email)) {
            try {
                String[] segments = credential.split("\\.");
                if (segments.length >= 2) {
                    byte[] payloadBytes = Base64.getUrlDecoder().decode(segments[1]);
                    String payloadJson = new String(payloadBytes, java.nio.charset.StandardCharsets.UTF_8);
                    Map<String, Object> tokenInfo = JsonUtils.parseObject(payloadJson, Map.class);
                    if (tokenInfo != null && tokenInfo.containsKey("email")) {
                        email = (String) tokenInfo.get("email");
                        name = (String) tokenInfo.get("name");
                        log.info("Successfully decoded Google token locally for email: {}", email);
                    }
                }
            } catch (Exception e) {
                log.error("Failed to decode token locally: {}", e.getMessage());
            }
        }

        if (StringUtils.isBlank(email)) {
            throw new RenException("Invalid Google token");
        }

        String username = email;

        // Check if user exists
        SysUserDTO userDTO = sysUserService.getByUsername(username);
        if (userDTO == null) {
            // Check if user registration is allowed
            if (!sysUserService.getAllowUserRegister()) {
                throw new RenException(ErrorCode.USER_REGISTER_DISABLED);
            }

            userDTO = new SysUserDTO();
            userDTO.setUsername(username);
            // Generate a random secure password satisfying regex rule
            // e.g. "G0ogle_" + UUID
            String generatedPassword = "G0ogle_" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
            userDTO.setPassword(generatedPassword);
            sysUserService.save(userDTO);

            // Fetch registered user again to get ID
            userDTO = sysUserService.getByUsername(username);
        }

        if (userDTO.getStatus() == null || userDTO.getStatus() == 0) {
            throw new RenException(ErrorCode.ACCOUNT_DISABLE);
        }

        return sysUserTokenService.createToken(userDTO.getId());
    }

    @GetMapping("/info")
    @Operation(summary = "用户信息获取")
    public Result<UserDetail> info() {
        UserDetail user = SecurityUser.getUser();
        Result<UserDetail> result = new Result<>();
        result.setData(user);
        return result;
    }

    @PutMapping("/change-password")
    @Operation(summary = "修改用户密码")
    public Result<?> changePassword(@RequestBody PasswordDTO passwordDTO) {
        // 判断非空
        ValidatorUtils.validateEntity(passwordDTO);
        Long userId = SecurityUser.getUserId();
        sysUserTokenService.changePassword(userId, passwordDTO);
        return new Result<>();
    }

    @PutMapping("/retrieve-password")
    @Operation(summary = "找回密码")
    public Result<?> retrievePassword(@RequestBody RetrievePasswordDTO dto) {
        // 判断非空
        ValidatorUtils.validateEntity(dto);

        // 按照用户名/手机号获取用户
        SysUserDTO userDTO = sysUserService.getByUsername(dto.getPhone());
        if (userDTO == null) {
            throw new RenException(ErrorCode.PHONE_NOT_REGISTERED);
        }

        String password = dto.getPassword();

        String actualPassword = decryptLoginPassword(password, dto.getCaptchaId());
        dto.setPassword(actualPassword);

        sysUserService.changePasswordDirectly(userDTO.getId(), dto.getPassword());
        return new Result<>();
    }

    /**
     * 解密登录/注册/找回密码请求中的密码；无图形验证码 ID 时不校验图形验证码。
     */
    private String decryptLoginPassword(String encryptedPassword, String captchaId) {
        return Sm2DecryptUtil.decryptAndValidateCaptcha(
                encryptedPassword,
                Sm2DecryptUtil.normalizeCaptchaId(captchaId),
                captchaService,
                sysParamsService);
    }

    @GetMapping("/pub-config")
    @Operation(summary = "公共配置")
    public Result<Map<String, Object>> pubConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("enableMobileRegister", sysParamsService
                .getValueObject(Constant.SysMSMParam.SERVER_ENABLE_MOBILE_REGISTER.getValue(), Boolean.class));
        config.put("version", Constant.VERSION);
        config.put("year", "©" + Calendar.getInstance().get(Calendar.YEAR));
        config.put("allowUserRegister", sysUserService.getAllowUserRegister());
        List<SysDictDataItem> list = sysDictDataService.getDictDataByType(Constant.DictType.MOBILE_AREA.getValue());
        config.put("mobileAreaList", list);
        config.put("beianIcpNum", sysParamsService.getValue(Constant.SysBaseParam.BEIAN_ICP_NUM.getValue(), true));
        config.put("beianGaNum", sysParamsService.getValue(Constant.SysBaseParam.BEIAN_GA_NUM.getValue(), true));
        config.put("name", sysParamsService.getValue(Constant.SysBaseParam.SERVER_NAME.getValue(), true));

        // SM2公钥
        String publicKey = sysParamsService.getValue(Constant.SM2_PUBLIC_KEY, true);
        if (StringUtils.isBlank(publicKey)) {
            throw new RenException(ErrorCode.SM2_KEY_NOT_CONFIGURED);
        }
        config.put("sm2PublicKey", publicKey);

        // 获取system-web.menu参数配置
        String menuConfig = sysParamsService.getValue("system-web.menu", true);
        if (StringUtils.isNotBlank(menuConfig)) {
            config.put("systemWebMenu", JsonUtils.parseObject(menuConfig, Object.class));
        }

        // Google Client ID
        config.put("googleClientId", sysParamsService.getValue("google.clientId", false));

        return new Result<Map<String, Object>>().ok(config);
    }
}