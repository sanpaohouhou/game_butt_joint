package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/17 , 14:38
 * 个人资料
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BotPersonInfo {

    /**
     * 游戏账户
     */
    private String gameAccount;
    /**
     * 提现url
     */
    private String withdrawalUrl;
    /**
     * 账户余额
     */
    private BigDecimal balance = BigDecimal.ZERO;
    /**
     * 总充值
     */
    private BigDecimal allRecharge = BigDecimal.ZERO;
    /**
     * 总提现
     */
    private BigDecimal allWithdrawal = BigDecimal.ZERO;
    /**
     * 总返水
     */
    private BigDecimal allBackWater = BigDecimal.ZERO;
    /**
     * 待返水
     */
    private BigDecimal waitBackWater = BigDecimal.ZERO;
    /**
     * 总佣金
     */
    private BigDecimal allCommission = BigDecimal.ZERO;
    /**
     *总彩金
     */
    private BigDecimal allProfit = BigDecimal.ZERO;
    /**
     * 提现待审核
     */
    private BigDecimal waitAuthWithdrawal = BigDecimal.ZERO;
}
