package com.tl.tgGame.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tl.tgGame.auth.annotation.Uid;
import com.tl.tgGame.common.dto.Response;
import com.tl.tgGame.project.dto.UserUsdtWithdrawDTO;
import com.tl.tgGame.project.entity.Withdrawal;
import com.tl.tgGame.project.enums.UserType;
import com.tl.tgGame.project.service.WithdrawalService;
import com.tl.tgGame.system.ConfigService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("/api/user/withdrawal")
public class WithdrawalController {
    @Resource
    private WithdrawalService withdrawalService;

    /**
     * partner u币提现
     *
     * @param uid
     * @param param
     * @return
     */
    @PostMapping("/withdraw")
    public Response partnerWithdraw(@Uid Long uid, @RequestBody @Valid UserUsdtWithdrawDTO param) {
        return Response.success(withdrawalService.withdraw(uid, UserType.USER, param.getNetwork(), param.getTo(), param.getAmount()));
    }

    /**
     * 获取usdt提现记录
     *
     * @param uid
     * @param page
     * @param size
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/list")
    public Response withdrawalList(@Uid Long uid,
                                       @RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "20") Integer size,
                                       @RequestParam(required = false) LocalDateTime startTime,
                                       @RequestParam(required = false) LocalDateTime endTime) {
        return Response.pageResult(withdrawalService.page(new Page<>(page, size),
                new LambdaQueryWrapper<Withdrawal>().eq(Withdrawal::getUid, uid)
                        .ge(Objects.nonNull(startTime), Withdrawal::getCreateTime, startTime)
                        .le(Objects.nonNull(endTime), Withdrawal::getCreateTime, endTime)
                        .orderByDesc(Withdrawal::getId)
        ));
    }


}
