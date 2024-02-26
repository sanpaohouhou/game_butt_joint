package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2024/1/23 , 17:30
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiPgBetHistoryDTO {

    //子投注的唯一标识符 （唯一键值）
    private Long betId;
    //母注单的唯一标识符
    private Long parentBetId;
    //玩家的唯一标识符
    private String playerName;
    //记录中玩家使用的货币
    private String currency;
    //游戏的唯一标识符
    private Integer gameId;
    //投注记录平台
    private Integer platform;
    //投注记录类别：1: 真实游戏
    private Integer betType;
    //交易类别：
    //0：调整后的投注（重置游戏状态）
    //1: 现金
    //2: 红利
    //3: 免费游戏
    private Integer transactionType;
    //玩家的投注额
    private BigDecimal betAmount;
    //玩家的所赢金额
    private BigDecimal winAmount;
    //玩家的奖池返还率贡献额
    private BigDecimal jackpotRtpContributionAmount;
    //玩家的奖池贡献额
    private BigDecimal jackpotContributionAmount;
    //玩家的奖池金额
    private BigDecimal jackpotWinAMount;
    //玩家交易前的余额
    private BigDecimal balanceBefore;
    //玩家交易后的余额
    private BigDecimal balanceAfter;
    //投注状态：
    //1: 非最后一手投注
    //2：最后一手投注
    //3：已调整
    private Integer handsStatus;
    //数据更新时间
    //（以毫秒为单位的 Unix 时间戳）
    private Long rowVersion;
    //当前投注的开始时间
    //（以毫秒为单位的 Unix 时间戳）
    private Integer betTime;
    //当前投注的结束时间
    //（以毫秒为单位的 Unix 时间戳）
    private Integer betEndTime;
    //表示旋转类型：
    //True：奖金游戏
    //False：普通旋转
    private Boolean isFeatureBuy;
}
