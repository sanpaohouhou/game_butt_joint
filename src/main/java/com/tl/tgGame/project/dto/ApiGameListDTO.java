package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/31 , 11:16
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiGameListDTO {

    private String gameId;
    private String name;
    private String image;
    private String multiple;
    private String onlineNumber;
}
