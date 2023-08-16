package com.tl.tgGame.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tl.tgGame.project.enums.Network;
import com.tl.tgGame.project.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户充值提现记录表
 * </p>
 *
 * @author baomidou
 * @since 2022-07-12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recharge implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 类型
     */
    private UserType userType;

    /**
     * 地址
     */
    private String fromAddress;

    /**
     * 到哪个地址
     */
    private String toAddress;

    /**
     * hash
     */
    private String hash;

    /**
     * 网络
     */
    private Network network;

    /**
     * 截图
     */
    private String screen;

    private String note;

    /**
     * 时间
     */
    private LocalDateTime createTime;
}
