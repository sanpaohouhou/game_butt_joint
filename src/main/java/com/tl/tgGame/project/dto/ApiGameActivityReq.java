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
 * @date 2023/8/8 , 10:28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiGameActivityReq {

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
    /**
     * 下注金额
     */
    private Double Bet;
    /**
     * 赢分
     */
    private Double Win;
    /**
     * 净输赢
     */
    private Double NetWin;
    /**
     * 游戏时间(年-月-日 时:分:秒)
     */
    private LocalDateTime GameDate;
    /**
     * 交易建立时间(年-月-日 时:分:秒)
     */
    private LocalDateTime CreateDate;
    /**
     * 要返还给玩家的金额(永远为正数)
     */
    private Double Refund;
    /**
     * 有效投注
     */
    private Double ValidBet;
    /**
     * 抽水金额
     */
    private Double Commission;
    /**
     * 此次结算了那些下注;对应至下注的BET_ID
     */
    private List<SettleBetIDs> SettleBetIDs;
}
