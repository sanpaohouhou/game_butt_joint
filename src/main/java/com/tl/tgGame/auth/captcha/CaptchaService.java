package com.tl.tgGame.auth.captcha;


import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.util.CookieTool;
import com.tl.tgGame.util.RedisKeyGenerator;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class CaptchaService {
    @Resource
    private RedisKeyGenerator redisKeyGenerator;

    public void code(String code) {
        String cookieValue = UUID.randomUUID().toString();
        String key = redisKeyGenerator.generateKey(REDIS_SESSION_PREFIX, cookieValue);
        CookieTool.setCookie(COOKIE_NAME, cookieValue, COOKIE_AGE, httpServletRequest, httpServletResponse);
        stringRedisTemplate.boundValueOps(key).set(code.toLowerCase(), 5, TimeUnit.MINUTES);
    }

    public void verify(String code) {
        String cookie = CookieTool.getCookie(httpServletRequest, COOKIE_NAME);

        if (cookie == null || "".equals(cookie) || cookie.length() > 256) {
            ErrorEnum.INTERNAL_ERROR.throwException();
        }
        String key = redisKeyGenerator.generateKey(REDIS_SESSION_PREFIX, cookie);
        Object cacheCode = stringRedisTemplate.boundValueOps(key).get();
        stringRedisTemplate.delete(key);
        CookieTool.cancelCookie(COOKIE_NAME, httpServletRequest, httpServletResponse);
        if (cacheCode == null || !code.toLowerCase().equals(cacheCode.toString())){
            ErrorEnum.CAPTCHA_ERROR.throwException();
        }
    }


    private static final String COOKIE_NAME = "_c";
    private static final String REDIS_SESSION_PREFIX = "captcha_";
    private static final int COOKIE_AGE = -1;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private HttpServletRequest httpServletRequest;
    @Resource
    private HttpServletResponse httpServletResponse;
}
