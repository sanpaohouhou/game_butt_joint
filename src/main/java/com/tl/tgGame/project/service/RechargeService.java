package com.tl.tgGame.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tl.tgGame.callback.entity.BscBep20Tx;
import com.tl.tgGame.callback.entity.EthErc20Tx;
import com.tl.tgGame.callback.entity.TronTrc20Tx;
import com.tl.tgGame.project.entity.Currency;
import com.tl.tgGame.project.entity.Recharge;
import com.tl.tgGame.project.enums.Network;
import com.tl.tgGame.project.enums.UserType;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户充值提现记录表 服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-07-12
 */
public interface RechargeService extends IService<Recharge> {

    Recharge addRecharge(Long uid, BigDecimal amount, UserType userType, String fromAddress, String toAddress, String hash, Network network, String screen, String note, Currency currency);

    /**
     * 处理波场转账事件
     *
     * @param tx 链上记录
     */
    void tronTxHandle(TronTrc20Tx tx);

    /**
     * 处理eth转账事件
     *
     * @param tx 链上记录
     */
    void ethTxHandle(EthErc20Tx tx);

    /**
     * 处理bsc转账事件
     *
     * @param tx 链上记录
     */
    void bscTxHandle(BscBep20Tx tx);

    BigDecimal sumRecharge(Long userId, UserType userType, LocalDateTime startTime,LocalDateTime endTime);

    Integer countRechargeNumber(List<Long> userIds,LocalDateTime startTime,LocalDateTime endTime);

    Integer countJuniorRechargeNumber(Long inviteUserId,Long userId,LocalDateTime startTime,LocalDateTime endTime);

    BigDecimal sumJuniorRechargeAmount(Long inviteUserId,Long userId,LocalDateTime startTime,LocalDateTime endTime);

}
