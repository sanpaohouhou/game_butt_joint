package com.tl.tgGame.project.dto;

import lombok.Data;

/**
 * @author yj
 * @date 2023/3/2 15:13
 */
@Data
public class ApiWlGameRes {

    private Integer code;

    private ApiWlGameOrderRes data;

    private String msg;

    private String orderId;
}
