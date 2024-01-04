package com.tl.tgGame.blockchain.bsc;


import com.tl.tgGame.blockchain.TransactionInfo;
import com.tl.tgGame.blockchain.TransactionResult;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.web3j.abi.DefaultFunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.*;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.utils.Numeric;
import party.loveit.bip44forjava.utils.Bip44Utils;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Slf4j
@Service
public class BscBlockChainActuator {
    @Resource
    private JsonRpc2_0Web3j bscWeb3j;
    @Resource
    private ConfigService configService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public String tokenSendRawTransaction(long nonce, String curContractAddress, String chainParams, String gas, String gasLimit, String password, String operation, Consumer<String> consumer) {
        return tokenSendRawTransaction(nonce, curContractAddress, chainParams, gas, gasLimit, password, BigInteger.ZERO, operation, consumer);
    }

    public String tokenSendRawTransaction(long nonce, String curContractAddress, String chainParams, String gas, String gasLimit, String password, BigInteger value, String operation, Consumer<String> consumer) {
        String main_wallet_chain_id = configService.getOrDefault(ConfigConstants.BSC_CHAIN_ID, "56");
        BigInteger privateKey = Bip44Utils.getPathPrivateKey(Arrays.asList(password), "m/44'/60'/0'/0/0");
        BigInteger gasPrice = new BigDecimal(gas).multiply(new BigDecimal("1000000000")).toBigInteger();
        RawTransaction rawTransaction = RawTransaction.createTransaction(new BigInteger("" + nonce), gasPrice, new BigInteger(gasLimit), curContractAddress, value, chainParams);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, Long.parseLong(main_wallet_chain_id), Credentials.create(ECKeyPair.create(privateKey)));
        String signedTransactionData = Numeric.toHexString(signedMessage);
        EthSendTransaction send = null;
        try {
            String txHash = Hash.sha3(signedTransactionData);
            consumer.accept(txHash);
            send = bscWeb3j.ethSendRawTransaction(signedTransactionData).send();
        } catch (IOException e) {
            log.error(String.format("上链操作[%s], 执行异常 !", operation), e);
        }
        if (Objects.isNull(send)) {
            ErrorEnum.INTERNAL_ERROR.throwException("上链失败, 请稍后重试");
        }
        if (StringUtils.isBlank(send.getTransactionHash())) {
            ErrorEnum.INTERNAL_ERROR.throwException(send.getError().getMessage());
        }
        return send.getTransactionHash();
    }

    /**
     * 获取地址链上的最新的nance值
     *
     * @param address eth地址
     */
    public Long getNonce(String address) {
        Long nance = null;
        try {
            EthGetTransactionCount send = bscWeb3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
            nance = send.getTransactionCount().longValue();
        } catch (Exception ignored) {
        }
        return Objects.isNull(nance) ? null : nance;
    }

    public <T extends Type> EthCall ethCall(String contractAddress, String methodName, List<Type> params, Class<T> result) throws IOException {
        return bscWeb3j.ethCall(Transaction.createEthCallTransaction(null, contractAddress,
                new DefaultFunctionEncoder().encodeFunction(
                        new Function(methodName, params,
                                Arrays.asList(TypeReference.create(result)))
                )), DefaultBlockParameterName.LATEST).send();
    }

    public String getGas() {
        BoundValueOperations<String, Object> ops = redisTemplate.boundValueOps("bnbgas");
        Object gasPrice = ops.get();
        if (gasPrice != null) return gasPrice.toString();
        try {
            BigInteger bigInteger = bscWeb3j.ethGasPrice().send().getGasPrice();
            String price = new BigDecimal(bigInteger).movePointLeft(9).toString();
            ops.set(price, 1L, TimeUnit.MINUTES);
            return price;
        } catch (Exception ignore) {
        }
        return null;
    }

    public TransactionInfo getTransactionByHash(String transactionHash) throws IOException {
        Request<?, EthTransaction> ethTransactionRequest = bscWeb3j.ethGetTransactionByHash(transactionHash);
        EthTransaction send = ethTransactionRequest.send();
        org.web3j.protocol.core.methods.response.Transaction result;
        if (Objects.isNull(send) || Objects.isNull(result = send.getResult())) {
            return null;
        }
        // gasPrice
        BigInteger gasPrice = result.getGasPrice();
        Request<?, EthGetTransactionReceipt> ethGetTransactionReceiptRequest = bscWeb3j.ethGetTransactionReceipt(transactionHash);
        EthGetTransactionReceipt send2 = ethGetTransactionReceiptRequest.send();
        TransactionReceipt result2;
        if (Objects.isNull(send2) || Objects.isNull(result2 = send2.getResult())) {
            return null;
        }
        // gasUsed
        BigInteger gasUsed = result2.getGasUsed();
        // fee
        BigInteger fee = gasUsed.multiply(gasPrice);
        // status  0x1成功 / 0x0失败
        String status = result2.getStatus();
        return TransactionInfo.builder()
                .status(status)
                .fee(fee)
                .transaction(send)
                .transactionReceipt(send2)
                .build();
    }


    /**
     * @param transactionHash 交易hash
     * @return -1 pending状态, 0: 失败  1: 成功
     */
    public TransactionResult roughJudgmentTransactionByHash(String transactionHash) {
        TransactionInfo transactionByHash;
        try {
            transactionByHash = getTransactionByHash(transactionHash);
        } catch (IOException e) {
            return TransactionResult.builder().build();
        }
        if (Objects.isNull(transactionByHash)) {
            return TransactionResult.builder().build();
        }
        String status = transactionByHash.getStatus();
        EthGetTransactionReceipt transactionReceipt = transactionByHash.getTransactionReceipt();
        TransactionReceipt result = transactionReceipt.getResult();
        if (Objects.equals(status, "0x1") && Objects.nonNull(result)) {
            List<Log> logs = result.getLogs();
            return TransactionResult.builder()
                    .success(true)
                    .fee(transactionByHash.getFee())
                    .logs(logs)
                    .build();
        }
        return TransactionResult.builder()
                .success(false)
                .fee(transactionByHash.getFee())
                .build();
    }

    public static EthCall getEthCallWithParam(JsonRpc2_0Web3j web3j, String address, String symbol, List<Type> params) throws IOException {
        return web3j.ethCall(Transaction.createEthCallTransaction(null, address,
                new DefaultFunctionEncoder().encodeFunction(
                        new Function(symbol, params,
                                Arrays.asList())
                )), DefaultBlockParameterName.LATEST).send();
    }

    public static String verifyList(List<Type> list) {
        if (list == null || CollectionUtils.isEmpty(list)) {
            ErrorEnum.CONTRACT_ADDRESS_ERROR.throwException();
        }
        String str = list.get(0).getValue().toString();
        if (StringUtils.isBlank(str)) {
            ErrorEnum.CONTRACT_ADDRESS_ERROR.throwException();
        }
        return str;
    }

    public String getSymbol(String address) throws IOException {
        String value = getEthCallWithParam(bscWeb3j, address, "symbol", Arrays.asList()).getValue();
        List<Type> list = FunctionReturnDecoder.decode(value,
                Arrays.asList(TypeReference.create((Class) Utf8String.class))
        );
        String symbol = verifyList(list);
        return symbol;
    }

    public String getName(String address) throws IOException {
        String value = getEthCallWithParam(bscWeb3j, address, "name", Arrays.asList()).getValue();
        List<Type> list = FunctionReturnDecoder.decode(value,
                Arrays.asList(TypeReference.create((Class) Utf8String.class))
        );
        String name = verifyList(list);
        return name;
    }

    public int getDecimal(String address) throws IOException {
        String value = getEthCallWithParam(bscWeb3j, address, "decimals", Arrays.asList()).getValue();
        List<Type> list = FunctionReturnDecoder.decode(value,
                Arrays.asList(TypeReference.create((Class) Uint8.class))
        );
        String decimals = verifyList(list);
        return Integer.parseInt(decimals);
    }

    public BigInteger balanceOf(String contractAddress, String address) {
        String balanceOf;
        try {
            balanceOf = bscWeb3j.ethCall(Transaction.createEthCallTransaction(null, contractAddress, new DefaultFunctionEncoder().encodeFunction(
                    new Function("balanceOf", Arrays.asList(new Address(address)),
                            Arrays.asList(TypeReference.create(Uint.class)))
            )), DefaultBlockParameterName.LATEST).send().getValue();
        } catch (IOException e) {
            e.printStackTrace();
            return BigInteger.ZERO;
        }
        List<Type> list = FunctionReturnDecoder.decode(balanceOf,
                Arrays.asList(TypeReference.create((Class) Uint256.class))
        );
        balanceOf = list.get(0).getValue().toString();
        return new BigInteger(balanceOf);
    }

}
