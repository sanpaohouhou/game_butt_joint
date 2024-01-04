package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/7 , 15:19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiEgLogoutReq {

    private String merch;

    private String playerId;
}
