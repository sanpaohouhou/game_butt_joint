package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2024/1/10 , 14:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiBBGetDemoUrlRes {

    private Integer game_type;
    private String url;
}
