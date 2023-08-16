package com.tl.tgGame.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/16 , 11:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserExtendInfoRes {

    /**
     * 时间
     */
    private LocalDate time;
    /**
     * 推广人数
     */
    private Integer extendNumber;
    /**
     * 推广佣金
     */
    private BigDecimal extendCommission;
}
