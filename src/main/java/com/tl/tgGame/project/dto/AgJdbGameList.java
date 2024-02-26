package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2024/2/22 , 14:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgJdbGameList {

    private String gameCode;
    private String name;
    private String gameType;
    private String image;
}
