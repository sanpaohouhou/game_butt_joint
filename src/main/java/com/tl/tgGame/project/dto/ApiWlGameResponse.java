package com.tl.tgGame.project.dto;

import lombok.Data;

/**
 * @version 1.0
 * @auther w
 * @date 2023/3/22 , 下午9:10
 */
@Data
public class ApiWlGameResponse {

    private Integer code;

    private ApiWlGameRecordData data;

    private String msg;
}
