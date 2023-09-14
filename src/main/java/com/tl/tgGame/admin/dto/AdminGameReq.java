package com.tl.tgGame.admin.dto;

import com.tl.tgGame.common.dto.PageQueryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 17:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminGameReq extends PageQueryDTO {

    private String gameName;

    private Long userId;
}
