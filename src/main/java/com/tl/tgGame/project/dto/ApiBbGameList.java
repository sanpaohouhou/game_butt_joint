package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2024/1/15 , 17:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiBbGameList {

    private String id;
    private String image;
    private String name;
    private String gameId;
}
