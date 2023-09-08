package com.tl.tgGame.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tl.tgGame.project.entity.Bet;
import com.tl.tgGame.project.entity.GameBet;
import com.tl.tgGame.project.enums.GameBusiness;
import com.tl.tgGame.project.mapper.GameBetMapper;
import com.tl.tgGame.project.service.GameBetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/6 , 11:28
 */
@Slf4j
@Service
public class GameBetServiceImpl extends ServiceImpl<GameBetMapper, GameBet> implements GameBetService {

    @Autowired
    private GameBetMapper gameBetMapper;

    @Override
    public BigDecimal sumAmount(Long userId, Boolean hasSettled, String business) {
        LambdaQueryWrapper<GameBet> wrapper = new LambdaQueryWrapper<GameBet>()
                .eq(GameBet::getUserId, userId)
                .eq(Objects.nonNull(business), GameBet::getGameBusiness,business)
                .eq(GameBet::getHasSettled, hasSettled);
        return gameBetMapper.sumAmount(wrapper);
    }

    @Override
    public BigDecimal sumBetAmount(Long userId, LocalDateTime startTime,LocalDateTime endTime,Boolean hasSettled) {
        LambdaQueryWrapper<GameBet> wrapper = new LambdaQueryWrapper<GameBet>()
                .eq(GameBet::getUserId, userId)
                .eq(Objects.nonNull(hasSettled),GameBet::getHasSettled,hasSettled)
                .ge(Objects.nonNull(startTime),GameBet::getRecordTime,startTime)
                .le(Objects.nonNull(endTime),GameBet::getRecordTime,endTime);
        return gameBetMapper.sumBetAmount(wrapper);
    }

    @Override
    public BigDecimal sumWinLose(Long userId, LocalDateTime startTime, LocalDateTime endTime
            ,Boolean hasWinLose,String gameBusiness) {
        LambdaQueryWrapper<GameBet> wrapper = new LambdaQueryWrapper<GameBet>()
                .eq(Objects.nonNull(userId), GameBet::getUserId, userId)
                .eq(!StringUtils.isEmpty(gameBusiness),GameBet::getGameBusiness,gameBusiness)
                .gt(hasWinLose != null && hasWinLose, GameBet::getProfit, 0)
                .lt(hasWinLose != null && !hasWinLose, GameBet::getProfit, 0)
                .ge(Objects.nonNull(startTime), GameBet::getRecordTime, startTime)
                .le(Objects.nonNull(endTime), GameBet::getRecordTime, endTime);
        return gameBetMapper.sumWinLose(wrapper);
    }

    @Override
    public Integer todayBetUserCount(LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<GameBet> wrapper = new LambdaQueryWrapper<GameBet>()
                .ge(Objects.nonNull(startTime), GameBet::getRecordTime, startTime)
                .le(Objects.nonNull(endTime), GameBet::getRecordTime, endTime);
        return gameBetMapper.todayBetUserCount(wrapper);
    }


}
