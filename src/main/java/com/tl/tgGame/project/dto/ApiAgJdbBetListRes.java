package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2024/2/18 , 15:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiAgJdbBetListRes {
    //编号
    private String _id;
    //注单流水号
    private Integer row_version;
    //会员账号
    private String player_name;
    //游戏平台代码
    private String vendor_code;
    //游戏代码
    private String game_code;
    //游戏类型
    private String game_type;
    //下注金额
    private BigDecimal bet_amount;
    //派彩金额
    private BigDecimal win_amount;
    //输赢金额
    private BigDecimal wl_amount;
    //相同betId的第一笔投注单
    private Integer first_bet;
    //转账钱包代码
    private String wallet_code;
    //游玩时间
    private Long game_time;
    //交易类型
    private String trans_type;
    //投注单号
    private String bet_id;
    //父投注单号
    private String parent_bet_id;
    //交易单号
    private String trace_id;
    //注单生成时间
    private Long created_time;
}
