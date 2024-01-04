package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/28 , 20:29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiEgRoundRecordTimeReq {

    private String merch;
    private String playerId;
    private String gameId;
    private String roundCode;
    private String start;
    private String end;
    private String page;
    private String pageSize;
}
