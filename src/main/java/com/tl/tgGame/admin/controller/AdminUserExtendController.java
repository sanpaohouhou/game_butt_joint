package com.tl.tgGame.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tl.tgGame.admin.dto.AdminUserExtendReq;
import com.tl.tgGame.admin.dto.UserExtendBetStatisticsDTO;
import com.tl.tgGame.admin.dto.UserExtendJuniorDTO;
import com.tl.tgGame.admin.dto.UserExtendListDTO;
import com.tl.tgGame.common.dto.Response;
import com.tl.tgGame.project.dto.GameBackWaterRes;
import com.tl.tgGame.project.dto.GameBetStatisticsListRes;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.enums.UserType;
import com.tl.tgGame.project.enums.WithdrawStatus;
import com.tl.tgGame.project.service.*;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/14 , 14:28
 */
@RestController
@RequestMapping("/api/admin/userExtend")
public class AdminUserExtendController {


    @Autowired
    private UserService userService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private GameBetService gameBetService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private CurrencyGameProfitService currencyGameProfitService;

    @Autowired
    private WithdrawalService withdrawalService;



    /**
     * 用户推广
     */
    @GetMapping("/list")
    public Response userExtend(AdminUserExtendReq req) {
        Page<User> page = userService.page(new Page<>(req.getPage(), req.getSize()));
        List<User> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return Response.pageResult(page);
        }
        List<UserExtendListDTO> list = new ArrayList<>();
        for (User user : records) {
            UserExtendListDTO extend = new UserExtendListDTO();
            BeanUtil.copyProperties(user, extend);
            String invite = configService.get(ConfigConstants.BOT_GROUP_INVITE_LINK);
            String inviteUrl = invite + "?start=" + user.getGameAccount();
            List<User> users = userService.queryByInviteUser(user.getId(), req.getStartTime(), req.getEndTime());
            UserExtendBetStatisticsDTO userExtendBetStatisticsDTO = gameBetService.extendGameBetList(user.getId(), null, req.getStartTime(), req.getEndTime());
            GameBackWaterRes gameBackWaterRes = currencyGameProfitService.juniorGameBackWaterStatistics(user.getId(), null);
            Integer rechargeNumber = rechargeService.countJuniorRechargeNumber(user.getId(), null, req.getStartTime(), req.getEndTime());
            BigDecimal rechargeAmount = rechargeService.sumJuniorRechargeAmount(user.getId(), null, req.getStartTime(), req.getEndTime());
            Integer withdrawalNumber = withdrawalService.countJuniorWithdrawalNumber(user.getId(), null,Arrays.asList(WithdrawStatus.withdraw_success),
                    req.getStartTime(), req.getEndTime());
            extend.setInviteUrl(inviteUrl);
            extend.setInviteNumber(users.size());
            extend.setAllProfit(userExtendBetStatisticsDTO.getAllProfit());
            extend.setCommission(userExtendBetStatisticsDTO.getAllTopCommission());
            extend.setWaitBackWaterAmount(gameBackWaterRes.getAllWaitBackWater());
            extend.setBackWaterAmount(gameBackWaterRes.getAllBackWater());
            extend.setLoseAmount(userExtendBetStatisticsDTO.getAllProfit());
            extend.setRechargeAmount(rechargeAmount);
            extend.setRechargeNumber(rechargeNumber);
            extend.setWithdrawalNumber(withdrawalNumber);
            list.add(extend);
        }
        Page<UserExtendListDTO> extendPage = new Page<>();
        extendPage.setCurrent(page.getCurrent());
        extendPage.setPages(page.getPages());
        extendPage.setSize(page.getSize());
        extendPage.setTotal(page.getTotal());
        extendPage.setRecords(list);
        return Response.pageResult(extendPage);
    }

    /**
     * 用户推广下级列表
     */
    @GetMapping("/juniorList")
    public Response juniorList(AdminUserExtendReq req) {
        Page<User> page = userService.page(new Page<>(req.getPage(), req.getSize()),
                new LambdaQueryWrapper<User>()
                        .eq(!StringUtils.isEmpty(req.getGameAccount()), User::getGameAccount, req.getGameAccount())
                        .eq(User::getInviteUser, req.getInviteUserId()));
        List<User> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return Response.pageResult(page);
        }
        List<UserExtendJuniorDTO> list = new ArrayList<>();
        for (User user : records) {
            BigDecimal rechargeAmount = rechargeService.sumRecharge(user.getId(), UserType.USER, req.getStartTime(), req.getEndTime());
            BigDecimal withdrawalAmount = withdrawalService.allWithdrawalAmount(user.getId(), UserType.USER,
                    Arrays.asList(WithdrawStatus.withdraw_success, WithdrawStatus.withdrawing), req.getStartTime(), req.getEndTime());
            GameBackWaterRes gameBackWaterRes = currencyGameProfitService.userBackWater(user.getId(), null);
            GameBetStatisticsListRes statistics = gameBetService.userBetStatistics(user.getId(), req.getStartTime(), req.getEndTime());
            UserExtendJuniorDTO build = UserExtendJuniorDTO.builder()
                    .backWaterAmount(gameBackWaterRes.getAllWaitBackWater())
                    .betAmount(statistics.getBetAmount())
                    .profit(statistics.getUserProfit())
                    .betNumber(statistics.getBetNumber())
                    .rechargeAmount(rechargeAmount)
                    .withdrawalAmount(withdrawalAmount)
                    .validAmount(statistics.getValidAmount())
                    .userId(user.getId())
                    .gameAccount(user.getGameAccount())
                    .build();
            list.add(build);
        }
        Page<UserExtendJuniorDTO> extendPage = new Page<>();
        extendPage.setCurrent(page.getCurrent());
        extendPage.setPages(page.getPages());
        extendPage.setSize(page.getSize());
        extendPage.setTotal(page.getTotal());
        extendPage.setRecords(list);
        return Response.pageResult(extendPage);
    }

}
