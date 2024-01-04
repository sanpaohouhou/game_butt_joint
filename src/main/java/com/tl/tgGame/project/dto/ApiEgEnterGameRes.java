package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/30 , 09:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiEgEnterGameRes {

    private String code;

    private String url;
}
