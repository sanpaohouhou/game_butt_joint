package com.tl.tgGame.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tl.tgGame.admin.dto.*;
import com.tl.tgGame.common.dto.Response;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.dto.GameBetStatisticsListRes;
import com.tl.tgGame.project.entity.*;
import com.tl.tgGame.project.entity.Currency;
import com.tl.tgGame.project.enums.BusinessEnum;
import com.tl.tgGame.project.enums.GameBusiness;
import com.tl.tgGame.project.enums.UserType;
import com.tl.tgGame.project.enums.WithdrawStatus;
import com.tl.tgGame.project.service.*;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import com.tl.tgGame.tgBot.service.BotMessageService;
import com.tl.tgGame.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 16:35
 */
@RestController
@RequestMapping("/api/admin/user")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private GameBetService gameBetService;

    @Autowired
    private CurrencyLogService currencyLogService;

    @Autowired
    private WithdrawalService withdrawalService;
    @Resource
    private BotMessageService botMessageService;

    @Autowired
    private ConfigService configService;

    /**
     * 用户列表
     *
     * @param req
     * @return
     */
    @GetMapping("userList")
    public Response userList(AdminUserListReq req) {
        Page<User> page = userService.page(new Page<>(req.getPage(), req.getSize()), new LambdaQueryWrapper<User>()
                .eq(!StringUtils.isEmpty(req.getGameAccount()), User::getGameAccount, req.getGameAccount())
                .eq(Objects.nonNull(req.getPartnerId()), User::getPartnerId, req.getPartnerId())
                .ge(Objects.nonNull(req.getStartTime()), User::getJoinedTime, req.getStartTime())
                .le(Objects.nonNull(req.getEndTime()), User::getJoinedTime, req.getEndTime()).orderByDesc(User::getJoinedTime));
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Response.pageResult(page);
        }
        List<User> list = new ArrayList<>();
        List<User> records = page.getRecords();
        for (User user : records) {
            Currency currency = currencyService.getOrCreate(user.getId(), UserType.USER);
            user.setCurrency(currency);
            list.add(user);
        }
        page.setRecords(list);
        return Response.pageResult(page);
    }

    @GetMapping("userInfo")
    public Response userInfo(@RequestParam Long userId,
                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        User user = userService.getById(userId);
        if (user == null) {
            ErrorEnum.OBJECT_NOT_FOUND.throwException();
        }
        BigDecimal rechargeAmount = rechargeService.sumRecharge(userId, UserType.USER, startTime, endTime);
        BigDecimal withdrawalAmount = withdrawalService.allWithdrawalAmount(userId, UserType.USER,
                Arrays.asList(WithdrawStatus.withdraw_success, WithdrawStatus.withdrawing), startTime, endTime);
        GameBetStatisticsListRes statistics = gameBetService.userBetStatistics(userId, startTime, endTime);
        if (statistics == null) {
            statistics = new GameBetStatisticsListRes();
        }
        UserInfoRes build = UserInfoRes
                .builder()
                .betAmount(statistics.getBetAmount())
                .betNumber(statistics.getBetNumber())
                .rechargeAmount(rechargeAmount)
                .profit(statistics.getUserProfit())
                .withdrawalUrl(user.getWithdrawalUrl())
                .withdrawalAmount(withdrawalAmount)
                .backWaterAmount(statistics.getBackWaterAmount())
                .validAmount(statistics.getValidAmount()).build();
        return Response.success(build);
    }

    /**
     * 修改用户提现地址
     */
    @PostMapping("updateWithdrawalUrl/{userId}")
    public Response updateWithdrawalUrl(@PathVariable Long userId, String withdrawalUrl) {
        User user = userService.getById(userId);
        if (user == null) {
            ErrorEnum.OBJECT_NOT_FOUND.throwException();
        }
        user.setWithdrawalUrl(withdrawalUrl);
        boolean b = userService.updateById(user);
        return Response.success(b);
    }

    /**
     * 游戏统计
     */
    @GetMapping("userGameStatistics")
    public Response userGameStatistics(AdminGameReq req) {
        List<GameBetStatisticsListRes> gameBetStatisticsListRes = gameBetService.betStatistics(req);
        UserInfoRes res = new UserInfoRes();
        if(CollectionUtils.isEmpty(gameBetStatisticsListRes)){
            return Response.success(res);
        }
        Integer betNumber = 0;
        BigDecimal betAmount = BigDecimal.ZERO;
        BigDecimal profit = BigDecimal.ZERO;
        BigDecimal validAmount = BigDecimal.ZERO;
        BigDecimal backWaterAmount = BigDecimal.ZERO;
        for (GameBetStatisticsListRes listRes:gameBetStatisticsListRes) {
            betNumber = betNumber + listRes.getBetNumber();
            betAmount = betAmount.add(listRes.getBetAmount());
            profit = profit.add(listRes.getUserProfit());
            validAmount = validAmount.add(listRes.getValidAmount());
            backWaterAmount = backWaterAmount.add(listRes.getBackWaterAmount());
            listRes.setGameBusiness(GameBusiness.of(listRes.getGameBusiness()));
        }
        res.setProfit(profit);
        res.setBetAmount(betAmount);
        res.setBetNumber(betNumber);
        res.setValidAmount(validAmount);
        res.setBetList(gameBetStatisticsListRes);
        res.setBackWaterAmount(backWaterAmount);
        return Response.success(res);
    }

    /**
     * 用户转账
     *
     * @param req
     * @return
     */
    @GetMapping("/recharge/list")
    public Response chargeRecord(AdminUserRechargeReq req
    ) {
        Long userId = req.getUserId();
        if (!StringUtils.isEmpty(req.getGameAccount())) {
            User user = userService.queryByMemberAccount(req.getGameAccount());
            if (Objects.isNull(user)) {
                ErrorEnum.OBJECT_NOT_FOUND.throwException();
            }
            userId = user.getId();
        }
        Page<Recharge> page = rechargeService.page(new Page<>(req.getPage(), req.getSize()),
                new LambdaQueryWrapper<Recharge>()
                        .eq(Objects.nonNull(req.getAgentId()),Recharge::getUserId,req.getAgentId())
                        .eq(Objects.nonNull(userId), Recharge::getUserId, userId)
                        .eq(Objects.nonNull(req.getUserType()),Recharge::getUserType,req.getUserType())
                        .ge(Objects.nonNull(req.getStartTime()), Recharge::getCreateTime, req.getStartTime())
                        .le(Objects.nonNull(req.getEndTime()), Recharge::getCreateTime, req.getEndTime())
                        .orderByDesc(Recharge::getId));
        List<Recharge> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return Response.pageResult(page);
        }
        List<Recharge> recharges = new ArrayList<>();
        for (Recharge recharge : records) {
            User user = null;
            if(recharge.getUserType().equals(UserType.USER)){
                user = userService.getById(recharge.getUserId());
            }else if(recharge.getUserType().equals(UserType.AGENT)){
                user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getAgentId, recharge.getUserId()));
            }
            if(user != null){
                recharge.setGameAccount(user.getGameAccount());
                recharge.setAgentId(user.getAgentId());
            }
            recharges.add(recharge);
        }
        page.setRecords(recharges);
        return Response.pageResult(page);
    }


    /**
     * 用户快速充值
     *
     * @param dto
     * @return
     */
    @PostMapping("/user/recharge")
    @Transactional(rollbackFor = Exception.class)
    public Response userRecharge(@RequestBody @Valid ChargeDTO dto) {
        User user = userService.getById(dto.getUserId());
        if (user == null) {
            ErrorEnum.USER_NOT_JOIN.throwException();
        }
        if (user.getIsBot()) {
            ErrorEnum.BOT_NOT_ALLOW_WITHDRAW.throwException();
        }
        List<InlineKeyboardButton> keyboardButtons = Collections.singletonList(InlineKeyboardButton.builder().text("唯一充提财务").url("https://t.me/cin89886").build());
        Currency currency = currencyService.getOrCreate(user.getId(), UserType.USER);
        Recharge recharge = rechargeService.addRecharge(dto.getUserId(),
                dto.getAmount(),
                UserType.USER,
                null,
                dto.getAddress(),
                dto.getHash(),
                dto.getNetwork(),
                dto.getScreen(),
                dto.getNote(), currency);
        currencyService.increase(dto.getUserId(), UserType.USER, BusinessEnum.RECHARGE, dto.getAmount(), recharge.getId(), "充值");
        String chat = configService.getOrDefault(ConfigConstants.BOT_BEGIN_GAME_GROUP_CHAT, null);
        botMessageService.sendMessageAsync(chat, "",
                InlineKeyboardMarkup.builder().keyboardRow(keyboardButtons).build());
        return Response.success(recharge);
    }

    /**
     * 用户详情
     *
     * @param gameAccount
     * @return
     */
    @GetMapping("/{gameAccount}")
    public Response getUser(@PathVariable String gameAccount) {
        User user = userService.queryByMemberAccount(gameAccount);
        if (user != null) {
            user.setCurrency(currencyService.get(user.getId(), UserType.USER));
            LocalDateTime todayBegin = TimeUtil.getTodayBegin();
            LocalDateTime now = LocalDateTime.now();
            int todayBetCount = gameBetService.count(new LambdaQueryWrapper<GameBet>().eq(GameBet::getUserId, user.getId())
                    .ge(GameBet::getRecordTime, todayBegin)
                    .le(GameBet::getRecordTime, now));
            int allBetCount = gameBetService.count(new LambdaQueryWrapper<GameBet>().eq(GameBet::getUserId, user.getId()));
            BigDecimal todayProfit = gameBetService.sumWinLose(user.getId(), todayBegin, now, null, null);
            BigDecimal allProfit = gameBetService.sumWinLose(user.getId(), null, null, null, null);
            user.setAllBetCount(allBetCount);
            user.setAllProfit(allProfit);
            user.setTodayBetCount(todayBetCount);
            user.setTodayProfit(todayProfit);
        }
        return Response.success(user);
    }


    /**
     * 保证金明细(账户操作明细)
     *
     * @param page
     * @param size
     * @param userType
     * @param userId
     * @param sn
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/currency-log/list")
    public Response currencyLogList(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "20") Integer size,
                                    @RequestParam(required = false) UserType userType,
                                    @RequestParam(required = false) Long userId,
                                    @RequestParam(required = false) Long agentId,
                                    @RequestParam(required = false) String sn,
                                    @RequestParam(required = false) String gameAccount,
                                    @RequestParam(required = false) BusinessEnum business,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime
    ) {
        if (!StringUtils.isEmpty(gameAccount)) {
            User user = userService.queryByMemberAccount(gameAccount);
            if (user != null) {
                userId = user.getId();
            }
        }
//        if(agentId != null){
//            User one = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getAgentId, agentId));
//            userId = one.getId();
//        }
        Page<CurrencyLog> page1 = currencyLogService.page(new Page<>(page, size),
                new LambdaQueryWrapper<CurrencyLog>()
                        .eq(Objects.nonNull(agentId),CurrencyLog::getUid,agentId)
                        .eq(Objects.nonNull(userId), CurrencyLog::getUid, userId)
                        .ne(CurrencyLog::getUid, 0)
                        .notIn(CurrencyLog::getBusiness, Arrays.asList(BusinessEnum.FC_RECHARGE, BusinessEnum.FC_WITHDRAWAL,
                                BusinessEnum.WL_RECHARGE, BusinessEnum.WL_WITHDRAWAL, BusinessEnum.EG_RECHARGE,
                                BusinessEnum.EG_WITHDRAWAL))
                        .eq(Objects.nonNull(userType), CurrencyLog::getUserType, userType)
                        .eq(Objects.nonNull(business), CurrencyLog::getBusiness, business)
                        .eq(org.apache.commons.lang3.StringUtils.isNotBlank(sn), CurrencyLog::getSn, sn)
                        .ge(Objects.nonNull(startTime), CurrencyLog::getCreateTime, startTime)
                        .le(Objects.nonNull(endTime), CurrencyLog::getCreateTime, endTime)
                        .orderByDesc(CurrencyLog::getId));
        List<CurrencyLog> records = page1.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return Response.pageResult(page1);
        }
        List<CurrencyLog> list = new ArrayList<>();
        for (CurrencyLog currencyLog : records) {
            User user = userService.getById(currencyLog.getUid());
            if (user != null) {
                currencyLog.setGameAccount(user.getGameAccount());
            }
            list.add(currencyLog);
        }
        page1.setRecords(list);
        return Response.pageResult(page1);
    }


}
