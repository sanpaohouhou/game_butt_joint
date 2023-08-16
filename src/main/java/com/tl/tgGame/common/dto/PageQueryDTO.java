package com.tl.tgGame.common.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @version 1.0
 * @auther w
 * @date 2023/7/11 , 上午10:05
 */
@Data
public class PageQueryDTO {

    private Integer page = 1;

    private Integer size = 10;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
