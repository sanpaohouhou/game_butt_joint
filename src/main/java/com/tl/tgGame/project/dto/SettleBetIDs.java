package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/7 , 17:27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettleBetIDs {

    /**
     * 投注编号
     */
    private String BetID;
    /**
     * 下注金额
     */
    private Double bet;
    /**
     * 有效投注
     */
    private Double validBet;
    /**
     * 派彩金额
     */
    private Double win;
    /**
     * 净输赢金额
     */
    private Double winLose;
}
