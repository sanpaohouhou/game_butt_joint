package com.tl.tgGame.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tl.tgGame.address.AddressService;
import com.tl.tgGame.admin.dto.*;
import com.tl.tgGame.common.dto.Response;
import com.tl.tgGame.project.entity.Recharge;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.entity.UserProfit;
import com.tl.tgGame.project.entity.Withdrawal;
import com.tl.tgGame.project.enums.WithdrawStatus;
import com.tl.tgGame.project.service.RechargeService;
import com.tl.tgGame.project.service.UserProfitService;
import com.tl.tgGame.project.service.UserService;
import com.tl.tgGame.project.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
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


    /**
     * 用户列表
     * @param req
     * @return
     */
    @GetMapping("userList")
    public Response userList(AdminUserListReq req) {
        Page<User> page = userService.page(new Page<>(req.getPage(), req.getSize()), new LambdaQueryWrapper<User>()
                .eq(Objects.nonNull(req.getGameAccount()), User::getGameAccount, req.getGameAccount())
                .eq(Objects.nonNull(req.getPartnerId()), User::getPartnerId, req.getPartnerId())
                .ge(Objects.nonNull(req.getStartTime()), User::getJoinedTime, req.getStartTime())
                .le(Objects.nonNull(req.getEndTime()), User::getJoinedTime, req.getEndTime()));
        // TODO: 2023/8/8 封装内容
        return Response.pageResult(page);
    }

    /**
     * 用户转账
     * @param req
     * @return
     */
    @GetMapping("/recharge/list")
    public Response chargeRecord(AdminUserRechargeReq req
    ) {
        return Response.pageResult(rechargeService.page(new Page<>(req.getPage(), req.getSize()),
                new LambdaQueryWrapper<Recharge>()
                        .eq(Objects.nonNull(req.getUserId()), Recharge::getUserId, req.getUserId())
                        .ge(Objects.nonNull(req.getStartTime()), Recharge::getCreateTime, req.getStartTime())
                        .le(Objects.nonNull(req.getEndTime()), Recharge::getCreateTime, req.getEndTime())
                        .orderByDesc(Recharge::getId)
        ));
    }

    /**
     * 用户提现
     * @return
     */
    @GetMapping("/withdrawal/list")
    public Response withdrawalList(AdminUserWithdrawalReq req) {
        return Response.pageResult(withdrawalService.page(new Page<>(req.getPage(), req.getSize()),
                new LambdaQueryWrapper<Withdrawal>().eq(Objects.nonNull(req.getUserId()), Withdrawal::getUid, req.getUserId())
                        .eq(Objects.nonNull(req.getStatus()), Withdrawal::getStatus, req.getStatus())
                        .ge(Objects.nonNull(req.getStartTime()), Withdrawal::getCreateTime, req.getStartTime())
                        .le(Objects.nonNull(req.getEndTime()), Withdrawal::getCreateTime, req.getEndTime())
                        .orderByDesc(Withdrawal::getId)
        ));
    }

    /**
     * 用户推广
     */
    @GetMapping("userExtend/list")
    public Response userExtend(AdminUserExtendReq req){
        Page<User> page = userService.page(new Page<>(req.getPage(), req.getSize()));
        return Response.pageResult(page);
    }

    /**
     * 推广详情
     */
    @GetMapping("userExtendInfo")
    public Response userExtendInfo(Long userId){
        return Response.success();
    }

    /**
     * 用户获利
     */
    @GetMapping("userMakeProfit")
    public Response userMakeProfit(AdminUserMakeProfitReq req){
        Page<UserProfit> page = userProfitService.page(new Page<>(req.getPage(), req.getSize()),
                new LambdaQueryWrapper<UserProfit>()
                        .eq(Objects.nonNull(req.getGameAccount()), UserProfit::getGameAccount, req.getGameAccount())
                        .eq(Objects.nonNull(req.getGameName()), UserProfit::getGameName, req.getGameName())
                        .ge(Objects.nonNull(req.getStartTime()), UserProfit::getCreateTime, req.getStartTime())
                        .le(Objects.nonNull(req.getEndTime()), UserProfit::getCreateTime, req.getEndTime()));
        return Response.pageResult(page);
    }

}
