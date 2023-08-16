package com.tl.tgGame.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 16:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserOverviewStatistics {

    /**
     * 全部注册用户/
     */
    private Integer registerUser = 0;
    /**
     * 全部充值用户
     */
    private BigDecimal rechargeAmount = BigDecimal.ZERO;

    private BigDecimal betAmount = BigDecimal.ZERO;
}
