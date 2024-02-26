package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @auther w
 * @date 2024/1/23 , 15:26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiPgUserWalletDTO {

    //玩家选择的币种
    private String currencyCode;
    //所有玩家钱包余额的总和
    private BigDecimal totalBalance;
    //玩家的现金钱包余额
    private BigDecimal cashBalance;
    private BigDecimal totalBonusBalance;
    private BigDecimal freeGameBalance;
    private List<ApiPgBonusesDTO> bonuses;
    private List<ApiPgBonusesDTO> freeGames;
    private Map<String,Object> cashWallet;
    private Map<String ,Object> bonusWallet;
    private Map<String,Object> freeGameWallet;
}
