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
 * @date 2023/8/4 , 17:40
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long inviteUser;

    private LocalDateTime joinedTime;

    private Boolean isBot;

    private Long partnerId;

    private Long tgId;

    private String tgGroup;

    private String country;

    private String gameAccount;

    //ture有效,还在群里,false无效,未在群里
    private Boolean hasGroup;

    private String withdrawalUrl;

    private Boolean hasJoinEg;

    private String inviteChain;

    //是否是代理,true是,false不是
    private Boolean hasAgent;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long agentId;

    @TableField(exist = false)
    private Currency currency;

    @TableField(exist = false)
    private Integer todayBetCount;

    @TableField(exist = false)
    private BigDecimal todayProfit;

    @TableField(exist = false)
    private BigDecimal allProfit;

    @TableField(exist = false)
    private Integer allBetCount;

    @TableField(exist = false)
    private BigDecimal rechargeAmount;

    @TableField(exist = false)
    private BigDecimal withdrawalAmount;


}
