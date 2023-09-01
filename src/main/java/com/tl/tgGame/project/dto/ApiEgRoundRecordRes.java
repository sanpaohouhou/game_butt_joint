package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/30 , 16:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiEgRoundRecordRes {
    private String code;

    private String page;

    private String pageSize;

    private List<ApiEgGameRecordRes> data;
}
