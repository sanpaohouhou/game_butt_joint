package com.tl.tgGame.project.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tl.tgGame.project.dto.*;
import com.tl.tgGame.project.entity.Bet;
import com.tl.tgGame.project.entity.EgBet;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.entity.WlBet;
import com.tl.tgGame.project.service.*;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import com.tl.tgGame.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
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
public class GameRecordSchedule {

    @Autowired
    private BetService betService;

    @Autowired
    private RedissonClient redisLock;

    @Autowired
    private GameService gameService;

    @Autowired
    private EgBetService egBetService;
    @Autowired
    private WlBetService wlBetService;
    @Autowired
    private ConfigService configService;


    @Scheduled(fixedDelay = 150000)
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


    @Scheduled(fixedDelay = 150000)
    public void insertEgBetRecord() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusMinutes(60);
        LocalDateTime endTime = now;
        EgBet egBet = egBetService.lambdaQuery().orderByDesc(EgBet::getId).last("LIMIT 1").one();
        if (egBet != null) {
            LocalDateTime pullTime = egBet.getPullTime();
            startTime = pullTime.minusMinutes(10);
            endTime = pullTime.plusMinutes(50);
            if (endTime.isBefore(now)) {
                egBet.setPullTime(endTime);
                egBetService.updateById(egBet);
            } else {
                endTime = now;
            }
        }
        String lockKey = "insertEgBetRecord:lock";
        RLock lock = redisLock.getLock(lockKey);
        if (lock.tryLock()) {
            try {
                Long end = TimeUtil.getTimestamp(endTime);
                Long start = TimeUtil.getTimestamp(startTime);
                String merch = configService.get(ConfigConstants.EG_AGENT_CODE);
                ApiEgRoundRecordTimeReq req = new ApiEgRoundRecordTimeReq();
//                req.setRoundCode("1");
                req.setEnd(end.toString());
                req.setStart(start.toString());
                req.setPage("1");
                req.setPageSize("1000");
                req.setMerch(merch);
                ApiEgRoundRecordRes res = gameService.egRoundRecordByTime(req);
                if (res == null || CollectionUtils.isEmpty(res.getData())) {
                    return;
                }
                egBetService.addEgBet(res.getData(), endTime.plusSeconds(1));
            } finally {
                lock.forceUnlock();
            }
        }
    }

    @Scheduled(fixedDelay = 150000)
    public void insertWlBetRecord() {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusMinutes(60);
        WlBet wlBet = wlBetService.lambdaQuery().orderByDesc(WlBet::getId).last("LIMIT 1").one();
        if (wlBet != null) {
            startTime = wlBet.getPullTime().minusMinutes(10);
            endTime = wlBet.getPullTime().plusMinutes(50);
            if (endTime.isBefore(LocalDateTime.now())) {
                wlBet.setPullTime(endTime);
                wlBetService.updateById(wlBet);
            } else {
                endTime = LocalDateTime.now();
            }
        }
        String lockKey = "insertWlBetRecord:lock";
        RLock lock = redisLock.getLock(lockKey);
        if (lock.tryLock()) {
            try {
                ApiWlGameResponse response = gameService.wlGameRecord(startTime, endTime);
                if (response.getCode() != 0) {
                    return;
                }
                wlBetService.addWlBet(response.getData());
            } finally {
                lock.forceUnlock();
            }
        }
    }
}
