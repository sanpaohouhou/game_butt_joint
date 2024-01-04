package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 10:18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiGameBalanceReq {

    /**
     * 玩家账号
     */
    private String MemberAccount;
    /**
     * 币别
     */
    private String Currency;
    /**
     * 游戏编号
     */
    private String GameID;
    /**
     * 发送请求当下的时间
     */
    private Long Ts;
}
