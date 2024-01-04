package com.tl.tgGame.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/14 , 14:04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserExtendJuniorDTO {

    private Long userId;
    private String gameAccount;
    private BigDecimal withdrawalAmount;
    private BigDecimal rechargeAmount;
    private Integer betNumber;
    private BigDecimal validAmount;
    private BigDecimal betAmount;
    private BigDecimal profit;
    private BigDecimal backWaterAmount;
}
