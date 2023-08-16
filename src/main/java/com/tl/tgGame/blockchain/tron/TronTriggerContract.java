package com.tl.tgGame.blockchain.tron;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.protobuf.ByteString;

import com.tl.tgGame.blockchain.SignTransactionResult;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import com.tl.tgGame.util.judge.JsonObjectTool;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jcajce.provider.digest.SHA256;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;
import org.tron.api.GrpcAPI;
import org.tron.api.WalletGrpc;
import org.tron.common.utils.Base58Utils;
import org.tron.common.utils.ByteArray;
import org.tron.protos.Protocol;
import org.tron.protos.contract.SmartContractOuterClass;
import org.tron.tronj.abi.FunctionEncoder;
import org.tron.tronj.abi.datatypes.Address;
import org.tron.tronj.abi.datatypes.DynamicArray;
import org.tron.tronj.abi.datatypes.Function;
import org.tron.tronj.abi.datatypes.Uint;
import org.tron.tronj.abi.datatypes.generated.Uint256;
import org.tron.tronj.crypto.SECP256K1;
import org.tron.tronj.crypto.tuweniTypes.Bytes32;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @Author cs
 * @Date 2022-01-06 2:27 下午
 */
@Component
public class TronTriggerContract {

    @Resource
    private ConfigService configService;
    @Resource
    private WalletGrpc.WalletBlockingStub blockingStub;

    public String computeAddress(String walletAddress, BigInteger uid) {
        String contractAddress = configService.get(ConfigConstants.TRON_CONTRACT_ADDRESS);

        String data = FunctionEncoder.encode(
                new Function("computeAddress",
                        Arrays.asList(new Address(walletAddress), new Uint(uid)),
                        Arrays.asList()));

        GrpcAPI.TransactionExtention transactionExtention = blockingStub.triggerConstantContract(
                SmartContractOuterClass.TriggerSmartContract.newBuilder()
                        .setContractAddress(address2ByteString(contractAddress))
                        .setData(parseHex(data)).build());
        ByteString constantResult = transactionExtention.getConstantResult(0);

        return Base58Utils.encode58Check(ByteArray.fromHexString("41" + ByteArray.toHexString(constantResult.toByteArray()).substring(24)));
    }

    /**
     * trc20代币转账的方法
     *
     * @param ownerAddress    发送地址
     * @param toAddress       接收地址
     * @param contractAddress 合约地址 后续如果需要其他的币种转账，修改这个合约地址即可
     * @param amount          转账数额
     * @return
     */
    public String transfer(String ownerAddress, String toAddress, String contractAddress, BigInteger amount, String priKey, Consumer<String> consumer) {
        String data = FunctionEncoder.encode(
                new Function("transfer",
                        Arrays.asList(new Address(toAddress), new Uint256(amount)),
                        Arrays.asList()));
        return triggerSmartContract(ownerAddress, contractAddress, data, 40000000L, priKey, consumer);
    }

    /**
     * 归集接口
     *
     * @param toAddress        归集地址 如果为null，修改为主钱包地址
     * @param uids             Address表中的id
     * @param trc20AddressList trc20的币种合约地址列表 可以传入多个一次性归集 地址数*用户数<400 最好
     * @return 返回交易hash
     */
    public String recycle(String toAddress, List<Long> uids, List<String> trc20AddressList, Consumer<String> consumer) {
        String dsw = configService.getAndDecrypt(ConfigConstants.MANAGER_WALLET);
        JsonObject pswJson = new Gson().fromJson(dsw, JsonObject.class);
        String ownerAddress = JsonObjectTool.getAsString(pswJson, "tronAddress");
        String priKey = JsonObjectTool.getAsString(pswJson, "tronPriKey");
        String contractAddress = configService.getOrDefault(ConfigConstants.TRON_CONTRACT_ADDRESS, "TEuLfwtYM83r4TjkewRWFFFS1inHzdpsP2");
        if (toAddress == null || toAddress.isEmpty()) toAddress = ownerAddress;
        String data = FunctionEncoder.encode(
                new Function("recycle", Arrays.asList(new Address(toAddress),
                        new DynamicArray(Uint256.class, uids.stream().map(e -> new Uint256(new BigInteger(e + ""))).collect(Collectors.toList())),
                        new DynamicArray(Address.class, trc20AddressList.stream().map(Address::new).collect(Collectors.toList())))
                        , new ArrayList<>()));
        return triggerSmartContract(ownerAddress, contractAddress, data, 1000000000L, priKey, consumer);
    }

