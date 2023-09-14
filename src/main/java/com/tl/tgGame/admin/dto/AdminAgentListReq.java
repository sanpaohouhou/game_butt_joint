package com.tl.tgGame.admin.dto;

import com.tl.tgGame.common.dto.PageQueryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/11 , 14:53
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminAgentListReq extends PageQueryDTO{

    private Long agentId;

    private String agentName;

    private Integer level = 1;
}
