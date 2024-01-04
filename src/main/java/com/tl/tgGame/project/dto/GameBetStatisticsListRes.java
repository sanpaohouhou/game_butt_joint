package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/13 , 14:23
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameBetStatisticsListRes {

    private String gameBusiness;

    /**
     * 下注次数
     */
    private Integer betNumber = 0;
    /**
     * 投注额
     */
    private BigDecimal betAmount = BigDecimal.ZERO;
    /**
     * 玩家盈亏
     */
    private BigDecimal userProfit= BigDecimal.ZERO;
    /**
     * 有效金额
     */
    private BigDecimal validAmount= BigDecimal.ZERO;
    /**
     * 已返水
     */
    private BigDecimal backWaterAmount= BigDecimal.ZERO;
    /**
     * 用户佣金
     */
    private BigDecimal userCommission= BigDecimal.ZERO;
}
