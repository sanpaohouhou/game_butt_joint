package com.tl.tgGame.blockchain.tron;

import com.google.protobuf.ByteString;

import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.entity.Wallet;
import com.tl.tgGame.project.service.WalletService;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;
import org.tron.api.GrpcAPI;
import org.tron.api.WalletGrpc;
import org.tron.common.utils.Base58Utils;
import org.tron.protos.Protocol;
import org.tron.protos.contract.SmartContractOuterClass;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.function.Consumer;

@Slf4j
@Component
public class UsdtTronContract {
    @Resource
    private WalletGrpc.WalletBlockingStub blockingStub;
    @Resource
    private ConfigService configService;
    @Resource
    private TronTriggerContract tronTriggerContract;
    @Resource
    private WalletService walletService;

    /**
     * 代理商提现USDT
     */
    public String transferNormalUsdt(String toAddress, BigInteger amount, Consumer<String> consumer) {
        Wallet wallet = walletService.getNormalWallet();
        String usdtContract = configService.get(ConfigConstants.USDT_TRC20_CONTRACT);
        try {
            return tronTriggerContract.transfer(wallet.getTronAddress(), toAddress, usdtContract, amount, wallet.getTronPriKey(), consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BigInteger balanceOf(String address) {
        String contractAddrStr = configService.get(ConfigConstants.USDT_TRC20_CONTRACT);
        byte[] ownerAddresses;
        ownerAddresses = Base58Utils.decodeFromBase58Check(address);
        if (ownerAddresses == null) ErrorEnum.ADDRESS_ERROR.throwException();

        String methodStr = "balanceOf(address)";
        String argsStr = "\"" + address + "\"";
        boolean isHex = false;
        byte[] input = Hex.decode(Base58Utils.parseMethod(methodStr, argsStr, isHex));
        byte[] contractAddress = Base58Utils.decodeFromBase58Check(contractAddrStr);
        if (contractAddress == null) ErrorEnum.INTERNAL_ERROR.throwException("合约地址错误");

        SmartContractOuterClass.TriggerSmartContract.Builder builder = SmartContractOuterClass.TriggerSmartContract.newBuilder();
        builder.setOwnerAddress(ByteString.copyFrom(ownerAddresses));
        builder.setContractAddress(ByteString.copyFrom(contractAddress));
        builder.setData(ByteString.copyFrom(input));
        builder.setCallValue(0);

        SmartContractOuterClass.TriggerSmartContract triggerContract = builder.build();
        GrpcAPI.TransactionExtention transactionExtention;
        transactionExtention = blockingStub.triggerConstantContract(triggerContract);

        if (transactionExtention == null || !transactionExtention.getResult().getResult()) {
            ErrorEnum.INTERNAL_ERROR.throwException("获取trc20代币余额失败");
        }
        Protocol.Transaction transaction = transactionExtention.getTransaction();
        if (transaction.getRetCount() != 0
                && transactionExtention.getConstantResult(0) != null
                && transactionExtention.getResult() != null) {
            byte[] result = transactionExtention.getConstantResult(0).toByteArray();
            String amount = org.spongycastle.util.encoders.Hex.toHexString(result);
            return new BigInteger(amount, 16);

        }
        return BigInteger.ZERO;
    }

    public Integer decimals() {
        return Integer.parseInt(configService.getOrDefault(ConfigConstants.USDT_TRC20_DECIMAL, "6"));
    }

}
