package com.tl.tgGame.blockchain;


import com.tl.tgGame.blockchain.bsc.UsdtBscContract;
import com.tl.tgGame.blockchain.eth.UsdtEthContract;
import com.tl.tgGame.blockchain.tron.UsdtTronContract;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.enums.Network;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Consumer;

@Service
public class UsdtContractAdapter {
    @Resource
    private UsdtBscContract usdtBscContract;
    @Resource
    private UsdtTronContract usdtTronContract;
    @Resource
    private UsdtEthContract usdtEthContract;


    public Integer decimals(Network network) {
        switch (network) {
            case ERC20:
                return usdtEthContract.decimals();
            case TRC20:
                return usdtTronContract.decimals();
            case BEP20:
                return usdtBscContract.decimals();
        }
        return 0;
    }

    /**
     * 将链上获取的余额转化成实际余额
     *
     * @param network token类型
     * @param val     值
     * @return 实际余额
     */
    public BigDecimal uint256ToDecimal(Network network, BigInteger val) {
        return new BigDecimal(val).movePointLeft(decimals(network));
    }

    /**
     * 将实际余额转成链上的余额
     *
     * @param network token类型
     * @param val     值
     * @return 链上余额
     */
    public BigInteger decimalToUint256(Network network, BigDecimal val) {
        return val.movePointRight(decimals(network)).toBigInteger();
    }


    /**
     * 获取链上地址的余额
     *
     * @param address 地址
     * @param network token类型
     * @return 余额
     */
    public BigDecimal getAddressBalance(String address, Network network) {
        switch (network) {
            case ERC20:
                return uint256ToDecimal(network, usdtEthContract.balanceOf(address));
            case BEP20:
                return uint256ToDecimal(network, usdtBscContract.balanceOf(address));
            case TRC20:
                return uint256ToDecimal(network, usdtTronContract.balanceOf(address));
        }
        return BigDecimal.ZERO;
    }

    public String transfer(Network network, String to, BigDecimal amount, Consumer<String> consumer) {
        BigInteger val = decimalToUint256(network, amount);
        String txId = null;
        switch (network) {
            case ERC20:
                txId = usdtEthContract.transferNormalUsdt(to, val, consumer);
                break;
            case BEP20:
                txId = usdtBscContract.transferNormalUsdt(to, val, consumer);
                break;
            case TRC20:
                txId = usdtTronContract.transferNormalUsdt(to, val, consumer);
                break;
        }
        if (txId == null || txId.isEmpty()) {
            ErrorEnum.INTERNAL_ERROR.throwException("转账失败");
        }
        return txId;
    }


}
