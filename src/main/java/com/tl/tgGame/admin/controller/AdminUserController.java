package com.tl.tgGame.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tl.tgGame.address.AddressService;
import com.tl.tgGame.admin.dto.*;
import com.tl.tgGame.common.dto.Response;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.entity.*;
import com.tl.tgGame.project.enums.BusinessEnum;
import com.tl.tgGame.project.enums.UserType;
import com.tl.tgGame.project.enums.WithdrawStatus;
import com.tl.tgGame.project.service.*;
import com.tl.tgGame.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private AddressService addressService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private UserProfitService userProfitService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private GameBetService gameBetService;



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
        if(!StringUtils.isEmpty(req.getGameAccount())){
            User user = userService.queryByMemberAccount(req.getGameAccount());
            if(Objects.isNull(user)){
                ErrorEnum.OBJECT_NOT_FOUND.throwException();
            }
            userId = user.getId();
        }
        Page<Recharge> page = rechargeService.page(new Page<>(req.getPage(), req.getSize()),
                new LambdaQueryWrapper<Recharge>()
                        .eq(Objects.nonNull(userId), Recharge::getUserId, userId)
                        .ge(Objects.nonNull(req.getStartTime()), Recharge::getCreateTime, req.getStartTime())
                        .le(Objects.nonNull(req.getEndTime()), Recharge::getCreateTime, req.getEndTime())
                        .orderByDesc(Recharge::getId));
        List<Recharge> records = page.getRecords();
        if(CollectionUtils.isEmpty(records)){
            return Response.pageResult(page);
        }
        List<Recharge> recharges = new ArrayList<>();
        for (Recharge recharge : records) {
            User user = userService.getById(recharge.getUserId());
            recharge.setGameAccount(user.getGameAccount());
            recharges.add(recharge);
        }
        page.setRecords(recharges);
        return Response.pageResult(page);
    }

    /**
     * 用户推广
     */
    @GetMapping("userExtend/list")
    public Response userExtend(AdminUserExtendReq req) {
        Page<User> page = userService.page(new Page<>(req.getPage(), req.getSize()));
        return Response.pageResult(page);
    }

    /**
     * 推广详情
     */
    @GetMapping("userExtendInfo")
    public Response userExtendInfo(Long userId) {
        return Response.success();
    }

    /** k
     * 用户获利
     */
    @GetMapping("userMakeProfit")
    public Response userMakeProfit(AdminUserMakeProfitReq req) {
        Page<UserProfit> page = userProfitService.page(new Page<>(req.getPage(), req.getSize()),
                new LambdaQueryWrapper<UserProfit>()
                        .eq(Objects.nonNull(req.getGameAccount()), UserProfit::getGameAccount, req.getGameAccount())
                        .eq(Objects.nonNull(req.getGameName()), UserProfit::getGameName, req.getGameName())
                        .ge(Objects.nonNull(req.getStartTime()), UserProfit::getCreateTime, req.getStartTime())
                        .le(Objects.nonNull(req.getEndTime()), UserProfit::getCreateTime, req.getEndTime()));
        return Response.pageResult(page);
    }

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
        Currency currency = currencyService.getOrCreate(user.getId(), UserType.USER);
        Recharge recharge = rechargeService.addRecharge(dto.getUserId(),
                dto.getAmount(),
                UserType.USER,
                null,
                dto.getAddress(),
                dto.getHash(),
                dto.getNetwork(),
                dto.getScreen(),
                dto.getNote(),currency);
        currencyService.increase(dto.getUserId(), UserType.USER, BusinessEnum.RECHARGE, dto.getAmount(), recharge.getId(), "充值");
        return Response.success(recharge);
    }

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
            BigDecimal todayProfit = gameBetService.sumWinLose(user.getId(), todayBegin, now, null,null);
            BigDecimal allProfit = gameBetService.sumWinLose(user.getId(), null, null, null,null);
            user.setAllBetCount(allBetCount);
            user.setAllProfit(allProfit);
            user.setTodayBetCount(todayBetCount);
            user.setTodayProfit(todayProfit);
        }
        return Response.success(user);
    }

}
