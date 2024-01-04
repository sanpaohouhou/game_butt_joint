package com.tl.tgGame.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tl.tgGame.admin.dto.AdminGameBetStatistics;
import com.tl.tgGame.admin.dto.AdminHomeUserStatistics;
import com.tl.tgGame.admin.dto.UserOverviewStatistics;
import com.tl.tgGame.common.dto.PageQueryDTO;
import com.tl.tgGame.common.dto.Response;
import com.tl.tgGame.project.entity.Game;
import com.tl.tgGame.project.entity.GameBet;
import com.tl.tgGame.project.entity.Recharge;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.enums.GameBusiness;
import com.tl.tgGame.project.enums.UserCommissionType;
import com.tl.tgGame.project.mapper.ConversionRateMapper;
import com.tl.tgGame.project.service.*;
import com.tl.tgGame.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 15:51
 */
@RestController
@RequestMapping("/api/admin/home")
public class AdminHomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private GameBetService gameBetService;
    
    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private ConversionRateMapper conversionRateMapper;

    @Autowired
    private UserCommissionService userCommissionService;
    /**
     * 用户统计
     * @return
     */
    @GetMapping("homeUserStatistics")
    public Response homeUserStatistics(){
        //总用户数
        int allUserCount = userService.count(new LambdaQueryWrapper<User>().eq(User::getHasGroup,true));
        //今日新增用户
        LocalDateTime todayBegin = TimeUtil.getTodayBegin();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime monthBegin = TimeUtil.getMonthBegin();
        List<User> todayUserList = userService.list(new LambdaQueryWrapper<User>().eq(User::getHasGroup, true)
                .ge(User::getJoinedTime, todayBegin).le(User::getJoinedTime, now));
        //总充值用户数量
        Integer allRechargeUserCount = rechargeService.countRechargeNumber(null, null, null);
        //今日注册用户充值数量
        Integer todayRegisterUserRechargeCount = 0;
        if(!CollectionUtils.isEmpty(todayUserList)){
            List<Long> userIds = todayUserList.stream().map(User::getId).collect(Collectors.toList());
            todayRegisterUserRechargeCount = rechargeService.countRechargeNumber(userIds,todayBegin,now);
        }
        //今日下注用户数
        Integer todayBetUserCount = gameBetService.todayBetUserCount(todayBegin,now);
        //今日充值金额
        BigDecimal todayRechargeAmount = rechargeService.sumRecharge(null, null, todayBegin, now);
        //今日提现金额
        BigDecimal todayWithdrawalAmount = withdrawalService.todayPlatformWithdrawalAmount();
        //今日用户下注金额
        BigDecimal todayUserBetAmount = gameBetService.sumBetAmount(null, todayBegin, now, null);
        //当月用户下注金额
        BigDecimal userBetAmount = gameBetService.sumBetAmount(null, monthBegin, now, null);

        BigDecimal allProfit = userCommissionService.sumAmount(null, UserCommissionType.DIVIDEND, null, null, null);

        AdminHomeUserStatistics build = AdminHomeUserStatistics.builder()
                .todayRegisterUserRechargeCount(todayRegisterUserRechargeCount)
                .allUserCount(allUserCount)
                .todayBetUserCount(todayBetUserCount)
                .allRechargeUserCount(allRechargeUserCount)
                .todayUserBetAmount(todayUserBetAmount)
                .userBetAmount(userBetAmount)
                .todayRechargeAmount(todayRechargeAmount)
                .todayWithdrawalAmount(todayWithdrawalAmount)
                .todayUserCount(todayUserList.size())
                .partnerAllCommissionAmount(allProfit)
                .build();
        return Response.success(build);
    }

    /**
     * 用户转化漏斗
     * @param
     * @return
     */
    @GetMapping("userOverviewStatistics")
    public Response userOverviewStatistics(PageQueryDTO queryDTO){
        Integer joinedUser = userService.count(new LambdaQueryWrapper<User>().gt(Objects.nonNull(queryDTO.getStartTime()),
                User::getJoinedTime, queryDTO.getStartTime()).le(Objects.nonNull(queryDTO.getEndTime()),
                User::getJoinedTime, queryDTO.getEndTime()).eq(User::getIsBot, false));
        Integer rechargeUser = conversionRateMapper.userChargeCount(new QueryWrapper<>().
                gt(Objects.nonNull(queryDTO.getStartTime()), "`user`.`joined_time`", queryDTO.getStartTime()).
                le(Objects.nonNull(queryDTO.getEndTime()), "`user`.`joined_time`", queryDTO.getEndTime()));
        Integer betUser = conversionRateMapper.userBetCount(new QueryWrapper<>().
                gt(Objects.nonNull(queryDTO.getStartTime()), "`user`.`joined_time`", queryDTO.getStartTime()).
                le(Objects.nonNull(queryDTO.getEndTime()), "`user`.`joined_time`", queryDTO.getEndTime()).eq("`user`.`is_bot`", false));
        return Response.success(UserOverviewStatistics.builder().joinedUser(joinedUser).rechargeUser(rechargeUser).betUser(betUser).build());
    }

    /**
     * 游戏下注
     * @return
     */
    @GetMapping("gameBet")
    public Response gameBet(){
        List<GameBet> list = gameBetService.list();
        if(CollectionUtils.isEmpty(list)){
            return Response.success();
        }
        Map<String, List<GameBet>> collect = list.stream().collect(Collectors.groupingBy(GameBet::getGameBusiness));
        List<AdminGameBetStatistics> adminGameBetStatistics = new ArrayList<>();
        for (GameBusiness business: GameBusiness.values()) {
            AdminGameBetStatistics statistics = new AdminGameBetStatistics();
            statistics.setGameName(business.getGameName());
            if(collect.containsKey(business.getKey())){
                List<GameBet> gameBets = collect.get(business.getKey());
                statistics.setUserCount(gameBets.size());
            }
            adminGameBetStatistics.add(statistics);
        }
        return Response.success(adminGameBetStatistics);
    }

}
