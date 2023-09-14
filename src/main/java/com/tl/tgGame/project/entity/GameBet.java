package com.tl.tgGame.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @date 2023/9/6 , 11:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class
GameBet {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     *游戏id
     */
    private String gameId;
    /**
     *游戏名
     */
    private String gameName;
    /**
     *游戏渠道
     */
    private String gameBusiness;
    /**
     *下注金额
     */
    private BigDecimal bet;
    /**
     *有效下注金额
     */
    private BigDecimal validBet;
    /**
     *实际盈利
     */
    private BigDecimal profit;
    /**
     *部分渠道需要如wl,
     */
    private BigDecimal tax;
    /**
     *待返水金额
     */
    private BigDecimal backWaterAmount;
    /**
     *上级返佣金额
     */
    private BigDecimal topCommission;
    /**
     *记录id,唯一
     */
    private String recordId;
    /**
     *游戏账户
     */
    private String gameAccount;
    /**
     *是否结算
     */
    private Boolean hasSettled;
    /**
     * 游戏房记录创建时间
     */
    private LocalDateTime recordTime;
    /**
     *创建时间
     */
    private LocalDateTime createTime;
    /**
     *更新时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private Integer level;

    @TableField(exist = false)
    private BigDecimal dividendAmount = BigDecimal.ZERO;


}
