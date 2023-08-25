package com.tl.tgGame.project.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tl.tgGame.project.dto.ApiGameRecordListDTO;
import com.tl.tgGame.project.dto.ApiRecordListReq;
import com.tl.tgGame.project.entity.Bet;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.service.BetService;
import com.tl.tgGame.project.service.GameService;
import com.tl.tgGame.project.service.UserService;
import com.tl.tgGame.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/21 , 16:18
 */
@Slf4j
@Component
public class FcGameSchedule {


    @Autowired
    private BetService betService;

    @Autowired
    private RedissonClient redisLock;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Scheduled(fixedDelay = 30000)
    @Transactional(rollbackFor = Exception.class)
    public void insertFcBetRecord() {
        LocalDateTime now = LocalDateTime.now();
        String startTime = TimeUtil.americaCharge(now.minusMinutes(15));
        String commonTime = TimeUtil.americaCharge(now);
        String endTime = commonTime;
        Bet record = betService.lambdaQuery().orderByDesc(Bet::getId).last("LIMIT 1").one();
        if (record != null) {
            startTime = record.getPullTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            endTime = record.getPullTime().plusMinutes(15).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (record.getPullTime().plusMinutes(15).isBefore(TimeUtil.parseLocalDateTime(commonTime))) {
                record.setPullTime(TimeUtil.parseLocalDateTime(endTime));
                betService.updateById(record);
            } else {
                endTime = TimeUtil.americaCharge(now);
            }
        }
        String lockKey = "insertBetRecord:lock";
        RLock lock = redisLock.getLock(lockKey);
        if (lock.tryLock()) {
            try {
                ApiRecordListReq req = new ApiRecordListReq();
                req.setStartDate(startTime);
                req.setEndDate(endTime);
                List<ApiGameRecordListDTO> recordList = gameService.getRecordList(req);
                if (CollectionUtils.isEmpty(recordList)) {
                    return;
                }
                betService.addBet(recordList);
            } finally {
                lock.forceUnlock();
            }
        }
    }




    public void fcCommissionPenny(){
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
                    //winlose大于0 代表用户赢钱 系统输钱
                    User user = userService.queryByMemberAccount(bet.getGameAccount());
                    try {
                        if (bet.getWinLose().compareTo(BigDecimal.ZERO) > 0) {

                        } else {
                            agentService.newUserWin(gameRecord, user);
                        }
                    } catch (Exception e) {
                        log.info("fc佣金分成系统异常exception:{},recordId:{}", e, bet.getRecordId());
                    }
                }
            } finally {
                lock.forceUnlock();
            }
        }




    }


}
