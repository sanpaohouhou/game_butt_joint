package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/19 , 14:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiGameRecordListRes {

    private Integer Result;

    private List<ApiGameRecordListDTO> Records;
}
