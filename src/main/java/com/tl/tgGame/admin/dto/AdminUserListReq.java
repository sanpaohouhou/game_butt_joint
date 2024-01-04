package com.tl.tgGame.admin.dto;

import com.tl.tgGame.common.dto.PageQueryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 16:37
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserListReq extends PageQueryDTO {

    private String gameAccount;

    private Long partnerId;
}
