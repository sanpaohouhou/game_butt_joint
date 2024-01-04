package com.tl.tgGame.wallet;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

@Configuration
public class FeginConfig {
    @Value("${wallet.endpoint}")
    private String endpoint;
    @Value("${wallet.appid}")
    private String appId;
    @Value("${wallet.secret}")
    private String secret;


    @Bean
    public WalletAPI walletAPI() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return Feign.builder()
                .encoder(new JacksonEncoder())
                .requestInterceptor(new WalletRequestInterceptor(appId, secret))
                .decoder(new JacksonDecoder(Collections.singletonList(javaTimeModule)))
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .target(WalletAPI.class, endpoint);
    }

}
