package com.tl.tgGame.admin.dto;

import com.tl.tgGame.common.dto.PageQueryDTO;
import com.tl.tgGame.project.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 17:10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserRechargeReq extends PageQueryDTO {

    private Long userId;

    private String gameAccount;

    private Long agentId;

    private UserType userType;
}
