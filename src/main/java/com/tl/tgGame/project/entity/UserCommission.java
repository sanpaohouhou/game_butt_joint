package com.tl.tgGame.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tl.tgGame.project.enums.UserCommissionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/18 , 10:45
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCommission {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private Long userId;

    private Long fromUserId;

    private Long betId;

    private String gameId;

    private String gameName;
    //利润类型,佣金,返水,分红
    private UserCommissionType type;

    //游戏类型 目前有FC电子,瓦力,EG
    private String gameBusiness;

    private BigDecimal profit;

    private BigDecimal rate;

    private BigDecimal actualAmount;

    private LocalDateTime createTime;
}
