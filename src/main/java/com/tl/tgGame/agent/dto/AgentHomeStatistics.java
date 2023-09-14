package com.tl.tgGame.agent.dto;

import com.tl.tgGame.project.entity.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/11 , 16:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgentHomeStatistics {

    private Currency currency;

    private BigDecimal dividendProfit;

    private Integer teamNumber;

    private String inviteUrl;

    private String address;
}
