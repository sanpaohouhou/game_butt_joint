package com.tl.tgGame.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tl.tgGame.admin.dto.*;
import com.tl.tgGame.common.dto.Response;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.entity.*;
import com.tl.tgGame.project.enums.BusinessEnum;
import com.tl.tgGame.project.enums.UserCommissionType;
import com.tl.tgGame.project.enums.UserType;
import com.tl.tgGame.project.service.*;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/11 , 15:58
 */
@RestController
@RequestMapping("/api/admin/agent")
public class AdminAgentController {

    @Autowired
    private AgentService agentService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private UserCommissionService userCommissionService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private UserService userService;

    @Autowired
    private GameBetService gameBetService;

    @Autowired
    private ConfigService configService;

    @PostMapping("addAgent")
    public Response addAgent(@RequestBody @Valid AddAgentDTO addAgentDTO) {
        return Response.success(agentService.addAgent(addAgentDTO.getAgentName(), addAgentDTO.getUserName(),
                addAgentDTO.getMobile(), addAgentDTO.getPassword(), addAgentDTO.getGameAccount(), addAgentDTO.getDividendRate(),
                addAgentDTO.getRemark(), null));
    }

    @PostMapping("updateAgent")
    public Response updateAgent(@RequestBody @Valid AddAgentDTO addAgentDTO){
        return Response.success(agentService.updateAgent(addAgentDTO.getId(),addAgentDTO.getAgentName(),
               addAgentDTO.getPassword(),addAgentDTO.getRemark(), addAgentDTO.getMobile(), addAgentDTO.getDividendRate()));
    }

    @GetMapping("queryByList")
    public Response queryByList(AdminAgentListReq req) {
        Page<Agent> page = agentService.page(new Page<>(req.getPage(), req.getSize()), new LambdaQueryWrapper<Agent>()
                .eq(Agent::getLevel, req.getLevel())
                .eq(Objects.nonNull(req.getPAgentId()),Agent::getInviteId,req.getPAgentId())
                .eq(Objects.nonNull(req.getAgentId()), Agent::getId, req.getAgentId())
                .like(!StringUtils.isEmpty(req.getAgentName()), Agent::getAgentName, req.getAgentName())
                .orderByDesc(Agent::getCreateTime));
        List<Agent> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return Response.pageResult(page);
        }
        List<Agent> list = new ArrayList<>();
        String inviteLink = configService.get(ConfigConstants.BOT_GROUP_INVITE_LINK);
        for (Agent agent : records) {
            Currency currency = currencyService.get(agent.getUserId(), UserType.AGENT);
            agent.setInviteUrl(inviteLink + "?start="+agent.getGameAccount());
            agent.setCurrency(currency);
            agent.setDividendProfit(userCommissionService.sumAmount(agent.getUserId(), UserCommissionType.DIVIDEND,null,null,null));
            list.add(agent);
        }
        page.setRecords(list);
        return Response.pageResult(page);
    }

    @GetMapping("/{agentId}")
    public Response getAgent(@PathVariable Long agentId) {
        Agent agent = agentService.getById(agentId);
        if (agent == null) {
            ErrorEnum.OBJECT_NOT_FOUND.throwException();
        }
        agent.setCurrency(currencyService.get(agent.getUserId(), UserType.AGENT));
        agent.setDividendProfit(userCommissionService.sumAmount(agent.getUserId(), UserCommissionType.DIVIDEND, null, null, null));
        return Response.success(agent);
    }

    /**
     * 保证金充值
     * @param dto
     * @return
     */
    @PostMapping("/partner/recharge")
    @Transactional(rollbackFor = Exception.class)
    public Response partnerRecharge(@RequestBody @Valid ChargeDTO dto) {
        Agent agent = agentService.queryByUserId(dto.getUserId());
        if (agent == null) {
            ErrorEnum.USER_NOT_JOIN.throwException("代理商不存在");
        }
        Currency currency = currencyService.get(agent.getUserId(), UserType.AGENT);
        Recharge recharge = rechargeService.addRecharge(
                agent.getUserId(),
                dto.getAmount(),
                UserType.AGENT,
                null,
                dto.getAddress(),
                dto.getHash(),
                dto.getNetwork(),
                dto.getScreen(),
                dto.getNote(),currency);
        currencyService.increase(agent.getUserId(), UserType.AGENT, BusinessEnum.RECHARGE, dto.getAmount(), recharge.getId(), "代理商保证金充值");
        return Response.success(recharge);
    }

    /**
     * 保证金提现
     * @param dto
     * @return
     */
    @PostMapping("/partner/withdraw")
    @Transactional(rollbackFor = Exception.class)
    public Response partnerWithdraw(@RequestBody @Valid ChargeDTO dto) {
        Agent agent = agentService.queryByUserId(dto.getUserId());
        if (agent == null) {
            ErrorEnum.USER_NOT_JOIN.throwException("代理商不存在");
        }
        Withdrawal withdrawal = withdrawalService.addWithdrawal(
                dto.getUserId(),
                dto.getAmount(),
                UserType.AGENT,
                null,
                dto.getAddress(),
                dto.getHash(),
                dto.getNetwork(),
                dto.getScreen(),
                dto.getNote()
        );
        currencyService.withdraw(agent.getUserId(), UserType.AGENT, BusinessEnum.WITHDRAW, dto.getAmount(), withdrawal.getId(), "代理商保证金提现");
        return Response.success(withdrawal);
    }

    /**
     * 团队下注明细
     */
    @GetMapping("betRecord")
    public Response betRecord(AdminQueryBetReq req) {
        List<Long> userIds = new ArrayList<>();
        if (req.getAgentUserId() != null) {
            List<User> list = userService.list(new LambdaQueryWrapper<User>().like(User::getInviteChain, req.getAgentUserId()));
            userIds = list.stream().filter(i -> !Objects.equals(i.getInviteChain(), String.valueOf(req.getAgentUserId())))
                    .map(User::getId).collect(Collectors.toList());
        }
        Page<GameBet> page = gameBetService.page(new Page<>(req.getPage(), req.getSize()),
                new LambdaQueryWrapper<GameBet>().in(!CollectionUtils.isEmpty(userIds), GameBet::getUserId, userIds)
                        .ge(req.getStartTime() != null, GameBet::getRecordTime, req.getStartTime())
                        .le(req.getEndTime() != null, GameBet::getRecordTime, req.getEndTime())
                        .orderByDesc(GameBet::getId));
        List<GameBet> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return Response.pageResult(page);
        }
        List<GameBet> list = new ArrayList<>();
        for (GameBet gameBet : records) {
            if (req.getAgentUserId() != null) {
                UserCommission one = userCommissionService.getOne(new LambdaQueryWrapper<UserCommission>()
                        .eq(UserCommission::getBetId, gameBet.getId())
                        .eq(UserCommission::getType, UserCommissionType.DIVIDEND)
                        .eq(UserCommission::getUserId, req.getAgentUserId()));
                if (one != null) {
                    gameBet.setDividendAmount(one.getProfit());
                }
            }
            User user = userService.getById(gameBet.getUserId());
            if (user != null) {
                String inviteChain = user.getInviteChain();
                String[] split = inviteChain.split(":");
                for (int i = 0; i < split.length; i++) {
                    if (String.valueOf(gameBet.getUserId()).equals(split[i])) {
                        gameBet.setLevel(i);
                        break;
                    }
                }
            }
            list.add(gameBet);
        }
        page.setRecords(list);
        return Response.pageResult(page);
    }
}
