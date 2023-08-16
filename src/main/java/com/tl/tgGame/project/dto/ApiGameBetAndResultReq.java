package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 10:19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiGameBetAndResultReq {


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
     * 是否购买免费游戏
     */
    private Boolean isBuyFeature;
    /**
     * 押注金额
     */
    private Double Bet;
    /**
     * 游戏赢分
     */
    private Double Win;
    /**
     * 彩金押注金额
     */
    private Double JPBet;
    /**
     * 彩金赢分
     */
    private Double JPPrize;
    /**
     * 总输赢
     */
    private Double NetWin;
    /**
     * 实际押注金额
     */
    private Double RequireAmt;
    /**
     * 游戏时间 (年-月-日 时:分:秒)
     */
    private LocalDateTime GameDate;
    /**
     * 交易建立时间(年-月-日 时:分:秒)
     */
    private LocalDateTime CreateDate;
}
