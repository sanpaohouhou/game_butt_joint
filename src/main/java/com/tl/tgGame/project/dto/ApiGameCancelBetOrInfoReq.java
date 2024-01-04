package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 10:17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiGameCancelBetOrInfoReq {

    /**
     * 玩家账号
     */
    private String MemberAccount;
    /**
     * 币别
     */
    private String currency;
    /**
     * 游戏编号
     */
    private String gameID;
    /**
     * 发送请求当下的时间
     */
    private Long Ts;
    /**
     * 下注编号(唯一码,长度24码)
     */
    private String BankID;
}
