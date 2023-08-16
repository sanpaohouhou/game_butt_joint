package com.tl.tgGame.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 10:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameBet {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * 玩家账号
     */
    private String memberAccount;
    /**
     * 币别
     */
    private String currency;
    /**
     * 游戏编号
     */
    private String gameId;
    /**
     * 发送请求当下的时间
     */
    private Long ts;

    /**
     * 游戏记录编号(唯一码,长度24码)
     */
    private String recordId;
    /**
     * 下注编号(唯一码,长度24码)
     */
    private String bankId;
    /**
     * 游戏类型
     */
    private Integer gameType;
    /**
     * 是否购买免费游戏
     */
    private Boolean isBuyFeature;
    /**
     * 押注金额
     */
    private Double bet;
    /**
     * 游戏赢分
     */
    private Double win;
    /**
     * 彩金押注金额
     */
    private Double jpBet;
    /**
     * 彩金赢分
     */
    private Double jpPrize;
    /**
     * 总输赢
     */
    private Double netWin;
    /**
     * 实际押注金额
     */
    private Double requireAmt;
    /**
     * 游戏时间 (年-月-日 时:分:秒)
     */
    private LocalDateTime gameDate;
    /**
     * 交易建立时间(年-月-日 时:分:秒)
     */
    private LocalDateTime createDate;

    /**
     * 是否结算,true已结算,false未结算
     */
    private Boolean hasSettled;

    private Long userId;

    /**
     * 返水金额
     */
    private BigDecimal backWaterAmount;
    /**
     * 上级返佣
     */
    private BigDecimal topCommission;
    /**
     * 总返水金额
     */
    private BigDecimal allBackWaterAmount;
    /**
     * 是否返水
     */
    private Boolean hasBackWater;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
