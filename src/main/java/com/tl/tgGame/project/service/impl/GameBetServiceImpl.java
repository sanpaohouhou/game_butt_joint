package com.tl.tgGame.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tl.tgGame.admin.dto.AdminGameReq;
import com.tl.tgGame.admin.dto.UserExtendBetStatisticsDTO;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.dto.GameBetStatisticsListRes;
import com.tl.tgGame.project.entity.Agent;
import com.tl.tgGame.project.entity.Game;
import com.tl.tgGame.project.entity.GameBet;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.enums.BusinessEnum;
import com.tl.tgGame.project.enums.GameBusiness;
import com.tl.tgGame.project.enums.UserCommissionType;
import com.tl.tgGame.project.enums.UserType;
import com.tl.tgGame.project.mapper.GameBetMapper;
import com.tl.tgGame.project.service.*;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
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
    private UserService userService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private UserCommissionService userCommissionService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private GameService gameService;

    @Autowired
    private CurrencyGameProfitService currencyGameProfitService;

    @Override
    public BigDecimal sumAmount(Long userId, Boolean hasSettled, String business) {
        LambdaQueryWrapper<GameBet> wrapper = new LambdaQueryWrapper<GameBet>()
                .eq(GameBet::getUserId, userId)
                .eq(Objects.nonNull(business), GameBet::getGameBusiness, business)
                .eq(GameBet::getHasSettled, hasSettled);
        return getBaseMapper().sumAmount(wrapper);
    }

    @Override
    public BigDecimal sumBetAmount(Long userId, LocalDateTime startTime, LocalDateTime endTime, Boolean hasSettled) {
        LambdaQueryWrapper<GameBet> wrapper = new LambdaQueryWrapper<GameBet>()
                .eq(GameBet::getUserId, userId)
                .eq(Objects.nonNull(hasSettled), GameBet::getHasSettled, hasSettled)
                .ge(Objects.nonNull(startTime), GameBet::getRecordTime, startTime)
                .le(Objects.nonNull(endTime), GameBet::getRecordTime, endTime);
        return getBaseMapper().sumBetAmount(wrapper);
    }

    @Override
    public BigDecimal sumWinLose(Long userId, LocalDateTime startTime, LocalDateTime endTime
            , Boolean hasWinLose, String gameBusiness) {
        LambdaQueryWrapper<GameBet> wrapper = new LambdaQueryWrapper<GameBet>()
                .eq(Objects.nonNull(userId), GameBet::getUserId, userId)
                .eq(!StringUtils.isEmpty(gameBusiness), GameBet::getGameBusiness, gameBusiness)
                .gt(hasWinLose != null && hasWinLose, GameBet::getProfit, 0)
                .lt(hasWinLose != null && !hasWinLose, GameBet::getProfit, 0)
                .ge(Objects.nonNull(startTime), GameBet::getRecordTime, startTime)
                .le(Objects.nonNull(endTime), GameBet::getRecordTime, endTime);
        return getBaseMapper().sumWinLose(wrapper);
    }

    @Override
    public Integer todayBetUserCount(LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<GameBet> wrapper = new LambdaQueryWrapper<GameBet>()
                .ge(Objects.nonNull(startTime), GameBet::getRecordTime, startTime)
                .le(Objects.nonNull(endTime), GameBet::getRecordTime, endTime);
        return getBaseMapper().todayBetUserCount(wrapper);
    }

    //用户输钱
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void loseCommission(GameBet gameBet) {
        User user = userService.getById(gameBet.getUserId());
        if (user == null) {
            ErrorEnum.OBJECT_NOT_FOUND.throwException();
        }
        //输钱返水
        BigDecimal backWaterRate = configService.getDecimal(ConfigConstants.GAME_BACK_WATER_RATE);
        BigDecimal backWater = gameBet.getProfit().multiply(backWaterRate).setScale(2, RoundingMode.DOWN);
        currencyGameProfitService.increase(gameBet.getUserId(), gameBet.getGameBusiness(), backWater.negate());
        BigDecimal remainProfit = gameBet.getProfit().subtract(backWater).negate();
        BigDecimal profit = remainProfit;
        if (user.getInviteUser() == null) {
            gameBet.setHasSettled(true);
            updateById(gameBet);
            return;
        }
//        User pUser = userService.getById(user.getInviteUser());
//        if(!pUser.getHasAgent()){
//            return;
//        }
        String[] inviteChain = user.getInviteChain().split(":");
        BigDecimal remainRate = BigDecimal.ZERO;
        for (int i = 0; i < inviteChain.length; i++) {
            String uid = inviteChain[i];
            int j = i + 1;
            String sonUid = null;
            if (j < inviteChain.length) {
                sonUid = inviteChain[j];
            }
            if (Long.valueOf(uid).equals(user.getId())) {
                continue;
            }
            User user1 = userService.getById(uid);
            if (!user1.getHasAgent()) {
                continue;
            }
            BigDecimal rate;
            BigDecimal dividendAmount;
            Agent agent = agentService.queryByUserId(user1.getId());//40
            if (agent.getLevel().equals(1)) {
                remainRate = agent.getDividendRate();
                BigDecimal platformRate = BigDecimal.ONE.subtract(remainRate);
                BigDecimal platformProfit = remainProfit.multiply(platformRate).setScale(2, RoundingMode.DOWN);
                commission(gameBet, 0L, UserCommissionType.DIVIDEND, BusinessEnum.DIVIDEND, platformProfit, platformRate, "平台分红");
                profit = profit.subtract(platformProfit);
            }
            if (sonUid != null) {
                User sonUser = userService.getById(sonUid);
                if (sonUser.getHasAgent()) {
                    Agent sonAgent = agentService.queryByUserId(Long.valueOf(sonUid)); //60
                    rate = remainRate.subtract(remainRate.multiply(sonAgent.getDividendRate()));
                    dividendAmount = remainProfit.multiply(rate).setScale(2, RoundingMode.DOWN);
                    profit = profit.subtract(dividendAmount);
                    remainRate = remainRate.subtract(rate);
                } else {
                    rate = remainRate;
                    dividendAmount = profit;
                }
            } else {
                rate = remainRate;
                dividendAmount = profit;
            }
            commission(gameBet, user1.getId(), UserCommissionType.DIVIDEND, BusinessEnum.DIVIDEND, dividendAmount, rate, "代理商" + agent.getLevel() + "级分红:" + gameBet.getRecordId());
        }
        boolean update = update(new LambdaUpdateWrapper<GameBet>().set(GameBet::getHasSettled, true)
                .set(GameBet::getUpdateTime, LocalDateTime.now()).set(GameBet::getBackWaterAmount, backWater.negate())
                .eq(GameBet::getHasSettled, false).eq(GameBet::getId, gameBet.getId()));
        if (!update) {
            ErrorEnum.SYSTEM_ERROR.throwException();
        }
    }

    //用户赢钱
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void winCommission(GameBet gameBet) {
        User user = userService.getById(gameBet.getUserId());
        if (user == null) {
            ErrorEnum.OBJECT_NOT_FOUND.throwException();
        }
        BigDecimal remainProfit = gameBet.getProfit();
        BigDecimal profit = gameBet.getProfit();
        //赢钱给上级返佣金
        if (user.getInviteUser() == null) {
            gameBet.setHasSettled(true);
            updateById(gameBet);
            return;
        }
        User pUser = userService.getById(user.getInviteUser());
        Game game = gameService.queryByName(GameBusiness.of(gameBet.getGameBusiness()));
        BigDecimal commissionRate = BigDecimal.ZERO;
        if (game != null) {
            commissionRate = game.getTopCommissionRate();
        }
        BigDecimal topCommission = gameBet.getProfit().multiply(commissionRate).setScale(2, RoundingMode.DOWN);
        if (topCommission.compareTo(BigDecimal.ZERO) > 0) {
            commission(gameBet, pUser.getId(), UserCommissionType.COMMISSION, BusinessEnum.COMMISSION, topCommission, commissionRate, "上级返佣:" + gameBet.getRecordId());
            remainProfit = gameBet.getProfit().subtract(topCommission);
            profit = remainProfit;
        }
//        if(!pUser.getHasAgent()){
//            return;
//        }
        String[] inviteChain = user.getInviteChain().split(":");
        BigDecimal remainRate = BigDecimal.ZERO;
        for (int i = 0; i < inviteChain.length; i++) {
            String uid = inviteChain[i];
            int j = i + 1;
            String sonUid = null;
            if (j < inviteChain.length) {
                sonUid = inviteChain[j];
            }
            if (Long.valueOf(uid).equals(user.getId())) {
                continue;
            }
            User user1 = userService.getById(uid);
            if (!user1.getHasAgent()) {
                continue;
            }
            BigDecimal rate;
            BigDecimal dividendAmount;
            Agent agent = agentService.queryByUserId(user1.getId());//40
            if (agent.getLevel().equals(1)) {
                remainRate = agent.getDividendRate();
                BigDecimal platformRate = BigDecimal.ONE.subtract(remainRate);
                BigDecimal platformProfit = remainProfit.multiply(platformRate).setScale(2, RoundingMode.DOWN);
                winDividend(gameBet, 0L, UserCommissionType.DIVIDEND, BusinessEnum.DIVIDEND, platformProfit, platformRate, "平台分红");
                profit = profit.subtract(platformProfit);
            }
            if (sonUid != null) {
                User sonUser = userService.getById(sonUid);
                if (sonUser.getHasAgent()) {
                    Agent sonAgent = agentService.queryByUserId(Long.valueOf(sonUid)); //60
                    rate = remainRate.subtract(remainRate.multiply(sonAgent.getDividendRate()));
                    dividendAmount = remainProfit.multiply(rate).setScale(2, RoundingMode.DOWN);
                    profit = profit.subtract(dividendAmount);
                    remainRate = remainRate.subtract(rate);
                } else {
                    rate = remainRate;
                    dividendAmount = profit;
                }
            } else {
                rate = remainRate;
                dividendAmount = profit;
            }
            winDividend(gameBet, user1.getId(), UserCommissionType.DIVIDEND, BusinessEnum.DIVIDEND, dividendAmount, rate, "合伙人占成结算");
        }
        boolean update = update(new LambdaUpdateWrapper<GameBet>().set(GameBet::getHasSettled, true)
                .set(GameBet::getUpdateTime, LocalDateTime.now()).set(GameBet::getTopCommission, topCommission)
                .eq(GameBet::getHasSettled, false).eq(GameBet::getId, gameBet.getId()));
        if (!update) {
            ErrorEnum.SYSTEM_ERROR.throwException();
        }
    }

    @Override
    public List<GameBetStatisticsListRes> betStatistics(AdminGameReq req) {
        String gameBusiness = null;
        if (!StringUtils.isEmpty(req.getGameName())) {
            gameBusiness = GameBusiness.gameName(req.getGameName());
        }
        LambdaQueryWrapper<GameBet> wrapper = new LambdaQueryWrapper<GameBet>()
                .eq(req.getUserId() != null , GameBet::getUserId,req.getUserId())
                .eq(!StringUtils.isEmpty(gameBusiness), GameBet::getGameBusiness, gameBusiness)
                .ge(req.getStartTime() != null, GameBet::getRecordTime, req.getStartTime())
                .le(req.getEndTime() != null, GameBet::getRecordTime, req.getEndTime())
                .groupBy(GameBet::getGameBusiness);
        return getBaseMapper().betStatistics(wrapper);
    }

    @Override
    public GameBetStatisticsListRes userBetStatistics(Long userId,LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<GameBet> wrapper = new LambdaQueryWrapper<GameBet>()
                .eq(userId != null,GameBet::getUserId,userId)
                .ge(startTime != null, GameBet::getRecordTime, startTime)
                .le(endTime != null, GameBet::getRecordTime, endTime);
        GameBetStatisticsListRes statistics = getBaseMapper().userBetStatistics(wrapper);
        if(statistics == null){
            statistics = new GameBetStatisticsListRes();
        }
        return statistics;
    }

    @Override
    public UserExtendBetStatisticsDTO extendGameBetList(Long inviteUserId, Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        QueryWrapper<Object> wrapper = new QueryWrapper<>()
                .eq(Objects.nonNull(inviteUserId), "u.invite_user", inviteUserId)
                .eq(Objects.nonNull(userId), "gb.user_id", userId)
                .ge(Objects.nonNull(startTime), "gb.record_time", startTime)
                .le(Objects.nonNull(endTime), "gb.record_time", endTime);
        UserExtendBetStatisticsDTO userExtendBetStatisticsDTO = getBaseMapper().extendBetStatistics(wrapper);
        if(userExtendBetStatisticsDTO == null){
            userExtendBetStatisticsDTO = new UserExtendBetStatisticsDTO();
        }
        return userExtendBetStatisticsDTO;
    }

    private void winDividend(GameBet gameBet, Long userId, UserCommissionType type, BusinessEnum businessEnum,
                             BigDecimal amount, BigDecimal rate, String des) {
        userCommissionService.insertUserCommission(userId, gameBet.getUserId(), gameBet.getGameId(), gameBet.getGameName(),
                type, gameBet.getGameBusiness(), amount.negate(), rate, gameBet.getProfit(), gameBet.getId());
        currencyService.withdrawalForce(userId, UserType.AGENT, businessEnum,
                amount, gameBet.getRecordId(), des);
    }

    private void commission(GameBet gameBet, Long userId, UserCommissionType type, BusinessEnum businessEnum, BigDecimal amount, BigDecimal rate, String des) {
        userCommissionService.insertUserCommission(userId, gameBet.getUserId(), gameBet.getGameId(), gameBet.getGameName(),
                type, gameBet.getGameBusiness(), amount, rate, gameBet.getProfit(), gameBet.getId());
        currencyService.increase(userId, UserType.AGENT, businessEnum,
                amount, gameBet.getRecordId(), des);
    }

}
