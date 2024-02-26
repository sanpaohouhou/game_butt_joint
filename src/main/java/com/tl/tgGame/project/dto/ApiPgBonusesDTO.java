package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @version 1.0
 * @auther w
 * @date 2024/1/23 , 15:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiPgBonusesDTO {

    private String key;
    private Integer bonusId;
    private String bonusName;
    private String transactionId;
    private String bonusParentType;
    private List<Integer> gameIds;
    private Integer gameIdLock;
    private BigDecimal balanceAmount;
    private BigDecimal bonusRationAmount;
    private BigDecimal minimumConversionAmount;
    private BigDecimal maximumConversionAmount;
    private Integer status;
    private Long createdDate;
    private Long expiredDate;
    private String createdBy;
    private String updatedBy;
    private Boolean isSuppressDiscard;

    private Integer freeGameId;
    private String freeGameName;
    private Integer gameCount;
    private Integer totalGame;
    private Integer multiplier;
    private BigDecimal coinSize;
    private char conversionType;
}
