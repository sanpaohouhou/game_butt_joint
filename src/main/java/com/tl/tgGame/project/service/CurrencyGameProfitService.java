package com.tl.tgGame.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tl.tgGame.admin.dto.AdminGameReq;
import com.tl.tgGame.project.dto.GameBackWaterRes;
import com.tl.tgGame.project.entity.CurrencyGameProfit;

import java.math.BigDecimal;
import java.util.List;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/12 , 19:34
 */
public interface CurrencyGameProfitService extends IService<CurrencyGameProfit> {

    CurrencyGameProfit getOrCreate(Long userId,String gameBusiness);

    List<CurrencyGameProfit> queryByUserId(Long userId);

    void increase(Long userId,String gameBusiness,BigDecimal amount);

    void withdrawal(Long userId, String gameBusiness, BigDecimal amount);

    GameBackWaterRes juniorGameBackWaterStatistics(Long inviteUserId,Long userId);

    GameBackWaterRes userBackWater(Long userId,String gameBusiness);
}
