package com.tl.tgGame.admin.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/16 , 11:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserExtendRes {

    /**
     * 用户id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    /**
     * 游戏账号
     */
    private String gameAccount;
    /**
     * 推广链接
     */
    private String extendUrl;
    /**
     * 推广总人数
     */
    private Integer allExtendNumber;
    /**
     * 当日转码数
     */
    private Integer dayExchangeNumber;
    /**
     * 已结算用尽
     */
    private BigDecimal settledCommission;
}
