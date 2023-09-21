package com.tl.tgGame.agent.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tl.tgGame.admin.dto.AddAgentDTO;
import com.tl.tgGame.admin.dto.AdminAgentListReq;
import com.tl.tgGame.admin.dto.AdminQueryBetReq;
import com.tl.tgGame.auth.annotation.Uid;
import com.tl.tgGame.common.dto.Response;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.dto.UserUsdtWithdrawDTO;
import com.tl.tgGame.project.entity.*;
import com.tl.tgGame.project.enums.BusinessEnum;
import com.tl.tgGame.project.enums.UserCommissionType;
import com.tl.tgGame.project.enums.UserType;
import com.tl.tgGame.project.service.*;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import com.tl.tgGame.wallet.WalletAPI;
import com.tl.tgGame.wallet.dto.RechargeCheckDTO;
import com.tl.tgGame.wallet.dto.SingleResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/11 , 16:13
 */
@RestController
@RequestMapping("/api/agent/agent")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private UserCommissionService userCommissionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private WalletAPI walletAPI;

    @Autowired
    private GameBetService gameBetService;

    @Autowired
    private CurrencyLogService currencyLogService;

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private WithdrawalService withdrawalService;

    /**
     * 获取首页统计数据
     */
    @GetMapping("homeStatistics")
    public Response homeStatistics(@Uid Long agentId) {
        Agent agent = agentService.getById(agentId);
        if (agent == null) {
            ErrorEnum.OBJECT_NOT_FOUND.throwException();
        }
        agent.setDividendProfit(userCommissionService.sumAmount(agent.getUserId(), UserCommissionType.DIVIDEND, null, null, null));
        agent.setCurrency(currencyService.getOrCreate(agent.getId(), UserType.AGENT));
        agent.setTeamNumber(userService.teamNumber(agent.getUserId()));
        agent.setInviteUrl(configService.get(ConfigConstants.BOT_GROUP_INVITE_LINK ) + "?start=" + agent.getGameAccount());
        agent.setAddress(walletAPI.getUserAddress(agent.getId()));
        return Response.success(agent);
    }

    /**
     * 添加二级代理商
     */
    @PostMapping("/addAgent")
    public Response addAgent(@Uid Long agentId, @RequestBody @Valid AddAgentDTO addAgentDTO) {
        return Response.success(agentService.addAgent(addAgentDTO.getAgentName(), addAgentDTO.getUserName(),
                addAgentDTO.getMobile(), addAgentDTO.getPassword(), addAgentDTO.getGameAccount(), addAgentDTO.getDividendRate(),
                addAgentDTO.getRemark(), agentId));
    }

    @PostMapping("updateAgent")
    public Response updateAgent(@Uid Long agentId,@RequestBody @Valid AddAgentDTO addAgentDTO){
        Agent agent = agentService.getById(addAgentDTO.getId());
        if(!agentId.equals(agent.getInviteId())){
            ErrorEnum.PARAM_ERROR.throwException();
        }
        return Response.success(agentService.updateAgent(addAgentDTO.getId(),addAgentDTO.getAgentName(),
                addAgentDTO.getPassword(),
                addAgentDTO.getRemark(), addAgentDTO.getMobile(), addAgentDTO.getDividendRate()));
    }

    /**
     * 代理管理
     */
    @GetMapping("queryByList")
    public Response queryByList(@Uid Long agentId,AdminAgentListReq req) {
        req.setPAgentId(agentId);
        req.setLevel(2);
        return Response.pageResult(agentService.queryByList(req));
    }


    /**
     * 转账完成
     */
    @PostMapping("rechargeSuccess")
    public Response rechargeSuccess(@Uid Long agentId) {
//        Agent agent = agentService.getById(agentId);
        RechargeCheckDTO rechargeCheckDTO = new RechargeCheckDTO();
        rechargeCheckDTO.setChainType("TRON");
        rechargeCheckDTO.setUid(agentId);
        SingleResponse<Boolean> rechargeCheck = walletAPI.rechargeCheck(rechargeCheckDTO);
        return Response.success(rechargeCheck.getData());
    }

    /**
     * 代理商团队下注记录
     */
    @GetMapping("team/betList")
    public Response agentTeamBetList(Long agentId, AdminQueryBetReq req) {

        List<User> users = userService.list(new LambdaQueryWrapper<User>()
                .like(User::getInviteChain, req.getAgentUserId()).eq(Objects.nonNull(req.getUserId()),User::getId,req.getUserId()));
        List<Long> userIds = users.stream().filter(i-> i.getAgentId() == null || !i.getAgentId().equals(agentId)).map(User::getId).collect(Collectors.toList());

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

    /**
     * 保证金列表
     */
    @GetMapping("bail/list")
    public Response bailList(@Uid Long agentId,
                             @RequestParam(defaultValue = "1", value = "page") Integer page,
                             @RequestParam(defaultValue = "10", value = "size") Integer size,
                             @RequestParam(required = false) BusinessEnum business,
                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        Agent agent = agentService.getById(agentId);
        Page<CurrencyLog> pages = currencyLogService.lambdaQuery().eq(CurrencyLog::getUid, agent.getId())
                .eq(Objects.nonNull(business), CurrencyLog::getBusiness, business)
                .notIn(CurrencyLog::getBusiness, Arrays.asList(BusinessEnum.FC_RECHARGE,BusinessEnum.FC_WITHDRAWAL,
                        BusinessEnum.WL_RECHARGE,BusinessEnum.WL_WITHDRAWAL,BusinessEnum.EG_RECHARGE,BusinessEnum.EG_WITHDRAWAL))
                .eq(CurrencyLog::getUserType,UserType.AGENT)
                .ge(Objects.nonNull(startTime), CurrencyLog::getCreateTime, startTime)
                .le(Objects.nonNull(endTime), CurrencyLog::getCreateTime, endTime)
                .orderByDesc(CurrencyLog::getId)
                .page(new Page<>(page, size));
        List<CurrencyLog> records = pages.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return Response.pageResult(pages);
        }
        List<CurrencyLog> list = new ArrayList<>();
        for (CurrencyLog currencyLog : records) {
            if (currencyLog.getBusiness() != null && currencyLog.getBusiness().equals(BusinessEnum.RECHARGE)) {
                Recharge recharge = rechargeService.getById(currencyLog.getSn());
                currencyLog.setFromAddress(recharge.getFromAddress());
                currencyLog.setToAddress(recharge.getToAddress());
                currencyLog.setNetwork(recharge.getNetwork());
                currencyLog.setHash(recharge.getHash());
            }
            if (currencyLog.getBusiness() != null && currencyLog.getBusiness().equals(BusinessEnum.WITHDRAW)) {
                Withdrawal withdrawal = withdrawalService.getById(currencyLog.getSn());
                currencyLog.setFromAddress(withdrawal.getFromAddress());
                currencyLog.setToAddress(withdrawal.getToAddress());
                currencyLog.setNetwork(withdrawal.getNetwork());
                currencyLog.setHash(withdrawal.getHash());
                currencyLog.setStatus(withdrawal.getStatus());
            }
            currencyLog.setBalance(currencyLog.getAmount().add(currencyLog.getBalance()));
            list.add(currencyLog);
        }
        pages.setRecords(list);
        return Response.pageResult(pages);
    }

    @PostMapping("/withdraw")
    public Response withdraw(@Uid Long agentId, @RequestBody @Valid UserUsdtWithdrawDTO param) {
        return Response.success(withdrawalService.withdraw(agentId, UserType.AGENT, param.getNetwork(), param.getTo(), param.getAmount()));
    }


}
