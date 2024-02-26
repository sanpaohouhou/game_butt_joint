package com.tl.tgGame.project.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2024/2/18 , 14:53
 */
@Data
public class ApiAgJdbBalanceRes {
    private String player_name;
    private String wallet_code;
    private BigDecimal balance;
    private String currency;
}
