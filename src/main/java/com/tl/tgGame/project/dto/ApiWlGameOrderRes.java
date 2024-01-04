package com.tl.tgGame.project.dto;

import lombok.Data;

/**
 * @author yj
 * @date 2023/3/2 15:14
 */
@Data
public class ApiWlGameOrderRes {

    private String orderId;

    private Integer status;

    private String reason;

    private String balance;

    private String gameUrl;

    private String transferable;
}
