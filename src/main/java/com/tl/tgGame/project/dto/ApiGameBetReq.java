package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 10:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiGameBetReq {
    /**
     * 玩家账号
     */
    private String MemberAccount;
    /**
     * 币别
     */
    private String Currency;
    /**
     * 游戏编号
     */
    private String GameID;
    /**
     * 游戏记录编号(唯一码,长度24码)
     */
    private String RecordID;
    /**
     * 游戏类型
     */
    private Integer GameType;
    /**
     * 下注金额
     */
    private Double Bet;
    /**
     * 交易建立时间
     */
    private LocalDateTime CreateDate;
    /**
     * 下注编号(唯一码,长度24码)
     */
    private String BetID;

}
