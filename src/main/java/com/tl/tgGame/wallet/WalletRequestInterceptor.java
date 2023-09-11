package com.tl.tgGame.wallet;

import com.tl.tgGame.util.Maps;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class WalletRequestInterceptor implements RequestInterceptor {

    private final String appId;
    private final String appSecret;

    public WalletRequestInterceptor(String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String nonce = RandomStringGeneral.randomint();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String str = String.join("_", appId, appSecret, timestamp, nonce);
        final String sign = DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
        requestTemplate.header("appid", appId);
        requestTemplate.header("nonce", nonce);
        requestTemplate.header("timestamp", timestamp);
        requestTemplate.header("sign", sign);
    }
}
