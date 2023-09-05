package com.tl.tgGame.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tl.tgGame.project.entity.UserCommission;
import com.tl.tgGame.project.enums.UserCommissionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/18 , 11:32
 */
public interface UserCommissionService extends IService<UserCommission>{

    /**
     * 查询反佣/返水金额
     */
    BigDecimal sumAmount(Long userId, UserCommissionType type, String gameBusiness, LocalDateTime startTime, LocalDateTime endTime);

    Boolean insertUserCommission(Long userId,Long fromUserId, String gameId, String gameName, UserCommissionType type,
                                 String gameBusiness, BigDecimal profit, BigDecimal rate, BigDecimal actualAmount);
}
