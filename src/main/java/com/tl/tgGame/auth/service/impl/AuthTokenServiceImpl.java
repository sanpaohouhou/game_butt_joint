package com.tl.tgGame.auth.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.tl.tgGame.auth.service.AuthTokenService;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.util.RedisKeyGenerator;
import com.tl.tgGame.util.SecretsUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;


@Service
@Log4j2
public class AuthTokenServiceImpl implements AuthTokenService {
    public final String REDIS_PREFIX = "LOGIN_TOKEN";
    @Value("${security.token-expire}")
    private Duration tokenExpire;

    @Resource
    private RedisKeyGenerator redisKeyGenerator;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private HttpServletRequest httpServletRequest;

    public Long _uid() {
        return _uid(token());
    }

    public Long uid() {
        Long uid = _uid();
        if (uid == null) {
            ErrorEnum.NO_LOGIN.throwException();
        }
        return uid;
    }

    public String login(Long uid) {
        String token = SecretsUtil.tokenUrlSafe(64);
        String key = redisKeyGenerator.generateKey(REDIS_PREFIX, token);
        stringRedisTemplate.boundValueOps(key).set(String.valueOf(uid), tokenExpire);
        return token;
    }

    public Long _uid(String token) {
        if (!StringUtils.isBlank(token)) {
            String key = redisKeyGenerator.generateKey(REDIS_PREFIX, token);
            String uid = stringRedisTemplate.boundValueOps(key).get();
            if (StringUtils.isNotBlank(uid)) {
                stringRedisTemplate.boundValueOps(key).expire(tokenExpire);
                return Long.valueOf(uid);
            }
        }
        return null;
    }

    @Override
    public String token() {
        return httpServletRequest.getHeader("X-Token");
    }
}
