package com.tl.tgGame.blockchain.bsc;


import com.tl.tgGame.project.entity.Wallet;
import com.tl.tgGame.project.service.WalletService;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Uint;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

@Slf4j
@Component
public class UsdtBscContract {
    @Resource
    private BscBlockChainActuator bscBlockChainActuator;
    @Resource
    private ConfigService configService;
    @Resource
    private WalletService walletService;

    public String transferNormalUsdt(String to, BigInteger val, Consumer<String> consumer) {
        Wallet wallet = walletService.getNormalWallet();
        return transferUsdt(wallet.getPassword(), wallet.getEthAddress(), to, val, consumer);
    }

    public String transferUsdt(String password, String from, String to, BigInteger val, Consumer<String> consumer) {
        long nonce = bscBlockChainActuator.getNonce(from);
        try {
            //后续如果需要其他的币种转账  修改这个合约地址即可
            String BFContractAddress = configService.get(ConfigConstants.USDT_BEP20_CONTRACT);
            return bscBlockChainActuator.tokenSendRawTransaction(nonce,
                    BFContractAddress,
                    FunctionEncoder.encode(
                            new Function("transfer", Arrays.asList(new Address(to), new Uint(val)), new ArrayList<>())
                    ),
                    configService.getOrDefault(ConfigConstants.BSC_GAS_PRICE, "5"),
                    configService.getOrDefault(ConfigConstants.BSC_GAS_LIMIT, "800000"),
                    password, "转账USDT: ", consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BigInteger balanceOf(String address) {
        try {
            String usdtContractAddress = configService.get(ConfigConstants.USDT_BEP20_CONTRACT);
            return bscBlockChainActuator.balanceOf(usdtContractAddress, address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigInteger.ZERO;
    }

    public Integer decimals() {
        return Integer.parseInt(configService.getOrDefault(ConfigConstants.USDT_BEP20_DECIMAL, "18"));
    }

}
