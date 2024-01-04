package com.tl.tgGame.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tl.tgGame.admin.dto.AdminAgentListReq;
import com.tl.tgGame.project.entity.Agent;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/11 , 14:30
 */
public interface AgentService extends IService<Agent> {

    String login(String username, String password);

    Agent checkLogin(String username, String password);

    Agent addAgent(String agentName, String userName, String mobile,
                   String password,String gameAccount, BigDecimal dividendRate,String remark,Long pAgentId);

    Agent updateAgent(Long agentId,String agentName,String password,String remark,String mobile,BigDecimal dividendRate);

    Page<Agent> queryByList(AdminAgentListReq req);

    Agent queryByUserId(Long userId);



}
