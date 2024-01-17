package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2024/1/12 , 14:35
 * bb视讯
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiBbSXGameUrlRes {

    private String pc;

    private String mobile;

    private String rwd;
}
