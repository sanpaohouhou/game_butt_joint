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
 * @date 2023/8/4 , 17:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String gameName;

    private BigDecimal backWaterRate;

    private BigDecimal topCommissionRate;

    private Boolean status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
//
//    @TableField(exist = false)
//    private Integer userRegisterCount = 0;
//    @TableField(exist = false)
//    private Integer userMonthRegisterCount = 0;
//    @TableField(exist = false)
//    private Integer userDayRegisterCount = 0;
//    @TableField(exist = false)
//    private BigDecimal userBetAllAmount = BigDecimal.ZERO;

    /**
     * 下注次数
     */
    @TableField(exist = false)
    private Integer betNumber = 0;
    /**
     * 投注额
     */
    @TableField(exist = false)
    private BigDecimal betAmount = BigDecimal.ZERO;
    /**
     * 玩家盈亏
     */
    @TableField(exist = false)
    private BigDecimal userProfit = BigDecimal.ZERO;
    /**
     * 有效金额
     */
    @TableField(exist = false)
    private BigDecimal validAmount = BigDecimal.ZERO;
    /**
     * 已返水
     */
    @TableField(exist = false)
    private BigDecimal backWaterAmount = BigDecimal.ZERO;
    /**
     * 用户佣金
     */
    @TableField(exist = false)
    private BigDecimal userCommission = BigDecimal.ZERO;


}
