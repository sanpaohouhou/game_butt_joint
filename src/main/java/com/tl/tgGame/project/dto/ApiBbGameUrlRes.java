package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2024/1/12 , 14:19
 * bb捕鱼
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiBbGameUrlRes {

    private Integer gametype;

    private String html5;
}
