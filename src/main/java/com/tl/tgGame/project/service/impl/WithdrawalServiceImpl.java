package com.tl.tgGame.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.tl.tgGame.common.lock.RedisLock;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.entity.Currency;
import com.tl.tgGame.project.entity.Withdrawal;
import com.tl.tgGame.project.enums.BusinessEnum;
import com.tl.tgGame.project.enums.Network;
import com.tl.tgGame.project.enums.UserType;
import com.tl.tgGame.project.enums.WithdrawStatus;
import com.tl.tgGame.project.mapper.WithdrawalMapper;
import com.tl.tgGame.project.service.CurrencyService;
import com.tl.tgGame.project.service.WithdrawalService;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import com.tl.tgGame.util.CompareUtil;
import com.tl.tgGame.util.RedisKeyGenerator;
import com.tl.tgGame.util.TimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

@Service
public class WithdrawalServiceImpl extends ServiceImpl<WithdrawalMapper, Withdrawal> implements WithdrawalService {
    @Resource
    private ConfigService configService;
    @Resource
    private RedisLock redisLock;
    @Resource
    private RedisKeyGenerator redisKeyGenerator;
    @Resource
    private CurrencyService currencyService;
//    @Resource
//    private WalletService walletService;
//    @Resource
//    private UsdtContractAdapter usdtContractAdapter;
//    @Resource
//    private HeatPurseRecordService heatPurseRecordService;
    @Resource
    private DefaultIdentifierGenerator defaultIdentifierGenerator;

