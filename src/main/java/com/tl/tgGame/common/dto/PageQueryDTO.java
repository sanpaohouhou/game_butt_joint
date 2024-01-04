package com.tl.tgGame.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
