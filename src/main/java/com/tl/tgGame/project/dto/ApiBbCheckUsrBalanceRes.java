package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2024/1/18 , 17:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiBbCheckUsrBalanceRes {

    private String LoginName;
    private String Currency;
    private BigDecimal Balance;
}
