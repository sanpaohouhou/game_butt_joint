package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2024/2/18 , 14:34
 */
@Data

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgJdbResponse {

    private Integer status;
    private String message;
    private Object data;
}
