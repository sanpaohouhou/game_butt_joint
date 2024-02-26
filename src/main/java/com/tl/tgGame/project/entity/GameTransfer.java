package com.tl.tgGame.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @version 1.0
 * @auther w
 * @date 2024/1/23 , 16:49
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameTransfer {

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String gameAccount;

    private Long userId;

    private BigDecimal beforeAmount;

    private BigDecimal amount;

    private BigDecimal afterAmount;

    private String sn;

    private String remark;

    private String business;

    private LocalDateTime createTime;
}
