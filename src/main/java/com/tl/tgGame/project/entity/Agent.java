package com.tl.tgGame.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @date 2023/9/11 , 11:19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Agent {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String agentName;

    private String userName;

    private String mobile;

    private BigDecimal dividendRate;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long inviteId;

    private String inviteChain;

    private String password;

    private Boolean enabled;

    private Boolean hasDeleted;

    private Integer level;

    private String remark;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    private String gameAccount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String inviteUrl;

    @TableField(exist = false)
    private Currency currency;

    @TableField(exist = false)
    private BigDecimal dividendProfit;

    @TableField(exist = false)
    private Integer teamNumber;

    @TableField(exist = false)
    private Object address;
}