    @Override
    public Withdrawal addWithdrawal(Long uid, BigDecimal amount, UserType userType, String fromAddress, String toAddress, String hash, Network network, String screen, String note) {
        long id = defaultIdentifierGenerator.nextId(null);
        Withdrawal withdrawal = Withdrawal.builder()
                .id(id)
                .uid(uid)
                .toAddress(toAddress)
                .fromAddress(fromAddress)
                .amount(amount)
                .fee(BigDecimal.ZERO)
                .actualAmount(amount)
                .hash(hash)
                .screen(screen)
                .network(network)
                .status(WithdrawStatus.withdraw_success)
                .note(note)
                .createTime(LocalDateTime.now())
                .completeTime(LocalDateTime.now())
                .userType(userType)
                .build();
        save(withdrawal);
        return withdrawal;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Withdrawal withdraw(long uid, UserType userType, Network network, String to, BigDecimal amount) {
        String key = redisKeyGenerator.generateKey("withdraw", userType, uid);
        redisLock.redissonLock(key);
        try {
            Currency currency = currencyService.getOrCreate(uid, userType);
            if (CompareUtil.isLessThan(amount, BigDecimal.TEN)) {
                ErrorEnum.WITHDRAW_FAIL.throwException("提现金额必须大于10U");
            }
            if (amount.compareTo(currency.getRemain()) > 0) {
                ErrorEnum.CREDIT_LACK.throwException();
            }
            BigDecimal fixedFee = BigDecimal.ZERO;
            switch (network) {
                case TRC20:
                    fixedFee = configService.getDecimal(ConfigConstants.TRC20_USDT_WITHDRAWAL_FIXED_FEE);
                    break;
                case BEP20:
                    fixedFee = configService.getDecimal(ConfigConstants.BEP20_USDT_WITHDRAWAL_FIXED_FEE);
                    break;
                case ERC20:
                    fixedFee = configService.getDecimal(ConfigConstants.ERC20_USDT_WITHDRAWAL_FIXED_FEE);
                    break;
            }
            if (!CompareUtil.isGreaterThan(amount.subtract(fixedFee), BigDecimal.ZERO)) {
                ErrorEnum.WITHDRAW_FAIL.throwException("提现金额必须大于手续费");
            }
            WithdrawStatus status = WithdrawStatus.created;
//            Wallet wallet = walletService.getNormalWallet();

            String fromAddress = null;
            if (network.equals(Network.TRC20)) {
//                fromAddress = wallet.getTronAddress();
            } else {
//                fromAddress = wallet.getEthAddress();
            }
            BigDecimal actualAmount = amount.subtract(fixedFee);
            long id = defaultIdentifierGenerator.nextId(null);
            Withdrawal withdrawal = Withdrawal.builder()
                    .id(id)
                    .uid(uid)
                    .createTime(LocalDateTime.now())
                    .toAddress(to)
                    .fromAddress(fromAddress)
                    .amount(amount)
                    .fee(fixedFee)
                    .actualAmount(actualAmount)
                    .network(network)
                    .status(status)
                    .userType(userType)
                    .build();
            boolean saved = save(withdrawal);
            if (!saved) {
                ErrorEnum.WITHDRAW_FAIL.throwException("提现失败");
            }
            // 冻结余额
            currencyService.freeze(uid, userType, BusinessEnum.WITHDRAW, amount, id, "保证金提现预冻结");
            return withdrawal;
        } finally {
            redisLock.redissonUnLock();
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Withdrawal audit(long id, boolean result, String note) {
        String key = redisKeyGenerator.generateKey("audit", id);
        redisLock.redissonLock(key);
        try {
            Withdrawal withdrawal = getById(id);
            if (Objects.isNull(withdrawal)) {
                ErrorEnum.OBJECT_NOT_FOUND.throwException("订单不存在");
            }
            if (!withdrawal.getStatus().equals(WithdrawStatus.created)) {
                ErrorEnum.ORDER_EXCEPTION.throwException();
            }
            if (result) {
                withdrawal.setStatus(WithdrawStatus.review_success);
            } else {
                withdrawal.setStatus(WithdrawStatus.review_fail);
                withdrawal.setCompleteTime(LocalDateTime.now());
                currencyService.unfreeze(withdrawal.getUid(), withdrawal.getUserType(), BusinessEnum.WITHDRAW, withdrawal.getAmount(), withdrawal.getId(), "保证金提现订单审核失败解冻");
            }
            withdrawal.setNote(note);
            updateById(withdrawal);
            return withdrawal;
        } finally {
            redisLock.redissonUnLock();
        }
    }

    @Override
    public BigDecimal todayWithdrawalAmount(long uid) {
        return (BigDecimal) getMap(new QueryWrapper<Withdrawal>().select("IFNULL(SUM(`amount`), 0) AS `total`").lambda()
                .eq(Withdrawal::getUid, uid)
                .gt(Withdrawal::getCreateTime, TimeUtil.getTodayBegin())
                .notIn(Withdrawal::getStatus, Arrays.asList(WithdrawStatus.withdraw_fail, WithdrawStatus.review_fail))).get("total");
    }

    @Override
    public Integer todayUserWithdrawalCount(long uid) {
        return lambdaQuery().eq(Withdrawal::getUid, uid)
                .gt(Withdrawal::getCreateTime, TimeUtil.getTodayBegin())
                .notIn(Withdrawal::getStatus, Arrays.asList(WithdrawStatus.withdraw_fail, WithdrawStatus.review_fail)).count();

    }

    @Override
    public BigDecimal todayPlatformWithdrawalAmount() {
        return (BigDecimal) getMap(new QueryWrapper<Withdrawal>().select("IFNULL(SUM(`amount`), 0) AS `total`").lambda()
                .gt(Withdrawal::getCreateTime, TimeUtil.getTodayBegin())
                .notIn(Withdrawal::getStatus, Arrays.asList(WithdrawStatus.withdraw_fail, WithdrawStatus.review_fail))).get("total");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void transfer(Withdrawal withdrawal) {
        Consumer<String> consumer = (tx) -> {
            withdrawal.setHash(tx);
            withdrawal.setStatus(WithdrawStatus.withdrawing);
            updateById(withdrawal);
        };
//        usdtContractAdapter.transfer(withdrawal.getNetwork(), withdrawal.getToAddress(), withdrawal.getActualAmount(), consumer);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void transferFail(Withdrawal withdrawal) {
        withdrawal.setStatus(WithdrawStatus.withdraw_fail);
        withdrawal.setCompleteTime(LocalDateTime.now());
        currencyService.unfreeze(withdrawal.getUid(), withdrawal.getUserType(), BusinessEnum.WITHDRAW, withdrawal.getAmount(), withdrawal.getId(), "保证金提现失败解冻");
        updateById(withdrawal);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void transferSuccess(Withdrawal withdrawal) {
        withdrawal.setStatus(WithdrawStatus.withdraw_success);
        withdrawal.setCompleteTime(LocalDateTime.now());
        updateById(withdrawal);
        currencyService.reduce(withdrawal.getUid(), withdrawal.getUserType(), BusinessEnum.WITHDRAW, withdrawal.getAmount(), withdrawal.getId(), "保证金提现");
//        heatPurseRecordService.save(HeatPurseRecord.builder()
//                .uid(withdrawal.getUid())
//                .sn(withdrawal.getId().toString())
//                .amount(withdrawal.getActualAmount())
//                .hash(withdrawal.getHash())
//                .fromAddress(withdrawal.getFromAddress())
//                .toAddress(withdrawal.getToAddress())
//                .network(withdrawal.getNetwork())
//                .createTime(LocalDateTime.now())
//                .recordType(withdrawal.getUserType().equals(UserType.USER) ? RecordType.user_withdrawal : RecordType.partner_withdrawal)
//                .build());
    }
}
