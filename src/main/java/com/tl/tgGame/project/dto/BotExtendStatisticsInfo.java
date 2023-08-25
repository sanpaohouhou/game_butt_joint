package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/23 , 14:14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BotExtendStatisticsInfo {

    /**
     * 人数
     */
    private Integer peopleNumber;
    /**
     * 今日转码(今日通过邀请链接进群的)
     */
    private Integer todayJoinGroup;
    /**
     *当月转码(当月已通过邀请码链接进群的)
     */
    private Integer monthJoinGroup;
    /**
     *已结算佣金
     */
    private BigDecimal settledCommission;
    /**
     *今日充值
     */
    private BigDecimal todayAllRecharge;
    /**
     *当月充值
     */
    private BigDecimal monthAllRecharge;
    /**
     *今日总提现
     */
    private BigDecimal todayAllWithdrawal;
    /**
     *当月总提现
     */
    private BigDecimal monthALlWithdrawal;
    /**
     *今日总返水
     */
    private BigDecimal todayAllBackWater;
    /**
     *今日总彩金
     */
    private BigDecimal todayAllProfit;
    /**
     *当月总彩金
     */
    private BigDecimal monthAllProfit;
}
