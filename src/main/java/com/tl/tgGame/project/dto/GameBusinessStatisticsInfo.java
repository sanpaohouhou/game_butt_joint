package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/23 , 11:46
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameBusinessStatisticsInfo {

    private String gameBusiness;

    private String backWaterRate;

    private BigDecimal backWater;

    private BigDecimal waitBackWater;

    private String juniorCommissionRate;

    private BigDecimal juniorCommission;
}
