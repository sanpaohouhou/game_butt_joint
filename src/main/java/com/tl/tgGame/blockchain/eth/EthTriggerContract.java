package com.tl.tgGame.blockchain.eth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import com.tl.tgGame.util.judge.JsonObjectTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.abi.DefaultFunctionEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.JsonRpc2_0Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Component
public class EthTriggerContract {

    @Resource
    private ConfigService configService;

    @Resource
    private JsonRpc2_0Web3j ethWeb3j;

    @Resource
    private EthBlockChainActuator ethBlockChainActuator;

    public String computeAddress(String walletAddress, BigInteger uid) throws IOException {
        String contractAddress = configService.get(ConfigConstants.ETH_CONTRACT_ADDRESS);
        EthCall send = ethWeb3j.ethCall(Transaction.createEthCallTransaction(null, contractAddress,
                new DefaultFunctionEncoder().encodeFunction(
                        new Function("computeAddress", Arrays.asList(new Address(walletAddress), new Uint(uid)),
                                Collections.emptyList())
                )), DefaultBlockParameterName.LATEST).send();
        Address address = new Address(send.getValue());
        return address.getValue();
    }

    /**
     * 归集
     *
     * @param toAddress    归集的地址
     * @param uid          用户的id(address表的ID)
     * @param erc20Address 归集的代币地址
     */
    public String recycle(String toAddress, long uid, String erc20Address, Consumer<String> consumer) {
        return recycle(toAddress, Arrays.asList(uid), Arrays.asList(erc20Address), consumer);
    }

    /**
     * 归集接口
     *
     * @param toAddress        归集地址 如果为null，修改为主钱包地址
     * @param uids             Address表中的id
     * @param erc20AddressList trc20的币种合约地址列表 可以传入多个一次性归集 地址数*用户数<400 最好
     * @return 返回交易hash
     */
    public String recycle(String toAddress, List<Long> uids, List<String> erc20AddressList, Consumer<String> consumer) {
        String contractAddress = configService.get(ConfigConstants.ETH_CONTRACT_ADDRESS);
        String dsw = configService.getAndDecrypt(ConfigConstants.MANAGER_WALLET);
        JsonObject jsonObject = new Gson().fromJson(dsw, JsonObject.class);
        String address = JsonObjectTool.getAsString(jsonObject, "ethAddress");
        long nonce = ethBlockChainActuator.getNonce(address);
        String password = JsonObjectTool.getAsString(jsonObject, "password");
        if (toAddress == null || toAddress.isEmpty()) toAddress = address;
        try {
            return ethBlockChainActuator.tokenSendRawTransaction(nonce,
                    contractAddress,
                    FunctionEncoder.encode(
                            new Function("recycle", Arrays.asList(new Address(toAddress),
                                    new DynamicArray(Uint256.class, uids.stream().map(e -> new Uint256(new BigInteger(e + ""))).collect(Collectors.toList())),
                                    new DynamicArray(Address.class, erc20AddressList.stream().map(Address::new).collect(Collectors.toList())))
                                    , new ArrayList<>())
                    ),
                    configService.getOrDefault(ConfigConstants.ETH_GAS_PRICE, "5"),
                    configService.getOrDefault(ConfigConstants.ETH_GAS_LIMIT, "200000"),
                    password, "归集: ", consumer);
        } catch (Exception ignored) {
            return null;
        }
    }
}
