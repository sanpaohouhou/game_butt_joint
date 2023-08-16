package com.tl.tgGame.project.service;

import com.google.gson.Gson;
import com.tl.tgGame.project.entity.Wallet;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WalletService {
    @Resource
    private ConfigService configService;

    public Wallet getNormalWallet() {
        String nw = configService.getAndDecrypt(ConfigConstants.NORMAL_WALLET);
        return new Gson().fromJson(nw, Wallet.class);
    }

    public Wallet getManagerWallet() {
        String nw = configService.getAndDecrypt(ConfigConstants.MANAGER_WALLET);
        return new Gson().fromJson(nw, Wallet.class);
    }

}
