package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/30 , 16:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiEgGameRecordRes {

    private String roundId;
    private String gameId;
    private String playerId;
    private String betTime;
    private String winTime;
    private String wpTime;
    private String currency;
    private String betType;
    private Object extra;
    private String bet;
    private String win;
    private String netWin;
    private String roundCode;
    private String beforeBalance;
    private String afterBalance;

}
