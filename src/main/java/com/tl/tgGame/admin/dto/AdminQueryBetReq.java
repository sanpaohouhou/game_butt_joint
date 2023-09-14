package com.tl.tgGame.admin.dto;

import com.tl.tgGame.common.dto.PageQueryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/13 , 10:18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminQueryBetReq extends PageQueryDTO {

    private Long agentUserId;

    private Long userId;
}
