package com.tl.tgGame.project.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tl.tgGame.common.lock.RedisLock;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.dto.*;
import com.tl.tgGame.project.entity.Currency;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.enums.*;
import com.tl.tgGame.project.mapper.UserMapper;
import com.tl.tgGame.project.service.*;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import com.tl.tgGame.util.RedisKeyGenerator;
import com.tl.tgGame.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/4 , 17:40
 */
@Slf4j
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
    private GameBetService gameBetService;

    @Autowired
    private GameService gameService;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private RedisKeyGenerator redisKeyGenerator;

    @Autowired
    private ConfigService configService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private DefaultIdentifierGenerator defaultIdentifierGenerator;

    @Override
    public User insertUser(String firstName, String lastName, String username, Boolean isBot, Long tgId, String tgGroup,Long inviteUser,String inviteChain) {
        String gameAccount = convertAccount();
        User user = checkGameAccount(gameAccount);
        if (user != null) {
            ErrorEnum.USERNAME_ALREADY_USED.throwException();
        }
        Long id = defaultIdentifierGenerator.nextId(null);
        User buildUser = buildUser(id,firstName, lastName, username, isBot, tgId, tgGroup,
                gameAccount,inviteUser,StringUtils.isEmpty(inviteChain) ? String.valueOf(id) : inviteChain+":"+id);
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
        BigDecimal sumRecharge = rechargeService.sumRecharge(user.getId(), UserType.USER, null, null).setScale(2, RoundingMode.DOWN);
        //总提现
        BigDecimal sumWithdrawal = withdrawalService.allWithdrawalAmount(user.getId(), UserType.USER, List.of(WithdrawStatus.withdraw_success), null, null).setScale(2, RoundingMode.DOWN);
        ;
        //提现待审核金额
        BigDecimal withdrawalWaitAuth = withdrawalService.allWithdrawalAmount(user.getId(), UserType.USER,
                List.of(WithdrawStatus.created, WithdrawStatus.review_success, WithdrawStatus.withdrawing), null, null).setScale(2, RoundingMode.DOWN);
        ;
        //总佣金
        BigDecimal allCommission = userCommissionService.sumAmount(user.getId(), UserCommissionType.COMMISSION, null, null, null).setScale(2, RoundingMode.DOWN);
        ;
        //总返水
        BigDecimal allBackWater = userCommissionService.sumAmount(user.getId(), UserCommissionType.BACK_WATER, null, null, null).setScale(2, RoundingMode.DOWN);
        ;
        //待返水
        BigDecimal waitBackWater = gameBetService.sumBetAmount(user.getId(), null, null, false).setScale(2, RoundingMode.DOWN);
        ;
        BigDecimal backWaterRate = configService.getDecimal(ConfigConstants.GAME_BACK_WATER_RATE);
        return BotPersonInfo.builder()
                .balance(currency.getBalance().setScale(2, RoundingMode.DOWN))
                .allCommission(allCommission)
                .allBackWater(allBackWater)
                .allRecharge(sumRecharge)
                .allWithdrawal(sumWithdrawal)
                .allProfit(allCommission.add(allBackWater))
                .waitAuthWithdrawal(withdrawalWaitAuth)
                .waitBackWater(waitBackWater.multiply(backWaterRate).setScale(2, RoundingMode.DOWN))
                .withdrawalUrl(user.getWithdrawalUrl() == null ? "" : user.getWithdrawalUrl())
                .gameAccount(user.getGameAccount()).build();
    }

    @Override
    public BotGameStatisticsInfo getGameStatisticsInfo(User user) {
        /**
         * 当月下注金额
         */
        LocalDateTime monthBegin = TimeUtil.getMonthBegin();
        LocalDateTime endTime = LocalDateTime.now();
        BigDecimal monthBetAmount = gameBetService.sumBetAmount(user.getId(), monthBegin, endTime, null).setScale(2, RoundingMode.DOWN);
        ;
        /**
         * 本周下注金额
         */
        LocalDateTime weekBegin = TimeUtil.getWeekBegin();
        BigDecimal weekBetAmount = gameBetService.sumBetAmount(user.getId(), weekBegin, endTime, null).setScale(2, RoundingMode.DOWN);
        ;
        /**
         * 当天下注金额
         */
        LocalDateTime todayBegin = TimeUtil.getTodayBegin();
        BigDecimal dayBetAmount = gameBetService.sumBetAmount(user.getId(), todayBegin, endTime, null).setScale(2, RoundingMode.DOWN);
        ;

        /**
         * 当月派彩
         */
        BigDecimal monthFestoon = userCommissionService.sumAmount(user.getId(), null, null, monthBegin, endTime).setScale(2, RoundingMode.DOWN);
        ;
        /**
         * 本周派彩
         */
        BigDecimal weekFestoon = userCommissionService.sumAmount(user.getId(), null, null, weekBegin, endTime).setScale(2, RoundingMode.DOWN);
        ;
        /**
         * 当日派彩
         */
        BigDecimal dayFestoon = userCommissionService.sumAmount(user.getId(), null, null, todayBegin, endTime).setScale(2, RoundingMode.DOWN);
        ;

        /**
         * 当月盈利
         */
        BigDecimal monthProfit = gameBetService.sumWinLose(user.getId(), monthBegin, endTime, true).setScale(2, RoundingMode.DOWN);
        ;
        /**
         * 本周盈利
         */
        BigDecimal weekProfit = gameBetService.sumWinLose(user.getId(), weekBegin, endTime, true).setScale(2, RoundingMode.DOWN);
        ;
        /**
         * 当日盈利
         */
        BigDecimal dayProfit = gameBetService.sumWinLose(user.getId(), todayBegin, endTime, true).setScale(2, RoundingMode.DOWN);
        ;


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
        BigDecimal waitBackWater = gameBetService.sumAmount(user.getId(), false, gameBusiness).setScale(2, RoundingMode.DOWN);

        return GameBusinessStatisticsInfo.builder()
                .gameBusiness(GameBusiness.of(gameBusiness))
                .backWaterRate(BigDecimal.valueOf(2) + "%")
                .juniorCommissionRate(BigDecimal.valueOf(2) + "%")
                .backWater(allBackWater)
                .waitBackWater(waitBackWater)
                .juniorCommission(allCommission).build();
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
        BigDecimal monthAllRecharge = rechargeService.sumRecharge(user.getId(), UserType.USER, monthBegin, endTime).setScale(2, RoundingMode.DOWN);
        ;
        //当日总充值
        BigDecimal todayAllRecharge = rechargeService.sumRecharge(user.getId(), UserType.USER, todayBegin, endTime).setScale(2, RoundingMode.DOWN);
        ;
        //当月总提现
        BigDecimal monthWithdrawal = withdrawalService.allWithdrawalAmount(user.getId(), UserType.USER, List.of(WithdrawStatus.withdraw_success), monthBegin, endTime).setScale(2, RoundingMode.DOWN);
        ;
        //今日总提现
        BigDecimal todayWithdrawal = withdrawalService.allWithdrawalAmount(user.getId(), UserType.USER, List.of(WithdrawStatus.withdraw_success), monthBegin, endTime).setScale(2, RoundingMode.DOWN);
        ;
        //总邀请
        List<User> users = queryByInviteUser(user.getInviteUser(), null, null);
        //当月总邀请
        List<User> monthUsers = queryByInviteUser(user.getId(), monthBegin, endTime);
        //当日总邀请
        List<User> todayUsers = queryByInviteUser(user.getId(), monthBegin, endTime);
        //总佣金
        BigDecimal todayCommission = userCommissionService.sumAmount(user.getId(), UserCommissionType.COMMISSION, null, null, null).setScale(2, RoundingMode.DOWN);
        ;
        //总返水
        BigDecimal todayBackWater = userCommissionService.sumAmount(user.getId(), UserCommissionType.BACK_WATER, null, todayBegin, endTime).setScale(2, RoundingMode.DOWN);
        ;
        //当月总盈利
        BigDecimal monthAllProfit = userCommissionService.sumAmount(user.getId(), null, null, monthBegin, endTime).setScale(2, RoundingMode.DOWN);
        ;


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
        return update(new LambdaUpdateWrapper<User>().set(User::getHasGroup, hasGroup)
                .eq(User::getTgId, tgId).eq(User::getTgGroup, tgGroup).eq(User::getHasGroup, !hasGroup));
    }


    @Transactional
    @Override
    public Boolean gameRecharge(Long tgId, String gameType) {
        String key = redisKeyGenerator.generateKey("gameRecharge", tgId);
        redisLock.redissonLock(key);
        try {
            User user = checkTgId(tgId);
            if (user == null) {
                return false;
            }
            Currency currency = currencyService.getOrCreate(user.getId(), UserType.USER);
            if (currency.getRemain().compareTo(BigDecimal.ZERO) <= 0) {
                return true;
            }
            Boolean result = false;
            switch (gameType) {
                case "FC":
                    currencyService.withdraw(user.getId(), UserType.USER,
                            BusinessEnum.FC_RECHARGE, currency.getRemain(), null, gameType + "电子用户充值");
                    ApiSetPointReq build = ApiSetPointReq.builder()
                            .Points(currency.getRemain().doubleValue())
                            .MemberAccount(user.getGameAccount())
                            .build();
                    ApiSetPointRes apiSetPointRes = gameService.setPoints(build);
                    if (!apiSetPointRes.getResult().equals(0)) {
                        ErrorEnum.GAME_RECHARGE_FAIL.throwException();
                    }
                    result = true;
                    break;
                case "WL":
                    currencyService.withdraw(user.getId(), UserType.USER,
                            BusinessEnum.WL_RECHARGE, currency.getRemain(), null, gameType + "电子用户充值");
                    String gup = StrUtil.emptyToDefault(configService.get(ConfigConstants.WL_GAME_USDT_POINT), "7.00");
                    BigDecimal point = currency.getRemain().multiply(new BigDecimal(gup)).setScale(2, RoundingMode.HALF_DOWN);
                    ApiWlGameRes wlGameRes = gameService.wlPayOrder(user.getId(), point);
                    if (wlGameRes.getCode() != 0 || (wlGameRes.getData() != null && !wlGameRes.getData().getReason().equals("ok"))) {
                        String errMsg = wlGameRes.getData() == null ? wlGameRes.getMsg() : wlGameRes.getData().getReason();
                        ErrorEnum.GAME_RECHARGE_FAIL.throwException(errMsg);
                    }
                    result = true;
                    break;
                case "EG":
                    String merch = configService.get(ConfigConstants.EG_AGENT_CODE);
                    String transactionId = UUID.randomUUID().toString();
                    ApiEgDepositReq req = ApiEgDepositReq.builder().merch(merch)
                            .playerId(user.getGameAccount()).amount(currency.getRemain().toString()).transactionId(transactionId).build();
                    currencyService.withdraw(user.getId(), UserType.USER,
                            BusinessEnum.EG_RECHARGE, currency.getRemain(), transactionId, gameType + "电子用户充值");
                    ApiEgDepositRes apiEgDepositRes = gameService.egDeposit(req);
                    if (!StringUtils.isEmpty(apiEgDepositRes.getCode())) {
                        ErrorEnum.GAME_RECHARGE_FAIL.throwException();
                    }
                    result = true;
                    break;
            }
            if (result) {
                String gameRechargeKey = redisKeyGenerator.generateKey("GAME_RECHARGE", user.getTgId());
                stringRedisTemplate.boundValueOps(gameRechargeKey).set(gameType);
            }
            return result;
        } finally {
            redisLock._redissonUnLock(key);
        }
    }

    @Override
    public Boolean gameWithdrawal(Long tgId, String gameType) {
        String key = redisKeyGenerator.generateKey("gameWithdrawal", tgId);
        redisLock.redissonLock(key);
        try {
            User user = checkTgId(tgId);
            if (user == null) {
                return false;
            }
            switch (gameType) {
                case "FC":
                    ApiSetPointReq build = ApiSetPointReq.builder()
                            .MemberAccount(user.getGameAccount())
                            .AllOut(1)
                            .build();
                    ApiSetPointRes apiSetPointRes = gameService.setPoints(build);
                    if (apiSetPointRes.getResult().equals(0)) {
                        log.info("FC提现余额增加BankID:{},amount:{}", apiSetPointRes.getBankID(), apiSetPointRes.getPoints());
                        currencyService.increase(user.getId(), UserType.USER, BusinessEnum.FC_WITHDRAWAL, BigDecimal.valueOf(apiSetPointRes.getPoints()).negate(),
                                apiSetPointRes.getBankID(), "Fc电子提现之后金额:" + apiSetPointRes.getAfterPoint());
                        log.info("FC提现余额增加成功BankID:{},amount:{}", apiSetPointRes.getBankID(), apiSetPointRes.getPoints());
                        return true;
                    }
                    break;
                case "WL":
                    ApiWlGameOrderRes apiWlGameOrderRes = gameService.wlGetUserBalance(user.getId());
                    BigDecimal transferable = new BigDecimal(apiWlGameOrderRes.getTransferable());
                    //transferable大于0,就可以下分
                    if (transferable.compareTo(BigDecimal.ZERO) > 0) {
                        ApiWlGameRes wlGameRes = gameService.wlPayOrder(user.getId(), transferable.negate());
                        if (wlGameRes.getCode().equals(0) && wlGameRes.getData() != null
                                && wlGameRes.getData().getStatus().equals(1) && wlGameRes.getData().getReason().equals("ok")) {
                            BigDecimal gup = configService.getDecimal(ConfigConstants.WL_GAME_USDT_POINT);
                            BigDecimal gupAmount = transferable.divide(gup, 2, RoundingMode.HALF_DOWN);
                            log.info("WL提现余额增加OrderID:{},amount:{}", wlGameRes.getOrderId(), gupAmount);
                            currencyService.increase(user.getId(), UserType.USER, BusinessEnum.WL_WITHDRAWAL,
                                    gupAmount, wlGameRes.getOrderId(), "WL电子提现金额:" + transferable);
                            log.info("WL提现余额增加成功OrderID:{},amount:{}", wlGameRes.getOrderId(), gupAmount);
                            return true;
                        }
                    }
                    break;
                case "EG":
                    String merch = configService.get(ConfigConstants.EG_AGENT_CODE);
                    gameService.egLogout(ApiEgLogoutReq.builder().playerId(user.getGameAccount()).merch(merch).build());
                    String transactionId = UUID.randomUUID().toString();
                    ApiEgWithdrawReq req = ApiEgWithdrawReq.builder()
                            .amount("0").transactionId(transactionId).merch(merch).playerId(user.getGameAccount())
                            .takeAll("1").build();
                    ApiEgDepositRes apiEgDepositRes = gameService.egWithdraw(req);
                    if (StringUtils.isEmpty(apiEgDepositRes.getCode())) {
                        log.info("EG提现余额增加TransactionID:{},amount:{}", transactionId, apiEgDepositRes.getAmount());
                        currencyService.increase(user.getId(), UserType.USER, BusinessEnum.EG_WITHDRAWAL, new BigDecimal(apiEgDepositRes.getAmount())
                                , transactionId, "EG电子提现之前金额:" + apiEgDepositRes.getBeforeBalance());
                        log.info("EG提现余额增加TransactionId:{},amount:{}", transactionId, apiEgDepositRes.getAmount());
                        return true;
                    }
                    break;
            }
            return false;
        } finally {
            redisLock._redissonUnLock(key);
        }
    }

    @Override
    public List<User> queryByInviteUser(Long inviteUser, LocalDateTime startTime, LocalDateTime endTime) {
        return list(new LambdaQueryWrapper<User>().eq(User::getInviteUser, inviteUser).ge(Objects.nonNull(startTime), User::getJoinedTime, startTime)
                .le(Objects.nonNull(endTime), User::getJoinedTime, endTime));
    }


    public static String convertAccount() {
        long anInt = RandomUtil.randomLong(9999999999L);
        return "qu" + anInt;
    }

    public User buildUser(Long id,String firstName, String lastName, String username, Boolean isBot, Long tgId, String tgGroup,
                          String gameAccount,Long inviteUser,String inviteChain) {
        return User.builder()
                .id(id)
                .joinedTime(LocalDateTime.now())
                .firstName(firstName)
                .lastName(lastName)
                .gameAccount(gameAccount)
                .isBot(isBot)
                .tgId(tgId)
                .username(username)
                .tgGroup(tgGroup)
                .inviteUser(inviteUser)
                .inviteChain(inviteChain)
                .build();
    }

    public static void main(String[] args) {
        String s = convertAccount();
        System.out.println(s);
    }
}
