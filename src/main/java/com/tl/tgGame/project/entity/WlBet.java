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
 * @date 2023/3/22 , 下午2:33
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WlBet {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户账号id
     */
    private Long userId;
    /**
     *游戏类型
     */
    private Integer game;
    /**
     *游戏分类
     */
    private Integer category;
    /**
     *系统实际盈利
     */
    private BigDecimal profit;
    /**
     *结算时用户的最新余额
     */
    private BigDecimal balance;
    /**
     *投注额
     */
    private BigDecimal bet;
    /**
     *有效投注额
     */
    private BigDecimal validBet;
    /**
     *游戏税收
     */
    private BigDecimal tax;
    /**
     *游戏开始时间
     */
    private LocalDateTime gameStartTime;
    /**
     *数据记录时间
     */
    private LocalDateTime recordTime;
    /**
     *投注单号
     */
    private String gameId;
    private String gameName;
    /**
     *记录全局唯一ID
     */
    private String recordId;
    /**
     *投注详情查询URl链接
     */
    private String detailUrl;

    private LocalDateTime createTime;

    private LocalDateTime pullTime;

    /**
     * 该记录是否操作过分钱
     */
    private Boolean hasSettled;

    private LocalDateTime updateTime;
    /**
     * 返水金额
     */
    private BigDecimal backWaterAmount;
    /**
     * 上级返佣
     */
    private BigDecimal topCommission;


}
