package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/14 , 10:11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiRecordListReq {

    private String StartDate;

    private String EndDate;
}
