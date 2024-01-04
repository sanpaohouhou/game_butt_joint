package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/29 , 15:57
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiEgDepositRes {

    private String code;
    private String currency;
    private String amount;
    private String beforeBalance;
    private String afterBalance;
}
