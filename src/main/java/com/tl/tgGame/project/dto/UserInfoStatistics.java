package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/4 , 16:55
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoStatistics {

    //游戏账号
    private String gameAccount;
    /**
     * 提现地址
     */
    private String address;
    /**
     * 账户余额
     */
    private BigDecimal balance;
    /**
     * 总充值
     */
    private BigDecimal allRecharge;
    /**
     * 总提现
     */
    private BigDecimal allWithdrawal;
    /**
     * 总返水
     */
    private BigDecimal allBackWater;
    /**
     * 待返水
     */
    private BigDecimal waitBackWater;
    /**
     * 总佣金
     */
    private BigDecimal allCommission;
    /**
     * 总彩金(佣金+返水)
     */
    private BigDecimal allIncome;
    /**
     * 提现待审核
     */
    private BigDecimal waitAuthWithdrawal;
}