    public String triggerSmartContract(String ownerAddress, String contractAddress, String data, long feeLimit, String priKey, Consumer<String> consumer) {
        SmartContractOuterClass.TriggerSmartContract trigger = SmartContractOuterClass.TriggerSmartContract.newBuilder()
                .setOwnerAddress(address2ByteString(ownerAddress))
                .setContractAddress(address2ByteString(contractAddress))
                .setData(parseHex(data)).build();
        GrpcAPI.TransactionExtention txnExt = blockingStub.triggerContract(trigger);

        Protocol.Transaction signedTxn;
        String txid;
        if (feeLimit > 0L) {
            Protocol.Transaction transaction = txnExt.getTransaction();
            Protocol.Transaction txn = transaction.toBuilder()
                    .setRawData(transaction.getRawData().toBuilder().setFeeLimit(feeLimit).build()).build();
            SignTransactionResult result = signTransaction(txn, getKeyPair(priKey));
            txid = result.getTxid();
            signedTxn = result.getTxn();
        } else {
            signedTxn = signTransaction(txnExt, getKeyPair(priKey));
            txid = Hex.toHexString(txnExt.getTxid().toByteArray());
        }
        consumer.accept(txid);
//        System.out.println("时间: " + TimeTool.getDateTimeDisplayString(LocalDateTime.now()) + ", " + " < HASH: " + txid + " >");
        GrpcAPI.Return ret = blockingStub.broadcastTransaction(signedTxn);
        if (!ret.getResult()) ErrorEnum.INTERNAL_ERROR.throwException(ret.toString());
        return txid;
    }

    public SECP256K1.KeyPair getKeyPair(String tronPrivateKey) {
//        String hexPrivateKey = configService.get(tronPrivateKey);
        return SECP256K1.KeyPair.create(SECP256K1.PrivateKey.create(Bytes32.fromHexString(tronPrivateKey)));
    }

    public Protocol.Transaction signTransaction(GrpcAPI.TransactionExtention txnExt, SECP256K1.KeyPair kp) {
        SECP256K1.Signature sig = SECP256K1.sign(Bytes32.wrap(txnExt.getTxid().toByteArray()), kp);
        return txnExt.getTransaction().toBuilder().addSignature(ByteString.copyFrom(sig.encodedBytes().toArray())).build();
    }

    public SignTransactionResult signTransaction(Protocol.Transaction txn, SECP256K1.KeyPair kp) {
        SHA256.Digest digest = new SHA256.Digest();
        digest.update(txn.getRawData().toByteArray());
        byte[] txid = digest.digest();
        SECP256K1.Signature sig = SECP256K1.sign(Bytes32.wrap(txid), kp);
        Protocol.Transaction transaction = txn.toBuilder().addSignature(ByteString.copyFrom(sig.encodedBytes().toArray())).build();
        return SignTransactionResult.builder().txn(transaction).txid(Hex.toHexString(txid)).build();
    }

    public static ByteString parseHex(String data) {
        return ByteString.copyFrom(Hex.decode(data));
    }

    public static ByteString address2ByteString(String address) {
        return ByteString.copyFrom(address2Bytes(address));
    }

    public static byte[] address2Bytes(String address) {
        byte[] bytes = Base58Utils.decodeFromBase58Check(address);
        if (bytes == null) ErrorEnum.ADDRESS_ERROR.throwException();
        return bytes;
    }

    public BigDecimal trc20Balance(String ownerAddress, String contractAddrStr) {
        byte[] ownerAddresses = null;
        ownerAddresses = Base58Utils.decodeFromBase58Check(ownerAddress);
        if (ownerAddresses == null) ErrorEnum.ADDRESS_ERROR.throwException();

        String methodStr = "balanceOf(address)";
        String argsStr = "\"" + ownerAddress + "\"";
        boolean isHex = false;
        byte[] input = Hex.decode(Base58Utils.parseMethod(methodStr, argsStr, isHex));
        byte[] contractAddress = Base58Utils.decodeFromBase58Check(contractAddrStr);
        if (contractAddress == null) ErrorEnum.CONTRACT_ADDRESS_ERROR.throwException();

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
            return new BigDecimal(new BigInteger(StringUtils.isBlank(amount) ? "0" : amount, 16));

        }
        return BigDecimal.ZERO;
    }


    /**
     * 获取trx余额
     */
    public Long trxBalance(String ownerAddress) {
        if (StringUtils.isBlank(ownerAddress)) ErrorEnum.ADDRESS_ERROR.throwException();
        Protocol.Account account;
        byte[] addressBytes = Base58Utils.decodeFromBase58Check(ownerAddress);
        if (addressBytes == null) {
            ErrorEnum.ADDRESS_ERROR.throwException();
        }
        ByteString addressBS = ByteString.copyFrom(addressBytes);
        Protocol.Account request = Protocol.Account.newBuilder().setAddress(addressBS).build();
        account = blockingStub.getAccount(request);
        if (account == null) {
            return 0L;
        } else {
            return account.getBalance();
        }
    }
}
