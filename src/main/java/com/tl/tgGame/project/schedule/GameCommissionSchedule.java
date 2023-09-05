package com.tl.tgGame.project.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tl.tgGame.project.entity.Bet;
import com.tl.tgGame.project.entity.EgBet;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.entity.WlBet;
import com.tl.tgGame.project.service.BetService;
import com.tl.tgGame.project.service.EgBetService;
import com.tl.tgGame.project.service.GameService;
import com.tl.tgGame.project.service.WlBetService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/4 , 11:48
 */

@Slf4j
@Component
public class GameCommissionSchedule {

    @Autowired
    private GameService gameService;

    @Autowired
    private BetService betService;

    @Autowired
    private EgBetService egBetService;

    @Autowired
    private WlBetService wlBetService;

    @Autowired
    private RedissonClient redisLock;

    public void fcCommission() {
        String lockKey = "fcCommissionPenny:lock";
        RLock lock = redisLock.getLock(lockKey);
        if (lock.tryLock()) {
            try {
                log.info("fc佣金分成开始-----");
                List<Bet> bets = betService.list(new LambdaQueryWrapper<Bet>().eq(Bet::getHasSettled, false));
                if (CollectionUtils.isEmpty(bets)) {
                    return;
                }
                for (Bet bet : bets) {
                    try {
                        betService.fcCommission(bet);
                    } catch (Exception e) {
                        log.info("fc佣金分成系统异常exception:{},recordId:{}", e, bet.getRecordId());
                    }
                }
            } finally {
                lock.forceUnlock();
            }
        }
    }

    public void egCommission() {
        String lockKey = "egCommissionPenny:lock";
        RLock lock = redisLock.getLock(lockKey);
        if (lock.tryLock()) {
            try {
                log.info("eg佣金分成开始-----");
                List<EgBet> bets = egBetService.list(new LambdaQueryWrapper<EgBet>().eq(EgBet::getHasSettled, false));
                if (CollectionUtils.isEmpty(bets)) {
                    return;
                }
                for (EgBet bet : bets) {
                    try {
                        egBetService.egCommission(bet);
                    } catch (Exception e) {
                        log.info("eg佣金分成系统异常exception:{},betId:{}", e, bet.getId());
                    }
                }
            } finally {
                lock.forceUnlock();
            }
        }
    }

    public void wlCommission() {
        String lockKey = "wlCommissionPenny:lock";
        RLock lock = redisLock.getLock(lockKey);
        if (lock.tryLock()) {
            try {
                log.info("wl佣金分成开始-----");
                List<WlBet> bets = wlBetService.list(new LambdaQueryWrapper<WlBet>().eq(WlBet::getHasSettled, false));
                if (CollectionUtils.isEmpty(bets)) {
                    return;
                }
                for (WlBet bet : bets) {
                    try {
                        wlBetService.wlCommission(bet);
                    } catch (Exception e) {
                        log.info("wl佣金分成系统异常exception:{},recordId:{}", e, bet.getRecordId());
                    }
                }
            } finally {
                lock.forceUnlock();
            }
        }
    }
}
