package com.tl.tgGame.address.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.tl.tgGame.project.enums.Network;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 地址余额
 * </p>
 *
 * @author hd
 * @since 2022-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressBalance {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 账户
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 地址
     */
    private String address;

    /**
     * 网络
     */
    private Network network;

    /**
     * 余额
     */
    private BigDecimal amount;

    /**
     * 状态值; 0:待归集, 1:正在归集
     */
    private Integer status;

}
