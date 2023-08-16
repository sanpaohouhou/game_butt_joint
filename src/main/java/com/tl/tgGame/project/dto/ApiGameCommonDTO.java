package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/7 , 17:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiGameCommonDTO {

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
     * 发送请求当下的时间
     */
    private Long Ts;

    /**
     * 游戏记录编号(唯一码,长度24码)
     */
    private String RecordID;
    /**
     * 下注编号(唯一码,长度24码)
     */
    private String BankID;
    /**
     * 游戏类型
     */
    private Integer GameType;

    private Boolean isBuyFeature;

    private Double Bet;

    private Double Win;

    private Double JPBet;

    private Double JPPrize;

    private Double NetWin;

    private Double RequireAmt;

    private LocalDateTime GameDate;

    private LocalDateTime CreateDate;

    private String BetID;

    private Double ValidBet;

    private Double Commission;

    private List<SettleBetIDs> SettleBetIDs;


}
