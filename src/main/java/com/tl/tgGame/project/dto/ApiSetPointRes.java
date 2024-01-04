package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/21 , 16:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiSetPointRes {

    private Integer Result;
    private Long BankID;
    private String TrsID;
    private Double AfterPoint;
    private Double Points;
}
