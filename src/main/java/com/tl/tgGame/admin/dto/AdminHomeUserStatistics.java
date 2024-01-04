package com.tl.tgGame.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 15:52
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminHomeUserStatistics {

    /**
     * 总用户数
     */
    private Integer allUserCount = 0;
    /**
     * 今日新增用户数
     */
    private Integer todayUserCount = 0;
    /**
     * 总充值用户数量
     */
    private Integer allRechargeUserCount = 0;
    /**
     * 今日注册用户充值数量
     */
    private Integer todayRegisterUserRechargeCount = 0;
    /**
     * 今日充值用户数
     */
    private Integer todayRechargeUserCount = 0;
    /**
     * 今日下注用户数
     */
    private Integer todayBetUserCount = 0;
    /**
     * 今日充值金额
     */
    private BigDecimal todayRechargeAmount = BigDecimal.ZERO;
    /**
     * 今日提现金额
     */
    private BigDecimal todayWithdrawalAmount = BigDecimal.ZERO;
    /**
     * 今日用户下注金额
     */
    private BigDecimal todayUserBetAmount = BigDecimal.ZERO;
    /**
     * 当月用户下注金额
     */
    private BigDecimal userBetAmount = BigDecimal.ZERO;
    /**
     * 代理返佣总金额
     */
    private BigDecimal partnerAllCommissionAmount = BigDecimal.ZERO;

}
