package com.tl.tgGame.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @date 2023/8/30 , 13:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EgBet {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private LocalDateTime pullTime;
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

    private String roundId;
    private String gameId;
    private String gameName;
    private String playerId;
    private String betTime;
    private String winTime;
    private String wpTime;
    private String currency;
    private String betType;
    private String extra;
    private String bet;
    private String win;
    private String netWin;
    private String roundCode;
    private String beforeBalance;
    private String afterBalance;
}
