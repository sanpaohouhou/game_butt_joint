package com.tl.tgGame.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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


    @Override
    public BigDecimal sumAmount(Long userId, UserCommissionType type, String gameBusiness, LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<UserCommission> wrapper = new LambdaQueryWrapper<UserCommission>()
                .eq(Objects.nonNull(userId),UserCommission::getUserId, userId)
                .eq(Objects.nonNull(type) , UserCommission::getType, type)
                .eq(Objects.nonNull(gameBusiness),UserCommission::getGameBusiness,gameBusiness)
                .ge(Objects.nonNull(startTime),UserCommission::getCreateTime,startTime)
                .le(Objects.nonNull(endTime),UserCommission::getCreateTime,endTime);
        return getBaseMapper().sumAmount(wrapper);
    }

    @Override
    public BigDecimal sumJuniorAmount(Long inviteUserId, Long userId, UserCommissionType type, String gameBusiness, LocalDateTime startTime, LocalDateTime endTime) {
        QueryWrapper<Object> wrapper = new QueryWrapper<>().eq("u.invite_user", inviteUserId)
                .eq(Objects.nonNull(userId), "uc.user_id", userId)
                .eq(Objects.nonNull(type), "uc.type", type)
                .eq(Objects.nonNull(gameBusiness), "uc.game_business", gameBusiness)
                .ge(Objects.nonNull(startTime), "uc.create_time", startTime)
                .le(Objects.nonNull(endTime), "uc.create_time", endTime);
        return getBaseMapper().sumJuniorAmount(wrapper);
    }

    @Override
    public Boolean insertUserCommission(Long userId,Long fromUserId, String gameId, String gameName, UserCommissionType type,String gameBusiness,
                                        BigDecimal profit, BigDecimal rate, BigDecimal actualAmount,Long betId) {
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
                .betId(betId)
                .build();
        return save(commission);
    }
}
