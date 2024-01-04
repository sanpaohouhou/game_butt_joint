package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/11 , 18:24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiGetSingleBillReq {

    private Long BankID;

    private String TrsID;
}
