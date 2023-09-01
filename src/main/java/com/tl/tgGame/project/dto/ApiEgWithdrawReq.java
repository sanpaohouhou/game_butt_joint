package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/28 , 20:17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiEgWithdrawReq {

    private String merch;
    private String playerId;
    private String transactionId;
    private String takeAll;
    private String amount;
}

