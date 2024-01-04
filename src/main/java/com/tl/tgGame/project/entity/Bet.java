package com.tl.tgGame.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/7 , 15:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bet {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 游戏记录编号(唯一码,长度24码)
     */
    private String recordId;

    /**
     * 玩家账号
     */
    private String gameAccount;
    /**
     * 游戏编号
     */
    private Integer gameId;

    /**
     * 游戏类型
     */
    private Integer gameType;

    private String gameName;
    /**
     * 下注金额
     */
    private BigDecimal bet;
    /**
     * 净输赢
     */
    private BigDecimal winLose;
    /**
     * 赢分
     */
    private BigDecimal prize;
    /**
     * 退还金额
     */
    private BigDecimal refund;
    /**
     * 有效投注
     */
    private BigDecimal validBet;

    /**
     * 抽水金额
     */
    private BigDecimal commission;
    /**
     * Jackpot 种类
     */
    private Integer jpMode;
    /**
     * Jackpot 中奖金额
     */
    private BigDecimal jpPoints;
    /**
     * Jackpot 抽水 (支持到小数第六位)
     */
    private BigDecimal jpTax;
    /**
     * 下注前点数
     */
    private BigDecimal befores;
    /**
     * 下注后点数
     */
    private BigDecimal afters;
    /**
     * 建立事件
     */
    private LocalDateTime bDate;
    /**
     * 是否购买免费游戏
     */
    private Boolean isBuyFeature;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 是否结算
     */
    private Boolean hasSettled;
    /**
     * 返水金额
     */
    private BigDecimal backWaterAmount;
    /**
     * 上级返佣
     */
    private BigDecimal topCommission;
    /**
     * 创建事件
     */
    private LocalDateTime createTime;
    /**
     * 更新事件
     */
    private LocalDateTime updateTime;
    /**
     * 记录拉取事件
     */
    private LocalDateTime pullTime;
    /**
     * 总返水金额
     */
    @TableField(exist = false)
    private BigDecimal allBackWaterAmount;
}
