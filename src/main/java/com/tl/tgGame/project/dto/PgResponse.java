package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2024/1/23 , 11:37
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PgResponse {

    private Object data;
    private String error;


    public class VerifySession{
        private String player_name;
        private String nickname;
        private String currency;
    }

}
