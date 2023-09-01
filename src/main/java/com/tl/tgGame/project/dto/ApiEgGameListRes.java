package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/29 , 16:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiEgGameListRes {

    private String code;

    private List<ApiEgGameNameRes> data;
}
