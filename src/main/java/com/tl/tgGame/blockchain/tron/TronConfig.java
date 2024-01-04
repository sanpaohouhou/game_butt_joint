package com.tl.tgGame.blockchain.tron;


import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tron.api.WalletGrpc;

import javax.annotation.Resource;

/**
 * @Author cs
 * @Date 2022-01-07 11:19 上午
 */
@Configuration
public class TronConfig {
    private static final String TRON_SMART_CHAIN_MAIN_NET = "grpc.trongrid.io:50051";
    @Resource
    private ConfigService configService;

    @Bean
    public WalletGrpc.WalletBlockingStub blockingStub() {
        String url;
        try {
            url = configService.getOrDefault(ConfigConstants.TRON_CHAIN_URL, TRON_SMART_CHAIN_MAIN_NET);
        } catch (Exception e) {
            url = TRON_SMART_CHAIN_MAIN_NET;
        }
        ManagedChannel channel = ManagedChannelBuilder.forTarget(url).usePlaintext().build();
        return WalletGrpc.newBlockingStub(channel);
    }
}
