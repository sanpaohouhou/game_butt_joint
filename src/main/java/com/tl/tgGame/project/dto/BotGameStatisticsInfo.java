package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/23 , 10:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BotGameStatisticsInfo {

    /**
     * 当日总下注
     */
    private BigDecimal dayBet = BigDecimal.ZERO;
    /**
     * 当日总派彩
     */
    private BigDecimal dayFestoon = BigDecimal.ZERO;
    /**
     * 当日总盈利
     */
    private BigDecimal dayProfit = BigDecimal.ZERO;

    /**
     * 当周总下注
     */
    private BigDecimal weekBet = BigDecimal.ZERO;

    /**
     * 当周总派彩
     */
    private BigDecimal weekFestoon = BigDecimal.ZERO;

    /**
     * 当周总盈利
     */
    private BigDecimal weekProfit = BigDecimal.ZERO;

    /**
     * 当月总下注
     */
    private BigDecimal monthBet = BigDecimal.ZERO;

    /**
     * 当月总派彩
     */
    private BigDecimal monthFestoon = BigDecimal.ZERO;
    /**
     * 当月总盈利
     */
    private BigDecimal monthProfit = BigDecimal.ZERO;
}
