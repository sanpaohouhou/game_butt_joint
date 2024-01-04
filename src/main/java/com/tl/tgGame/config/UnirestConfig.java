package com.tl.tgGame.config;

import kong.unirest.Config;
import kong.unirest.UnirestInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UnirestConfig {

    @Bean
    public UnirestInstance unirest() {
        Config config = new Config();
        config.verifySsl(false);

        return new UnirestInstance(config);
    }
}
