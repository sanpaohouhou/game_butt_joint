package com.tl.tgGame.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.dto.GameBackWaterRes;
import com.tl.tgGame.project.entity.CurrencyGameProfit;
import com.tl.tgGame.project.mapper.CurrencyGameProfitMapper;
import com.tl.tgGame.project.service.CurrencyGameProfitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/12 , 19:34
 */
@Service
public class CurrencyGameProfitServiceImpl extends ServiceImpl<CurrencyGameProfitMapper, CurrencyGameProfit> implements CurrencyGameProfitService {


    @Override
    public CurrencyGameProfit getOrCreate(Long userId, String gameBusiness) {
        CurrencyGameProfit one = getOne(new LambdaQueryWrapper<CurrencyGameProfit>().eq(CurrencyGameProfit::getUserId, userId)
                .eq(CurrencyGameProfit::getGameBusiness, gameBusiness));
        if(one == null){
            one = new CurrencyGameProfit();
            one.setGameBusiness(gameBusiness);
            one.setUserId(userId);
            one.setBalance(BigDecimal.ZERO);
            one.setFreeze(BigDecimal.ZERO);
            one.setSettled(BigDecimal.ZERO);
            save(one);
        }
        return one;
    }

    @Override
    public List<CurrencyGameProfit> queryByUserId(Long userId) {
        return list(new LambdaQueryWrapper<CurrencyGameProfit>().eq(CurrencyGameProfit::getUserId, userId));
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void increase(Long userId, String gameBusiness, BigDecimal amount) {
        getOrCreate(userId, gameBusiness);
        long increase = getBaseMapper().increase(userId, gameBusiness, amount);
        if (increase <= 0L) ErrorEnum.CREDIT_LACK.throwException();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void withdrawal(Long userId, String gameBusiness, BigDecimal amount) {
        getOrCreate(userId, gameBusiness);
        long withdraw = getBaseMapper().withdraw(userId, gameBusiness, amount);
        if(withdraw <= 0L) ErrorEnum.CREDIT_LACK.throwException();
    }

    @Override
    public GameBackWaterRes gameBackWaterStatistics(Long inviteUserId,Long userId) {
        QueryWrapper<Object> wrapper = new QueryWrapper<>().eq(Objects.nonNull(inviteUserId), "u.invite_user", inviteUserId)
                .eq(Objects.nonNull(userId), "cgp.user_id", userId);
        return getBaseMapper().gameBackWaterStatistics(wrapper);
    }


}
