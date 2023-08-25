package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/19 , 14:04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiGameRecordListDTO {

    private String recordID;
    private String account;
    private Integer gameID;
    private Integer gametype;
    private Double bet;
    private Double winlose;
    private Double prize;
    private Double refund;
    private Double validBet;
    private Double commission;
    private Integer jpmode;
    private Double jppoints;
    private Double jptax;
    private Double before;
    private Double after;
    private String bdate;
    private Boolean isBuyFeature;
}
