package com.tl.tgGame.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tl.tgGame.project.entity.UserCommission;
import com.tl.tgGame.project.enums.UserCommissionType;
import com.tl.tgGame.project.mapper.UserCommissionMapper;
import com.tl.tgGame.project.service.UserCommissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/18 , 11:33
 */
@Slf4j
@Service
public class UserCommissionServiceImpl extends ServiceImpl<UserCommissionMapper, UserCommission> implements UserCommissionService {

    @Autowired
    private UserCommissionMapper userCommissionMapper;


    @Override
    public BigDecimal sumAmount(Long userId, UserCommissionType type, String gameBusiness, LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<UserCommission> wrapper = new LambdaQueryWrapper<UserCommission>().eq(UserCommission::getUserId, userId)
                .eq(Objects.nonNull(type) , UserCommission::getType, type)
                .eq(Objects.nonNull(gameBusiness),UserCommission::getGameBusiness,gameBusiness)
                .ge(Objects.nonNull(startTime),UserCommission::getCreateTime,startTime)
                .le(Objects.nonNull(endTime),UserCommission::getCreateTime,endTime);
        return userCommissionMapper.sumAmount(wrapper);
    }

    @Override
    public Boolean insertUserCommission(Long userId,Long fromUserId, Long gameId, String gameName, UserCommissionType type,String gameBusiness, BigDecimal profit, BigDecimal rate, BigDecimal actualAmount) {
        UserCommission commission = UserCommission.builder()
                .actualAmount(actualAmount)
                .fromUserId(fromUserId)
                .createTime(LocalDateTime.now())
                .rate(rate)
                .profit(profit)
                .gameId(gameId)
                .gameName(gameName)
                .gameBusiness(gameBusiness)
                .type(type)
                .userId(userId)
                .build();
        return save(commission);
    }
}
