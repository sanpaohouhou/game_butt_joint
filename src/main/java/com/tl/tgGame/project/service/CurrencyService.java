package com.tl.tgGame.project.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.entity.Currency;
import com.tl.tgGame.project.enums.BusinessEnum;
import com.tl.tgGame.project.enums.CurrencyLogType;
import com.tl.tgGame.project.enums.UserType;
import com.tl.tgGame.project.mapper.CurrencyMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 * 用户余额表 服务实现类
 * </p>
 *
 * @author hd
 * @since 2020-12-04
 */
@Service
public class CurrencyService extends ServiceImpl<CurrencyMapper, Currency> {

    public Currency get(long uid, UserType type) {
        return lambdaQuery().eq(Currency::getUid, uid).eq(Currency::getUserType, type).one();
    }

    public Currency getByUid(long uid) {
        return lambdaQuery().eq(Currency::getUid, uid).one();
    }

    public Currency getOrCreate(long uid, UserType type) {
        Currency currency = get(uid, type);
        if (currency == null) {
            currency = Currency.builder()
                    .uid(uid)
                    .userType(type)
                    .balance(BigDecimal.ZERO)
                    .freeze(BigDecimal.ZERO)
                    .remain(BigDecimal.ZERO)
                    .build();
            currencyMapper.insert(currency);
        }
        return currency;
    }

    /**
     * 增加金额
     *
     * @param uid    用户id
     * @param amount 金额
     * @param sn     订单号
     */
    @Transactional(rollbackFor = Exception.class)
    public void increase(long uid, UserType type, BusinessEnum businessEnum, BigDecimal amount, Object sn, String des) {
        if (!_increase(uid, type, businessEnum, amount, sn, des)) ErrorEnum.CREDIT_LACK.throwException();
    }

    /**
     * 扣除冻结金额
     *
     * @param uid    用户id
     * @param amount 金额
     * @param sn     订单号
     * @param des    订单号
     */
    @Transactional(rollbackFor = Exception.class)
    public void reduce(long uid, UserType type, BusinessEnum businessEnum, BigDecimal amount, Object sn, String des) {
        if (!_reduce(uid, type, businessEnum, amount, sn, des)) ErrorEnum.CREDIT_LACK.throwException();
    }

    /**
     * 扣除可用金额
     *
     * @param uid    用户id
     * @param amount 金额
     * @param sn     订单号
     */
    @Transactional(rollbackFor = Exception.class)
    public void withdraw(long uid, UserType type, BusinessEnum businessEnum, BigDecimal amount, Object sn, String des) {
        if (!_withdraw(uid, type, businessEnum, amount, sn, des)) ErrorEnum.CREDIT_LACK.throwException();
    }

    /**
     * 冻结金额
     *
     * @param uid    用户id
     * @param amount 金额
     * @param sn     订单号
     */
    @Transactional(rollbackFor = Exception.class)
    public void freeze(long uid, UserType type, BusinessEnum businessEnum, BigDecimal amount, Object sn, String des) {
        if (!_freeze(uid, type, businessEnum, amount, sn, des)) ErrorEnum.CREDIT_LACK.throwException();
    }

    /**
     * 解冻金额
     *
     * @param uid    用户id
     * @param amount 金额
     * @param sn     订单号
     */
    @Transactional(rollbackFor = Exception.class)
    public void unfreeze(long uid, UserType type, BusinessEnum businessEnum, BigDecimal amount, Object sn, String des) {
        if (!_unfreeze(uid, type, businessEnum, amount, sn, des)) ErrorEnum.CREDIT_LACK.throwException();
    }

    private boolean _increase(long uid, UserType type, BusinessEnum businessEnum, BigDecimal amount, Object sn, String des) {
        Currency currency = getOrCreate(uid, type);
        long result = currencyMapper.increase(uid, type, amount);
        currencyLogService.add(uid, type, CurrencyLogType.increase, businessEnum, amount, sn, des, currency.getBalance(), currency.getFreeze(), currency.getRemain());
        return result > 0L;
    }

    private boolean _reduce(long uid, UserType type, BusinessEnum businessEnum, BigDecimal amount, Object sn, String des) {
        Currency currency = getOrCreate(uid, type);
        long result = currencyMapper.reduce(uid, type, amount);
        currencyLogService.add(uid, type, CurrencyLogType.reduce, businessEnum, amount, sn, des, currency.getBalance(), currency.getFreeze(), currency.getRemain());
        return result > 0L;
    }

    private boolean _withdraw(long uid, UserType type, BusinessEnum businessEnum, BigDecimal amount, Object sn, String des) {
        Currency currency = getOrCreate(uid, type);
        long result = currencyMapper.withdraw(uid, type, amount);
        currencyLogService.add(uid, type, CurrencyLogType.withdraw, businessEnum, amount, sn, des, currency.getBalance(), currency.getFreeze(), currency.getRemain());
        return result > 0L;
    }

    private boolean _freeze(long uid, UserType type, BusinessEnum businessEnum, BigDecimal amount, Object sn, String des) {
        Currency currency = getOrCreate(uid, type);
        long result = currencyMapper.freeze(uid, type, amount);
        currencyLogService.add(uid, type, CurrencyLogType.freeze, businessEnum, amount, sn, des, currency.getBalance(), currency.getFreeze(), currency.getRemain());

        return result > 0L;
    }

    private boolean _unfreeze(long uid, UserType type, BusinessEnum businessEnum, BigDecimal amount, Object sn, String des) {
        Currency currency = getOrCreate(uid, type);
        long result = currencyMapper.unfreeze(uid, type, amount);
        currencyLogService.add(uid, type, CurrencyLogType.unfreeze, businessEnum, amount, sn, des, currency.getBalance(), currency.getFreeze(), currency.getRemain());
        return result > 0L;
    }

    @Resource
    private CurrencyLogService currencyLogService;
    @Resource
    private CurrencyMapper currencyMapper;
}
