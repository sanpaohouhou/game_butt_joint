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

    private Integer joinedUser;
    private Integer rechargeUser;
    private Integer betUser;
}
