package com.tl.tgGame.auth.totp.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.tl.tgGame.admin.role.entity.Admin;
import com.tl.tgGame.admin.role.entity.Role;
import com.tl.tgGame.admin.role.service.AdminService;
import com.tl.tgGame.admin.role.service.RoleService;
import com.tl.tgGame.auth.service.AuthTokenService;
import com.tl.tgGame.auth.totp.mapper.TotpSecret;
import com.tl.tgGame.auth.totp.mapper.TotpSecretMapper;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.util.MapTool;
import com.tl.tgGame.util.Maps;
import com.tl.tgGame.util.RedisKeyGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
public class TotpService {
    @Value("${spring.application.name}")
    private String appName;
    @Resource
    private SecretGenerator secretGenerator;
    @Resource
    private QrDataFactory qrDataFactory;
    @Resource
    private QrGenerator qrGenerator;
    @Resource
    private TotpSecretMapper totpSecretMapper;
    @Resource
    private CodeVerifier verifier;
    @Resource
    private AuthTokenService authTokenService;
    @Resource
    private RedisKeyGenerator redisKeyGenerator;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private AdminService adminService;
    @Resource
    private RoleService roleService;
//    @Resource
//    private IPartnerService partnerService;
    private static final String REDIS_PREFIX = "TOTP_AUTH";


    public boolean hasSetup(Long uid) {
        return totpSecretMapper.selectOne(new LambdaQueryWrapper<TotpSecret>().eq(TotpSecret::getId, uid).eq(TotpSecret::isLogged, true)) != null;
    }

    public HashMap<String, String> setupDevice(Long uid, String issuer) throws QrGenerationException {
        // Generate and store the secret
        TotpSecret totpSecret = totpSecretMapper.selectById(uid);
        if (totpSecret == null) {
            String secret = secretGenerator.generate();
            totpSecret = TotpSecret.builder()
                    .id(uid).secret(secret).build();
            totpSecretMapper.insert(totpSecret);
        }

        QrData data = qrDataFactory.newBuilder()
                .label(appName)
                .secret(totpSecret.getSecret())
                .issuer(issuer)
                .build();
        // Generate the QR code image data as a base64 string which
        // can be used in an <img> tag:
        String qrCodeImage = getDataUriForImage(
                qrGenerator.generate(data),
                qrGenerator.getImageMimeType()
        );
        return Maps.newHashMap("base64", qrCodeImage, "secret", totpSecret.getSecret());
    }

    public void verify(Long uid, String code) {
        TotpSecret totpSecret = totpSecretMapper.selectById(uid);
        if (totpSecret == null || !verifier.isValidCode(totpSecret.getSecret(), code)) {
            ErrorEnum.TOTP_ERROR.throwException();
        }
        totpSecret.setLogged(true);
        totpSecretMapper.updateById(totpSecret);
    }

    public Map step1(Long id, String label) throws QrGenerationException {
        String refId = UUID.randomUUID().toString();
        String key = redisKeyGenerator.generateKey(REDIS_PREFIX, refId);
        stringRedisTemplate.boundValueOps(key).set(String.valueOf(id), Duration.ofMinutes(15));
        boolean hasSetup = hasSetup(id);
        return Maps.newHashMap(
                "refId", refId,
                "totp", hasSetup,
                "qrcode", hasSetup ? null : setupDevice(id, label)
        );
    }


    public Map<String, Object> step2(String refId, String code) {
        String key = redisKeyGenerator.generateKey(REDIS_PREFIX, refId);
        String uid = stringRedisTemplate.boundValueOps(key).get();
        if (StringUtils.isBlank(uid)) {
            ErrorEnum.TOTP_ERROR.throwException();
        }
        verify(Long.valueOf(uid), code);
        String token = authTokenService.login(Long.valueOf(uid));
        Admin admin = adminService.getById(uid);
        if(admin == null || !admin.getEnable()){
            ErrorEnum.PERMISSION_MISS.throwException("用户被禁用");
        }
        Role role = roleService.getById(admin.getRoleId());
        if(role == null || !role.getActive()){
            ErrorEnum.PERMISSION_MISS.throwException("角色被禁用");
        }
        return MapTool.Map().put("token", token).put("role", admin.getRoleId() != null ? admin.getRoleId().toString() : null);
    }

    public Map<String, Object> step3(String refId, String code) {
//        String key = redisKeyGenerator.generateKey(REDIS_PREFIX, refId);
//        String uid = stringRedisTemplate.boundValueOps(key).get();
//        if (StringUtils.isBlank(uid)) {
//            ErrorEnum.TOTP_ERROR.throwException();
//        }
//        verify(Long.valueOf(uid), code);
//        String token = authTokenService.login(Long.valueOf(uid));
//        Partner partner = partnerService.getById(uid);
//        if(partner == null || !partner.getEnable()){
//            ErrorEnum.PERMISSION_MISS.throwException("合伙人账号被禁用");
//        }
        return MapTool.Map().put("token", "token");
    }

}
