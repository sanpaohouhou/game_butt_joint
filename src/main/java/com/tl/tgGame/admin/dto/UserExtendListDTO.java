package com.tl.tgGame.admin.dto;

import com.tl.tgGame.project.entity.User;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/13 , 17:10
 */
@Data
public class UserExtendListDTO extends User {

    /**
     * 邀请url
     */
    private String inviteUrl;
    /**
     * 下级人数
     */
    private Integer inviteNumber;
    /**
     *佣金
     */
    private BigDecimal commission;
    /**
     *待返水金额
     */
    private BigDecimal waitBackWaterAmount;
    /**
     *已返水金额
     */
    private BigDecimal backWaterAmount;
    /**
     *客损(下级用户总输的金额)
     */
    private BigDecimal loseAmount;
    /**
     *充值金额
     */
    private BigDecimal rechargeAmount;
    /**
     *充值人数
     */
    private Integer rechargeNumber;
    /**
     *提现金额
     */
    private Integer withdrawalNumber;

}
