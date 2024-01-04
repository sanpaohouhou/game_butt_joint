package com.tl.tgGame.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.tl.tgGame.project.enums.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 余额变动记录表
 * </p>
 *
 * @author hd
 * @since 2020-12-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyLog extends Model<CurrencyLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    private Long uid;
    /**
     * 类型
     */
    private UserType userType;
    /**
     * 业务
     */
    private BusinessEnum business;
    /**
     * 订单号
     */
    private String sn;

    /**
     * 记录类型
     */
    private CurrencyLogType logType;

    /**
     * 余额变动描述
     */
    private String des;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 总余额
     */
    private BigDecimal balance;

    /**
     * 冻结余额
     */
    private BigDecimal freeze;

    /**
     * 剩余余额
     */
    private BigDecimal remain;

    @TableField(exist = false)
    private String fromAddress;

    @TableField(exist = false)
    private String toAddress;

    @TableField(exist = false)
    private Network network;

    @TableField(exist = false)
    private String hash;

    @TableField(exist = false)
    private WithdrawStatus status;

    @TableField(exist = false)
    private String gameAccount;

}
