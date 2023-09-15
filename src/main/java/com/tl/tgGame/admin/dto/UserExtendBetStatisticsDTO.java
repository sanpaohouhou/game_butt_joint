package com.tl.tgGame.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/14 , 10:15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserExtendBetStatisticsDTO {

    private BigDecimal allTopCommission = BigDecimal.ZERO;

    private BigDecimal allProfit = BigDecimal.ZERO;
}
