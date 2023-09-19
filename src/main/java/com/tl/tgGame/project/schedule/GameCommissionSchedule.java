package com.tl.tgGame.project.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tl.tgGame.project.entity.*;
import com.tl.tgGame.project.service.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    private ApiGameService apiGameService;

    @Autowired
    private GameBetService gameBetService;

    @Autowired
    private RedissonClient redisLock;

//    @Scheduled(fixedDelay = 30000)
    public void commission() {
        String lockKey = "commissionPenny:lock";
        RLock lock = redisLock.getLock(lockKey);
        if (lock.tryLock()) {
            try {
                log.info("佣金分成开始");
                List<GameBet> bets = gameBetService.list(new LambdaQueryWrapper<GameBet>().eq(GameBet::getHasSettled, false));
                if (CollectionUtils.isEmpty(bets)) {
                    return;
                }
                for (GameBet bet : bets) {
                    try {
                        if(bet.getProfit().compareTo(BigDecimal.ZERO) > 0){
                            gameBetService.winCommission(bet);
                        }else {
                            gameBetService.loseCommission(bet);
                        }
                    } catch (Exception e) {
                        log.info("佣金分成系统异常exception:{},recordId:{}", e, bet.getRecordId());
                    }
                }
            } finally {
                lock.forceUnlock();
            }
        }
    }

}
