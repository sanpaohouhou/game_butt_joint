package com.tl.tgGame.admin.dto;

import com.tl.tgGame.common.dto.PageQueryDTO;
import com.tl.tgGame.project.enums.WithdrawStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 17:26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserWithdrawalReq extends PageQueryDTO {

    private Long userId;

    private WithdrawStatus status;
}
