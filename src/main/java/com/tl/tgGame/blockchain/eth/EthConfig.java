package com.tl.tgGame.blockchain.eth;


import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import javax.annotation.Resource;

@Configuration
public class EthConfig {

    private static final String ETH_CHAIN_MAIN_NET = "https://mainnet.infura.io/v3/9aa3d95b3bc440fa88ea12eaa4456161";

    @Resource
    private ConfigService configService;

    @Bean
    public Web3j ethWeb3j() {
        String eth_url;
        try {
            eth_url = configService.getOrDefault(ConfigConstants.ETH_CHAIN_URL, ETH_CHAIN_MAIN_NET);
        } catch (Exception e) {
            eth_url = ETH_CHAIN_MAIN_NET;
        }
        return Web3j.build(new HttpService(eth_url));
    }

}
