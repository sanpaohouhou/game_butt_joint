package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2023/3/22 , 下午2:11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiWlGameRecordRes {

    /**
     * 用户账号
     */
    private Long[] uid;
    /**
     *游戏类型
     */
    private Integer[] game;
    /**
     *游戏分类
     */
    private Integer[] category;
    /**
     *系统实际盈利
     */
    private BigDecimal[] profit;
    /**
     *结算时用户的最新余额
     */
    private BigDecimal[] balance;
    /**
     *投注额
     */
    private BigDecimal[] bet;
    /**
     *有效投注额
     */
    private BigDecimal[] validBet;
    /**
     *游戏税收
     */
    private BigDecimal[] tax;
    /**
     *游戏开始时间
     */
    private String[] gameStartTime;
    /**
     *数据记录时间
     */
    private String[] recordTime;
    /**
     *投注单号
     */
    private String[] gameId;
    /**
     *记录全局唯一ID
     */
    private String[] recordId;
    /**
     *投注详情查询URl链接
     */
    private String[] detailUrl;
    /**
     * flag类型,只有体育有
     */
    private String[] flag;

}
