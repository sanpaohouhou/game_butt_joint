package com.tl.tgGame.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/14 , 15:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRes {

    private String withdrawalUrl;

    private BigDecimal rechargeAmount = BigDecimal.ZERO;

    private BigDecimal withdrawalAmount= BigDecimal.ZERO;

    private Integer betNumber = 0;

    private BigDecimal betAmount = BigDecimal.ZERO;

    private BigDecimal profit = BigDecimal.ZERO;

    private BigDecimal validAmount = BigDecimal.ZERO;

    private BigDecimal backWaterAmount = BigDecimal.ZERO;
}
