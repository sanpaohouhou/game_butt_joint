package com.tl.tgGame.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 16:34
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminGameBetStatistics {

    private String gameName ;

    private Integer userCount = 0;
}
