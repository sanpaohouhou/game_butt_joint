package com.tl.tgGame.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tl.tgGame.admin.dto.AdminAgentListReq;
import com.tl.tgGame.auth.service.AuthTokenService;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.entity.Agent;
import com.tl.tgGame.project.entity.Currency;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.enums.UserCommissionType;
import com.tl.tgGame.project.enums.UserType;
import com.tl.tgGame.project.mapper.AgentMapper;
import com.tl.tgGame.project.service.AgentService;
import com.tl.tgGame.project.service.CurrencyService;
import com.tl.tgGame.project.service.UserCommissionService;
import com.tl.tgGame.project.service.UserService;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import com.tl.tgGame.util.IdGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/11 , 14:30
 */
@Slf4j
@Service
public class AgentServiceImpl extends ServiceImpl<AgentMapper, Agent> implements AgentService {

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private UserCommissionService userCommissionService;

    @Autowired
    private AuthTokenService authTokenService;

    @Override
    public String login(String username, String password) {
        return authTokenService.login(checkLogin(username,password).getId());
    }

    @Override
    public Agent checkLogin(String username, String password) {
        Agent agent = lambdaQuery().eq(Agent::getUserName, username).eq(Agent::getEnabled, true).one();
        if (agent == null || !passwordEncoder.matches(password, agent.getPassword())) {
            ErrorEnum.LOGIN_FAIL.throwException();
        }
        return agent;
    }

    @Override
    public Agent addAgent(String agentName, String userName, String mobile, String password, String gameAccount,
                          BigDecimal dividendRate, String remark, Long pAgentId) {
        Agent one = getOne(new LambdaQueryWrapper<Agent>().eq(Agent::getAgentName, agentName));
        if (one != null) {
            ErrorEnum.REPEAT_SET_AGENT.throwException();
        }
        User user = userService.queryByMemberAccount(gameAccount);
        if (user == null) {
            ErrorEnum.USER_NOT_JOIN.throwException();
        }
        if(dividendRate.compareTo(BigDecimal.valueOf(1L)) >= 0){
            ErrorEnum.PARAM_ERROR.throwException("分红占比请小于1");
        }
        Long id = idGeneratorService.incrementId();
        String inviteChain = id.toString();
        Integer level = 1;
        String pInviteChain = user.getId().toString();
        Long pInviteUser = null;
        if (pAgentId != null) {
            User one1 = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getAgentId, pAgentId));
            pInviteChain = one1.getInviteChain() + ":" + pInviteChain;
            pInviteUser = one1.getId();
            inviteChain = pAgentId + ":" + inviteChain;
            level = 2;
        }
        Agent build = Agent.builder()
                .id(id)
                .agentName(agentName)
                .inviteChain(inviteChain)
                .inviteId(pAgentId)
                .createTime(LocalDateTime.now())
                .dividendRate(dividendRate)
                .mobile(mobile)
                .password(passwordEncoder.encode(password))
                .userId(user.getId())
                .userName(userName)
                .level(level)
                .enabled(true)
                .hasDeleted(false)
                .remark(remark)
                .gameAccount(gameAccount)
                .build();
        if (!save(build)) {
            ErrorEnum.SYSTEM_ERROR.throwException();
        }
        user.setHasAgent(true);
        user.setAgentId(id);
        user.setInviteUser(pInviteUser);
        user.setInviteChain(pInviteChain);
        if (!userService.updateById(user)) {
            ErrorEnum.SYSTEM_ERROR.throwException();
        }
        return build;
    }

    @Override
    public Agent updateAgent(Long agentId, String agentName,String password,String remark, String mobile, BigDecimal dividendRate) {
        Agent agent = getById(agentId);
        if (agent == null) {
            ErrorEnum.OBJECT_NOT_FOUND.throwException();
        }
        if(dividendRate.compareTo(BigDecimal.valueOf(1L)) >= 0){
            ErrorEnum.PARAM_ERROR.throwException("分红占比请小于1");
        }
        if(!StringUtils.isEmpty(password)){
            agent.setPassword(passwordEncoder.encode(password));
        }
        agent.setAgentName(agentName);
        agent.setRemark(remark);
        agent.setMobile(mobile);
        agent.setDividendRate(dividendRate);
        boolean update = updateById(agent);
        if (!update) {
            ErrorEnum.SYSTEM_ERROR.throwException();
        }
        return agent;
    }

    @Override
    public Page<Agent> queryByList(AdminAgentListReq req) {
        Page<Agent> page = page(new Page<>(req.getPage(), req.getSize()), new LambdaQueryWrapper<Agent>()
                .eq(Agent::getLevel, req.getLevel())
                .eq(Objects.nonNull(req.getPAgentId()),Agent::getInviteId,req.getPAgentId())
                .eq(Objects.nonNull(req.getAgentId()), Agent::getId, req.getAgentId())
                .like(!StringUtils.isEmpty(req.getAgentName()), Agent::getAgentName, req.getAgentName())
                .orderByDesc(Agent::getCreateTime));
        List<Agent> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return page;
        }
        List<Agent> list = new ArrayList<>();
        String inviteLink = configService.get(ConfigConstants.BOT_GROUP_INVITE_LINK);
        for (Agent agent : records) {
            Currency currency = currencyService.get(agent.getUserId(), UserType.AGENT);
            agent.setInviteUrl(inviteLink + "?start="+agent.getGameAccount());
            agent.setCurrency(currency);
            agent.setDividendProfit(userCommissionService.sumAmount(agent.getUserId(), UserCommissionType.DIVIDEND,null,null,null));
            if(agent.getLevel().equals(2)){
                Agent agent1 = getById(agent.getInviteId());
                if(agent1 != null){
                    agent.setDividendRate(agent1.getDividendRate());
                }
            }
            list.add(agent);
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public Agent queryByUserId(Long userId) {
        return getOne(new LambdaQueryWrapper<Agent>().eq(Agent::getUserId,userId).eq(Agent::getEnabled,true).eq(Agent::getHasDeleted,false));
    }
}
