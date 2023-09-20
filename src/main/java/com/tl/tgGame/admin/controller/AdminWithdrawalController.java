package com.tl.tgGame.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tl.tgGame.admin.dto.AuditDTO;
import com.tl.tgGame.admin.dto.WithdrawalUploadDTO;
import com.tl.tgGame.common.dto.Response;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.entity.Withdrawal;
import com.tl.tgGame.project.enums.UserType;
import com.tl.tgGame.project.enums.WithdrawStatus;
import com.tl.tgGame.project.service.AgentService;
import com.tl.tgGame.project.service.UserService;
import com.tl.tgGame.project.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/6 , 10:41
 */
@RestController
@RequestMapping("/api/admin/withdrawal")
public class AdminWithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private UserService userService;

    /**
     * 获取usdt提现记录
     *
     * @param page
     * @param size
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/withdrawal/list")
    public Response withdrawalList(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "20") Integer size,
                                   @RequestParam(required = false) Long userId,
                                   @RequestParam(required = false) Long agentId,
                                   @RequestParam(required = false) Long id,
                                   @RequestParam(required = false) String gameAccount,
                                   @RequestParam(required = false) UserType userType,
                                   @RequestParam(required = false) WithdrawStatus status,
                                   @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                   @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        if (!StringUtils.isEmpty(gameAccount)) {
            User user = userService.queryByMemberAccount(gameAccount);
            userId = user.getId();
        }
        if (agentId != null) {
            User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getAgentId, agentId));
            if (Objects.isNull(user)) {
                ErrorEnum.OBJECT_NOT_FOUND.throwException();
            }
            userId = user.getId();
        }
        Page<Withdrawal> page1 = withdrawalService.page(new Page<>(page, size),
                new LambdaQueryWrapper<Withdrawal>().eq(Objects.nonNull(userId), Withdrawal::getUid, userId)
                        .eq(Objects.nonNull(status), Withdrawal::getStatus, status)
                        .eq(Objects.nonNull(id), Withdrawal::getId, id)
                        .eq(Objects.nonNull(userType), Withdrawal::getUserType, userType)
                        .ge(Objects.nonNull(startTime), Withdrawal::getCreateTime, startTime)
                        .le(Objects.nonNull(endTime), Withdrawal::getCreateTime, endTime)
                        .orderByDesc(Withdrawal::getId)
        );
        List<Withdrawal> records = page1.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return Response.pageResult(page1);
        }
        List<Withdrawal> list = new ArrayList<>();
        for (Withdrawal withdrawal : records) {
            User user = userService.getById(withdrawal.getUid());
            withdrawal.setGameAccount(user.getGameAccount());
            list.add(withdrawal);
        }
        page1.setRecords(list);
        return Response.pageResult(page1);
    }

    //弃用
//    @GetMapping("/agent/withdrawal/list")
//    public Response agentWithdrawalList(@RequestParam(defaultValue = "1") Integer page,
//                                        @RequestParam(defaultValue = "20") Integer size,
//                                        @RequestParam(required = false) Long userId,
//                                        @RequestParam(required = false) Long agentId,
//                                        @RequestParam(required = false) Long id,
//                                        @RequestParam(required = false) WithdrawStatus status,
//                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
//                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
//        return Response.pageResult(withdrawalService.agentWithdrawalList(page, size, userId, agentId, id, status, startTime, endTime));
//    }

    @PostMapping("/withdrawal/audit")
    public Response withdrawAudit(@RequestBody @Valid AuditDTO param) {
        withdrawalService.audit(param.getId(), param.getResult(), param.getNote());
        return Response.success();
    }

    @PostMapping("/withdrawal/upload")
    public Response withdrawalUpload(@RequestBody @Valid WithdrawalUploadDTO dto) {
        withdrawalService.upload(dto.getId(), dto.getImage(), dto.getHash());
        return Response.success();
    }
}
