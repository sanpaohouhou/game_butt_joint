package com.tl.tgGame.project.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tl.tgGame.common.lock.RedisLock;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.dto.*;
import com.tl.tgGame.project.entity.Currency;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.enums.*;
import com.tl.tgGame.project.mapper.UserMapper;
import com.tl.tgGame.project.service.*;
import com.tl.tgGame.util.RedisKeyGenerator;
import com.tl.tgGame.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/4 , 17:40
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private UserCommissionService userCommissionService;

    @Autowired
    private BetService betService;

    @Autowired
    private GameService gameService;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private RedisKeyGenerator redisKeyGenerator;

    @Override
    public User insertUser(String firstName, String lastName, String username, Boolean isBot, Long tgId, String tgGroup) {
        String gameAccount = convertAccount();
        User user = checkGameAccount(gameAccount);
        if (user != null) {
            ErrorEnum.USERNAME_ALREADY_USED.throwException();
        }
        if (tgId.toString().equals(tgGroup)) {
            return null;
        }
        User buildUser = buildUser(firstName, lastName, username, isBot, tgId, tgGroup, gameAccount);
        boolean save = save(buildUser);
        if (!save) {
            ErrorEnum.SYSTEM_ERROR.throwException();
        }
        return buildUser;
    }

    @Override
    public User checkGameAccount(String gameAccount) {
        return getOne(new LambdaQueryWrapper<User>().eq(User::getGameAccount, gameAccount));
    }

    @Override
    public User checkTgIdAndGroup(Long tgId, String tgGroup) {
        return getOne(new LambdaQueryWrapper<User>().eq(User::getTgId, tgId).eq(User::getTgGroup, tgGroup));
    }

    @Override
    public User checkTgId(Long tgId) {
        return getOne(new LambdaQueryWrapper<User>().eq(User::getTgId, tgId));
    }

    @Override
    public User queryByMemberAccount(String memberAccount) {
        return getOne(new LambdaQueryWrapper<User>().eq(User::getGameAccount, memberAccount));
    }

    @Override
    public BotPersonInfo getbotPersonInfo(User user) {
        //余额
        Currency currency = currencyService.getOrCreate(user.getId(), UserType.USER);
        //总充值
        BigDecimal sumRecharge = rechargeService.sumRecharge(user.getId(), UserType.USER, null, null);
        //总提现
        BigDecimal sumWithdrawal = withdrawalService.allWithdrawalAmount(user.getId(), UserType.USER, List.of(WithdrawStatus.withdraw_success), null, null);
        //提现待审核金额
        BigDecimal withdrawalWaitAuth = withdrawalService.allWithdrawalAmount(user.getId(), UserType.USER,
                List.of(WithdrawStatus.created, WithdrawStatus.review_success, WithdrawStatus.withdrawing), null, null);
        //总佣金
        BigDecimal allCommission = userCommissionService.sumAmount(user.getId(), UserCommissionType.COMMISSION, null, null, null);
        //总返水
        BigDecimal allBackWater = userCommissionService.sumAmount(user.getId(), UserCommissionType.BACK_WATER, null, null, null);
        //待返水
        BigDecimal waitBackWater = betService.sumAmount(user.getId(), false);

        return BotPersonInfo.builder()
                .balance(currency.getBalance())
                .allCommission(allCommission)
                .allBackWater(allBackWater)
                .allRecharge(sumRecharge)
                .allWithdrawal(sumWithdrawal)
                .allProfit(allCommission.add(allBackWater))
                .waitAuthWithdrawal(withdrawalWaitAuth)
                .waitBackWater(waitBackWater)
                .withdrawalUrl(user.getWithdrawalUrl())
                .gameAccount(user.getGameAccount()).build();
    }

    @Override
    public BotGameStatisticsInfo getGameStatisticsInfo(User user) {
        /**
         * 当月下注金额
         */
        LocalDateTime monthBegin = TimeUtil.getMonthBegin();
        LocalDateTime endTime = LocalDateTime.now();
        BigDecimal monthBetAmount = betService.sumBetAmount(user.getId(), monthBegin, endTime);
        /**
         * 本周下注金额
         */
        LocalDateTime weekBegin = TimeUtil.getWeekBegin();
        BigDecimal weekBetAmount = betService.sumBetAmount(user.getId(), weekBegin, endTime);
        /**
         * 当天下注金额
         */
        LocalDateTime todayBegin = TimeUtil.getTodayBegin();
        BigDecimal dayBetAmount = betService.sumBetAmount(user.getId(), todayBegin, endTime);

        /**
         * 当月派彩
         */
        BigDecimal monthFestoon = userCommissionService.sumAmount(user.getId(), null, null, monthBegin, endTime);
        /**
         * 本周派彩
         */
        BigDecimal weekFestoon = userCommissionService.sumAmount(user.getId(), null, null, weekBegin, endTime);
        /**
         * 当日派彩
         */
        BigDecimal dayFestoon = userCommissionService.sumAmount(user.getId(), null, null, todayBegin, endTime);

        /**
         * 当月盈利
         */
        BigDecimal monthProfit = betService.sumWinLose(user.getId(), monthBegin, endTime, true);
        /**
         * 本周盈利
         */
        BigDecimal weekProfit = betService.sumWinLose(user.getId(), weekBegin, endTime, true);
        /**
         * 当日盈利
         */
        BigDecimal dayProfit = betService.sumWinLose(user.getId(), todayBegin, endTime, true);


        return BotGameStatisticsInfo.builder()
                .dayFestoon(dayFestoon)
                .dayBet(dayBetAmount)
                .dayProfit(dayProfit)
                .weekBet(weekBetAmount)
                .weekProfit(weekProfit)
                .weekFestoon(weekFestoon)
                .monthFestoon(monthFestoon)
                .monthBet(monthBetAmount)
                .monthProfit(monthProfit).build();
    }

    @Override
    public GameBusinessStatisticsInfo getGameBusinessStatistics(User user, String gameBusiness) {

        //总佣金
        BigDecimal allCommission = userCommissionService.sumAmount(user.getId(), UserCommissionType.COMMISSION, gameBusiness, null, null);
        //总返水
        BigDecimal allBackWater = userCommissionService.sumAmount(user.getId(), UserCommissionType.BACK_WATER, gameBusiness, null, null);
        //待返水
        BigDecimal waitBackWater = betService.sumAmount(user.getId(), false);

        GameBusinessStatisticsInfo.builder()
                .gameBusiness(GameBusiness.of(gameBusiness))
                .backWaterRate(BigDecimal.valueOf(2) + "%")
                .juniorCommissionRate(BigDecimal.valueOf(2) + "%")
                .backWater(allBackWater)
                .waitBackWater(waitBackWater)
                .juniorCommission(allCommission);
        return null;
    }

    @Override
    public BotExtendStatisticsInfo getExtendStatistics(User user) {

        /**
         * 当月下注金额
         */
        LocalDateTime monthBegin = TimeUtil.getMonthBegin();
        LocalDateTime endTime = LocalDateTime.now();
        /**
         * 当天下注金额
         */
        LocalDateTime todayBegin = TimeUtil.getTodayBegin();

        //当月总充值
        BigDecimal monthAllRecharge = rechargeService.sumRecharge(user.getId(), UserType.USER, monthBegin, endTime);
        //当日总充值
        BigDecimal todayAllRecharge = rechargeService.sumRecharge(user.getId(), UserType.USER, todayBegin, endTime);
        //当月总提现
        BigDecimal monthWithdrawal = withdrawalService.allWithdrawalAmount(user.getId(), UserType.USER, List.of(WithdrawStatus.withdraw_success), monthBegin, endTime);
        //今日总提现
        BigDecimal todayWithdrawal = withdrawalService.allWithdrawalAmount(user.getId(), UserType.USER, List.of(WithdrawStatus.withdraw_success), monthBegin, endTime);
        //总邀请
        List<User> users = queryByInviteUser(user.getInviteUser(), null, null);
        //当月总邀请
        List<User> monthUsers = queryByInviteUser(user.getId(),monthBegin,endTime);
        //当日总邀请
        List<User> todayUsers = queryByInviteUser(user.getId(),monthBegin,endTime);
        //总佣金
        BigDecimal todayCommission = userCommissionService.sumAmount(user.getId(), UserCommissionType.COMMISSION, null, null, null);
        //总返水
        BigDecimal todayBackWater = userCommissionService.sumAmount(user.getId(), UserCommissionType.BACK_WATER, null, todayBegin, endTime);
        //当月总盈利
        BigDecimal monthAllProfit = userCommissionService.sumAmount(user.getId(), null, null, monthBegin, endTime);


        return BotExtendStatisticsInfo.builder()
                .monthAllRecharge(monthAllRecharge)
                .monthAllProfit(monthAllProfit)
                .monthJoinGroup(monthUsers.size())
                .monthALlWithdrawal(monthWithdrawal)
                .peopleNumber(users.size())
                .todayAllBackWater(todayBackWater)
                .settledCommission(todayCommission)
                .todayAllProfit(todayCommission.add(todayBackWater))
                .todayAllRecharge(todayAllRecharge)
                .todayJoinGroup(todayUsers.size())
                .todayAllWithdrawal(todayWithdrawal)
                .build();

    }

    @Override
    public Boolean updateByHasGroup(Long tgId, String tgGroup, Boolean hasGroup) {
        return update(new LambdaUpdateWrapper<User>().set(User::getHasGroup, hasGroup).eq(User::getTgId, tgId).eq(User::getTgGroup, tgGroup));
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean fcGameRecharge(Long tgId) {
        String key = redisKeyGenerator.generateKey("fcGameRecharge", tgId);
        redisLock.redissonLock(key);
        try {
            User user = checkTgId(tgId);
            if (user == null) {
                return false;
            }
            Currency currency = currencyService.getOrCreate(user.getId(), UserType.USER);
            ApiSetPointReq build = ApiSetPointReq.builder()
                    .Points(currency.getRemain().doubleValue())
                    .MemberAccount(user.getGameAccount())
                    .build();
            ApiSetPointRes apiSetPointRes = gameService.setPoints(build);
            if (apiSetPointRes.getResult().equals(0)) {
                currencyService.withdraw(user.getId(), UserType.USER, BusinessEnum.FC_RECHARGE, currency.getRemain(),
                        apiSetPointRes.getTrsID(), "Fc电子充值:" + apiSetPointRes.getBankID());
                return true;
            }
            return false;
        } catch (Exception e) {
            ErrorEnum.SYSTEM_BUSY.throwException(e.getMessage());
        } finally {
            redisLock._redissonUnLock(key);
        }
        return null;
    }

    @Override
    public Boolean fcGameWithdrawal(Long tgId) {
        String key = redisKeyGenerator.generateKey("fcGameWithdrawal", tgId);
        redisLock.redissonLock(key);
        try {
            User user = checkTgId(tgId);
            if (user == null) {
                return false;
            }
            ApiSetPointReq build = ApiSetPointReq.builder()
                    .MemberAccount(user.getGameAccount())
                    .AllOut(1)
                    .build();
            ApiSetPointRes apiSetPointRes = gameService.setPoints(build);
            if (apiSetPointRes.getResult().equals(0)) {
                currencyService.increase(user.getId(), UserType.USER, BusinessEnum.FC_WITHDRAWAL, BigDecimal.valueOf(apiSetPointRes.getPoints()),
                        apiSetPointRes.getTrsID(), "Fc电子提现:" + apiSetPointRes.getBankID());
                return true;
            }
            return false;
        } catch (Exception e) {
            ErrorEnum.SYSTEM_BUSY.throwException(e.getMessage());
        } finally {
            redisLock._redissonUnLock(key);
        }
        return null;
    }

    @Override
    public List<User> queryByInviteUser(Long inviteUser, LocalDateTime startTime,LocalDateTime endTime) {
        return list(new LambdaQueryWrapper<User>().eq(User::getInviteUser,inviteUser).ge(Objects.nonNull(startTime),User::getJoinedTime,startTime)
                .le(Objects.nonNull(endTime),User::getJoinedTime,endTime));
    }


    public static String convertAccount() {
        long anInt = RandomUtil.randomLong(9999999999L);
        return "qu" + anInt;
    }

    public User buildUser(String firstName, String lastName, String username, Boolean isBot, Long tgId, String tgGroup, String gameAccount) {
        return User.builder()
                .joinedTime(LocalDateTime.now())
                .firstName(firstName)
                .lastName(lastName)
                .gameAccount(gameAccount)
                .isBot(isBot)
                .tgId(tgId)
                .username(username)
                .tgGroup(tgGroup)
                .build();
    }

    public static void main(String[] args) {
        String s = convertAccount();
        System.out.println(s);
    }
}
