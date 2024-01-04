package com.tl.tgGame.address;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.tl.tgGame.address.entity.Address;
import com.tl.tgGame.address.mapper.AddressMapper;
import com.tl.tgGame.blockchain.bsc.BscTriggerContract;
import com.tl.tgGame.blockchain.eth.EthTriggerContract;
import com.tl.tgGame.blockchain.tron.TronTriggerContract;
import com.tl.tgGame.callback.controller.CallbackController;
import com.tl.tgGame.callback.entity.ChainEnum;
import com.tl.tgGame.callback.req.TxConditionReq;
import com.tl.tgGame.callback.req.TxConditionWrapperReq;
import com.tl.tgGame.common.lock.RedisLock;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.entity.Wallet;
import com.tl.tgGame.project.service.WalletService;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import com.tl.tgGame.util.RedisKeyGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户充值地址表 服务实现类
 * </p>
 *
 * @author hd
 * @since 2020-12-14
 */
@Service
public class AddressService extends ServiceImpl<AddressMapper, Address> {
    @Resource
    private RedisLock redisLock;
    @Resource
    private BscTriggerContract bscTriggerContract;
    @Resource
    private TronTriggerContract tronTriggerContract;
    @Resource
    private EthTriggerContract ethTriggerContract;
    @Resource
    private ConfigService configService;
    @Resource
    private CallbackController callbackController;
    @Resource
    private RedisKeyGenerator redisKeyGenerator;
    @Resource
    private WalletService walletService;
    @Resource
    private DefaultIdentifierGenerator defaultIdentifierGenerator;

    @Transactional(rollbackFor = Exception.class)
    public Address get(long uid) throws IOException {
        String key = redisKeyGenerator.generateKey("AddressService.get", String.valueOf(uid));
        redisLock.lock(key, 1L, TimeUnit.MINUTES);
        try {
            Address address = lambdaQuery().eq(Address::getUid, uid).one();
            if (address != null) return address;
            Long id = defaultIdentifierGenerator.nextId(null);
            BigInteger idBigInteger = new BigInteger(String.valueOf(id));
            String bsc;
            String eth;
            String tron;
            Wallet wallet = walletService.getManagerWallet();
            String dswEthAddress = wallet.getEthAddress();
            String dswTronAddress = wallet.getTronAddress();
            bsc = bscTriggerContract.computeAddress(dswEthAddress, idBigInteger);
            eth = ethTriggerContract.computeAddress(dswEthAddress, idBigInteger);
            tron = tronTriggerContract.computeAddress(dswTronAddress, idBigInteger);

            if (StringUtils.isEmpty(bsc) || StringUtils.isEmpty(tron) || StringUtils.isEmpty(eth))
                ErrorEnum.NETWORK_ERROR.throwException();
            address = Address.builder()
                    .id(id)
                    .uid(uid)
                    .createTime(LocalDateTime.now())
                    .tron(tron)
                    .bsc(bsc)
                    .eth(eth)
                    .build();
            if (!save(address)) ErrorEnum.INTERNAL_ERROR.throwException();
//            List<TxConditionReq> list = new ArrayList<>();
//            list.add(TxConditionReq.builder().chain(ChainEnum.BSC).contractAddress(configService.get(ConfigConstants.USDT_BEP20_CONTRACT)).to(bsc).build());
//            list.add(TxConditionReq.builder().chain(ChainEnum.TRON).contractAddress(configService.get(ConfigConstants.USDT_TRC20_CONTRACT)).to(tron).build());
//            list.add(TxConditionReq.builder().chain(ChainEnum.ETH).contractAddress(configService.get(ConfigConstants.USDT_ERC20_CONTRACT)).to(eth).build());
//            callbackController.pushCondition(TxConditionWrapperReq.builder()
//                    .callbackAddress(configService.get(ConfigConstants.TX_CALLBACK))
//                    .txConditionReqs(list).build());
            return address;
        } finally {
            redisLock.unlock(key);
        }
    }

    public Address getByEthAddress(String address) {
        return super.getOne(Wrappers.lambdaQuery(Address.class).eq(Address::getEth, address));
    }

    public Address getByBscAddress(String address) {
        return super.getOne(Wrappers.lambdaQuery(Address.class).eq(Address::getBsc, address));
    }

    public Address getByTronAddress(String address) {
        return super.getOne(Wrappers.lambdaQuery(Address.class).eq(Address::getTron, address));
    }
}
