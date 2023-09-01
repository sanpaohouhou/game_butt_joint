package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/28 , 17:32
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiEgEnterGameReq {

    private String merch;

    private String playerId;

    private String gameId;

    private String lang;

    private String homeUrl;
}
