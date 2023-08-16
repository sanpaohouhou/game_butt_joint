package com.tl.tgGame.project.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.tl.tgGame.project.entity.Withdrawal;
import com.tl.tgGame.project.enums.Network;
import com.tl.tgGame.project.enums.UserType;

import java.math.BigDecimal;

public interface WithdrawalService extends IService<Withdrawal> {

    /**
     * usdt提现
     *
     * @param uid     用户id
     * @param network 网络
     * @param userType currencyType
     * @param to      提现到哪个地址
     * @param amount  提现金额
     * @return 提现记录
     */
    Withdrawal withdraw(long uid, UserType userType, Network network, String to, BigDecimal amount);

    Withdrawal addWithdrawal(Long uid, BigDecimal amount, UserType userType, String fromAddress, String toAddress, String hash, Network network, String screen, String note);


    /**
     * 审核提现订单
     *
     * @param id     订单id
     * @param result 是否通过
     * @param note   备注
     * @return 提现订单
     */
    Withdrawal audit(long id, boolean result, String note);

    /**
     * 获取今日用户已提现的金额
     *
     * @param uid 用户id
     * @return 金额
     */
    BigDecimal todayWithdrawalAmount(long uid);

    /**
     * 获取今日用户已提现的次数
     *
     * @param uid 用户id
     * @return 次数
     */
    Integer todayUserWithdrawalCount(long uid);

    /**
     * 获取全平台提现金额
     *
     * @return 金额
     */
    BigDecimal todayPlatformWithdrawalAmount();

    void transfer(Withdrawal withdrawal);

    void transferFail(Withdrawal withdrawal);

    void transferSuccess(Withdrawal withdrawal);

}
