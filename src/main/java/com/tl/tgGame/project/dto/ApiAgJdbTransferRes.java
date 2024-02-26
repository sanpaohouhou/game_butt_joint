package com.tl.tgGame.project.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2024/2/18 , 14:58
 */
@Data
public class ApiAgJdbTransferRes {

    private String currency;
    private String trace_id;
    private String player_name;
    private String wallet_code;
    private BigDecimal amount;
    private BigDecimal balance;
}
