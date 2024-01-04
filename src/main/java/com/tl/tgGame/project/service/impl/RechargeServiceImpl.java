package com.tl.tgGame.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.tl.tgGame.address.AddressBalanceService;
import com.tl.tgGame.address.AddressService;
import com.tl.tgGame.address.entity.Address;
import com.tl.tgGame.blockchain.UsdtContractAdapter;
import com.tl.tgGame.callback.entity.BscBep20Tx;
import com.tl.tgGame.callback.entity.EthErc20Tx;
import com.tl.tgGame.callback.entity.TronTrc20Tx;
import com.tl.tgGame.callback.service.BscBep20TxService;
import com.tl.tgGame.callback.service.EthErc20TxService;
import com.tl.tgGame.callback.service.TronTrc20TxService;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.entity.Currency;
import com.tl.tgGame.project.entity.Recharge;
import com.tl.tgGame.project.enums.BusinessEnum;
import com.tl.tgGame.project.enums.Network;
import com.tl.tgGame.project.enums.UserType;
import com.tl.tgGame.project.mapper.RechargeMapper;
import com.tl.tgGame.project.service.CurrencyService;
import com.tl.tgGame.project.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 用户充值提现记录表 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-07-12
 */
@Service
public class RechargeServiceImpl extends ServiceImpl<RechargeMapper, Recharge> implements RechargeService {
    @Resource
    private DefaultIdentifierGenerator defaultIdentifierGenerator;
    @Resource
    private CurrencyService currencyService;
    @Resource
    private BscBep20TxService bscBep20TxService;
    @Resource
    private EthErc20TxService ethErc20TxService;
    @Resource
    private TronTrc20TxService tronTrc20TxService;
    @Resource
    private UsdtContractAdapter usdtContractAdapter;
    @Resource
    private AddressService addressService;
    @Resource
    private AddressBalanceService addressBalanceService;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Override
    public Recharge addRecharge(Long uid, BigDecimal amount, UserType userType, String fromAddress,
                                String toAddress, String hash, Network network, String screen, String note,Currency currency) {
        Long id = defaultIdentifierGenerator.nextId(null);
        Recharge rechargeRecord = Recharge.builder()
                .id(id)
                .toAddress(toAddress)
                .fromAddress(fromAddress)
                .hash(hash)
                .userType(userType)
                .userId(uid)
                .network(network)
                .screen(screen)
                .amount(amount)
                .note(note)
                .freeze(currency.getFreeze())
                .remain(currency.getRemain())
                .balance(currency.getBalance())
                .createTime(LocalDateTime.now())
                .build();
        save(rechargeRecord);
        return rechargeRecord;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tronTxHandle(TronTrc20Tx tx) {
        boolean updated = tronTrc20TxService.lambdaUpdate()
                .eq(TronTrc20Tx::getId, tx.getId())
                .set(TronTrc20Tx::getHandled, true).update();
        if (!updated) ErrorEnum.SYSTEM_BUSY.throwException();
        Address address = addressService.getByTronAddress(tx.getTo());
        if (Objects.isNull(address)) return;
        txHandle(address.getUid(), tx.getFrom(), tx.getTo(), Network.TRC20, tx.getHash(), tx.getValue());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ethTxHandle(EthErc20Tx tx) {
        boolean updated = ethErc20TxService.lambdaUpdate()
                .eq(EthErc20Tx::getId, tx.getId())
                .set(EthErc20Tx::getHandled, true).update();
        if (!updated) ErrorEnum.SYSTEM_BUSY.throwException();
        Address address = addressService.getByEthAddress(tx.getTo());
        if (Objects.isNull(address)) return;
        txHandle(address.getUid(), tx.getFrom(), tx.getTo(), Network.ERC20, tx.getHash(), tx.getValue());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bscTxHandle(BscBep20Tx tx) {
        boolean updated = bscBep20TxService.lambdaUpdate()
                .eq(BscBep20Tx::getId, tx.getId())
                .set(BscBep20Tx::getHandled, true).update();
        if (!updated) ErrorEnum.SYSTEM_BUSY.throwException();
        Address address = addressService.getByBscAddress(tx.getTo());
        if (Objects.isNull(address)) return;
        txHandle(address.getUid(), tx.getFrom(), tx.getTo(), Network.BEP20, tx.getHash(), tx.getValue());
    }

    @Override
    public BigDecimal sumRecharge(Long userId, UserType userType, LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<Recharge> wrapper =
                new LambdaQueryWrapper<Recharge>()
                        .eq(Objects.nonNull(userId),Recharge::getUserId, userId)
                        .eq(Objects.nonNull(userType),Recharge::getUserType, userType)
                        .ge(Objects.nonNull(startTime), Recharge::getCreateTime, startTime)
                        .le(Objects.nonNull(endTime), Recharge::getCreateTime, endTime);
        return rechargeMapper.amountSum(wrapper);
    }

    @Override
    public Integer countRechargeNumber(List<Long> userIds, LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<Recharge> wrapper =
                new LambdaQueryWrapper<Recharge>()
                        .in(!CollectionUtils.isEmpty(userIds),Recharge::getUserId, userIds)
                        .ge(Objects.nonNull(startTime),Recharge::getCreateTime,startTime)
                        .le(Objects.nonNull(endTime),Recharge::getCreateTime,endTime);;
        return rechargeMapper.countRechargeNumber(wrapper);
    }

    @Override
    public Integer countJuniorRechargeNumber(Long inviteUserId, Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        QueryWrapper<Object> wrapper = new QueryWrapper<>().eq(Objects.nonNull(inviteUserId), "u.invite_user", inviteUserId)
                .eq(Objects.nonNull(userId), "re.user_id", userId)
                .ge(Objects.nonNull(startTime), "re.create_time", startTime)
                .le(Objects.nonNull(endTime), "re.create_time", endTime);
        return rechargeMapper.countJuniorRechargeNumber(wrapper);
    }

    @Override
    public BigDecimal sumJuniorRechargeAmount(Long inviteUserId, Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        QueryWrapper<Object> wrapper = new QueryWrapper<>().eq(Objects.nonNull(inviteUserId), "u.invite_user", inviteUserId)
                .eq(Objects.nonNull(userId), "re.user_id", userId)
                .ge(Objects.nonNull(startTime), "re.create_time", startTime)
                .le(Objects.nonNull(endTime), "re.create_time", endTime);
        return rechargeMapper.sumJuniorRechargeAmount(wrapper);
    }

    private void txHandle(long uid, String from, String to, Network network, String hash, BigInteger txVal) {
        Currency currency = currencyService.getByUid(uid);
        BigDecimal amount = usdtContractAdapter.uint256ToDecimal(network, txVal);
        Recharge recharge = addRecharge(uid, amount, currency.getUserType(), from, to, hash, network, null, "链上充值",currency);
        currencyService.increase(uid, currency.getUserType(), BusinessEnum.RECHARGE, amount, recharge.getId(), "充值-" + network);
        addressBalanceService.asyncAddressBalance(to, network);
    }
}
