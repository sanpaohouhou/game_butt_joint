package com.tl.tgGame.wallet;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@Configurable
public class FeginConfig {
    @Value("${wallet.endpoint}")
    private String endpoint;
    @Value("${wallet.appid}")
    private String appId;
    @Value("${wallet.secret}")
    private String secret;


    @Bean
    public WalletAPI walletAPI() {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .requestInterceptor(new WalletRequestInterceptor(appId, secret))
                .decoder(new JacksonDecoder())
                .target(WalletAPI.class, endpoint);
    }

}
