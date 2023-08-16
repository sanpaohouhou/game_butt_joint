package com.tl.tgGame.project.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/7 , 14:03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfit {

    private Long id;

    private Long userId;

    private String gameAccount;

    private String gameName;

    private BigDecimal backWaterRate;

    private BigDecimal completeBackWater;

    private BigDecimal waitBackWater;

    private BigDecimal topCommissionRate;

    private BigDecimal topCommission;

    private LocalDateTime createTime;

}
