package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/7 , 16:34
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiGameCancelBetReq {

    private String BankID;

    private String currency;

    private String MemberAccount;

    private Integer gameID;

    private Long Ts;
}
