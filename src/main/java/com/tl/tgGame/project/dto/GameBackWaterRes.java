package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/13 , 16:41
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameBackWaterRes {

    private String gameBusiness;

    private BigDecimal allWaitBackWater;

    private BigDecimal allBackWater;
}
