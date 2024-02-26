package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2024/2/18 , 15:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiAgJdbQueryOrderRes {

    private String currency;
    private String player_name;
    private String wallet_code;
    private BigDecimal before_balance;
    private BigDecimal amount;
    private BigDecimal balance;
    private String trans_type;
    private String trace_id;
    private Long created_time;
}
