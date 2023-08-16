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

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String gameName;

    private BigDecimal backWaterRate;

    private BigDecimal topCommissionRate;

    @TableField(exist = false)
    private Integer userRegisterCount = 0;
    @TableField(exist = false)
    private Integer userMonthRegisterCount = 0;
    @TableField(exist = false)
    private Integer userDayRegisterCount = 0;
    @TableField(exist = false)
    private BigDecimal userBetAllAmount = BigDecimal.ZERO;
}
