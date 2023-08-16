package com.tl.tgGame.project.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tl.tgGame.project.enums.Network;
import com.tl.tgGame.project.enums.UserType;
import com.tl.tgGame.project.enums.WithdrawStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * usdt提现记录表
 * </p>
 *
 * @author baomidou
 * @since 2022-07-12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Withdrawal implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 实际到账
     */
    private BigDecimal actualAmount;

    /**
     * 地址
     */
    private String fromAddress;

    /**
     * 地址
     */
    private String toAddress;

    /**
     * 网络
     */
    private Network network;

    /**
     * 交易Hash
     */
    private String hash;

    /**
     * 交易Hash
     */
    private String screen;

    /**
     * 时间
     */
    private LocalDateTime createTime;

    /**
     * 完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 状态
     */
    private WithdrawStatus status;

    /**
     * 审核备注
     */
    private String note;

    /**
     * 提现用户类型
     * user,merchant,business
     */
    private UserType userType;

}
