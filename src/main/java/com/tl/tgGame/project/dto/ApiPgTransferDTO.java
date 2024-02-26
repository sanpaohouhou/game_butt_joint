package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2024/1/23 , 16:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiPgTransferDTO {

    private String transactionId;
    private BigDecimal balanceAmountBefore;
    private BigDecimal balanceAmount;
    private BigDecimal amount;
}
