package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2024/1/15 , 10:10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiBbRecordBy3Res {

    private String UserName;
    private String WagersID;
    private String WagersDate;
    private Integer SerialID;
    private String RoundNo;
    private Integer GameType;
    private String WagerDetail;
    private Integer GameCode;
    //注单结果(C:注销,X:未结算,W:赢,L:输)
    private String Result;
    private Integer ResultType;
    private String Card;
    private BigDecimal BetAmount;
    //派彩金额(不包含本金)
    private BigDecimal Payoff;
    private String Currency;
    private BigDecimal ExchangeRate;
    //会员有效投注额
    private BigDecimal Commissionable;
    private String Origin;
    //注单变更时间
    private String ModifiedDate;
    private String Client;
    private String Portal;
}
