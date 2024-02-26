package com.tl.tgGame.project.dto;

import lombok.Data;

import java.util.List;

/**
 * @version 1.0
 * @auther w
 * @date 2024/2/22 , 15:23
 */
@Data
public class ApiAgJdbGameListPageRes {

    private Integer totalCount;
    private Integer currentPage;
    private Integer perPage;
    private List<ApiAgJdbGameListRes> list;
}
