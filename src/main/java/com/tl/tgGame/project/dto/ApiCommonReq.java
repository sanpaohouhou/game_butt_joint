package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/9 , 16:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiCommonReq {

    private String Params;

    private String ReParams;

    private String AgentKey;

}
