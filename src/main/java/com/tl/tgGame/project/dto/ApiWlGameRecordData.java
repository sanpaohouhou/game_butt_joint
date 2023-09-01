package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/3/22 , 下午4:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiWlGameRecordData {

    private String from;

    private String until;

    private Integer count;

    private Boolean hasMore;

    private ApiWlGameRecordRes list;
}
