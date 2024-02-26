package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2024/2/18 , 14:37
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiAgJdbMemberRes {

    private String player_name;
    private String member_code;
    private String currency;
    private String status;
    private Long created_time;
    private Long updated_time;
}
