package com.tl.tgGame.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tl.tgGame.common.lock.RedisLock;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.dto.ApiGameRecordListDTO;
import com.tl.tgGame.project.entity.Bet;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.mapper.BetMapper;
import com.tl.tgGame.project.service.BetService;
import com.tl.tgGame.project.service.UserService;
import com.tl.tgGame.tgBot.enums.GameNameEnum;
import com.tl.tgGame.util.RedisKeyGenerator;
import com.tl.tgGame.util.TimeUtil;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 17:41
 */
@Service
public class BetServiceImpl extends ServiceImpl<BetMapper, Bet> implements BetService {


    @Autowired
    private RedisKeyGenerator redisKeyGenerator;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private UserService userService;

    @Autowired
    private BetMapper betMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean bet(Long userId, Bet bet) {
        String key = redisKeyGenerator.generateKey("bet", bet.getRecordId(), userId);
        redisLock.redissonLock(key);
        try {

        } finally {
            redisLock._redissonLock(key);
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean addBet(List<ApiGameRecordListDTO> result) {
        List<Bet> list = new ArrayList<>();
        for (ApiGameRecordListDTO record : result) {
            Bet bet = buildBet(record);
            User user = userService.queryByMemberAccount(record.getAccount());
            bet.setUserId(user.getId());
            Bet one = getOne(new LambdaQueryWrapper<Bet>().eq(Bet::getRecordId, record.getRecordID()));
            if(one == null){
               list.add(bet);
            }
        }
        boolean saveBatch = saveBatch(list);
        if(!saveBatch){
            ErrorEnum.API_FC_GAME_RECORD_ADD_FAIL.throwException();
        }
        return true;
    }

    @Override
    public BigDecimal sumAmount(Long userId, Boolean hasSettled) {
        LambdaQueryWrapper<Bet> wrapper = new LambdaQueryWrapper<Bet>()
                .eq(Bet::getUserId, userId)
                .eq(Bet::getHasSettled, hasSettled);
        return betMapper.sumAmount(wrapper);
    }

    @Override
    public BigDecimal sumBetAmount(Long userId, LocalDateTime startTime,LocalDateTime endTime) {
        LambdaQueryWrapper<Bet> wrapper = new LambdaQueryWrapper<Bet>()
                .eq(Bet::getUserId, userId).ge(Objects.nonNull(startTime),Bet::getCreateTime,startTime)
                .le(Objects.nonNull(endTime),Bet::getCreateTime,endTime);
        return betMapper.sumBetAmount(wrapper);
    }

    @Override
    public BigDecimal sumWinLose(Long userId, LocalDateTime startTime, LocalDateTime endTime,Boolean hasWinLose) {
        LambdaQueryWrapper<Bet> wrapper = new LambdaQueryWrapper<Bet>()
                .eq(Objects.nonNull(userId), Bet::getUserId, userId)
                .gt(hasWinLose != null && hasWinLose, Bet::getWinLose, 0)
                .lt(hasWinLose != null & !hasWinLose, Bet::getWinLose, 0)
                .ge(Objects.nonNull(startTime), Bet::getCreateTime, startTime)
                .le(Objects.nonNull(endTime), Bet::getCreateTime, endTime);
        return betMapper.sumWinLose(wrapper);
    }


    private Bet buildBet(ApiGameRecordListDTO record) {
        return Bet.builder()
                .recordId(record.getRecordID())
                .gameAccount(record.getAccount())
                .gameId(record.getGameID())
                .gameType(record.getGametype())
                .bet(BigDecimal.valueOf(record.getBet()))
                .winLose(BigDecimal.valueOf(record.getWinlose()))
                .prize(BigDecimal.valueOf(record.getPrize()))
                .refund(BigDecimal.valueOf(record.getRefund()))
                .validBet(BigDecimal.valueOf(record.getValidBet()))
                .commission(BigDecimal.valueOf(record.getCommission()))
                .jpMode(record.getJpmode())
                .jpPoints(BigDecimal.valueOf(record.getJppoints()))
                .jpTax(BigDecimal.valueOf(record.getJptax()))
                .befores(BigDecimal.valueOf(record.getBefore()))
                .afters(BigDecimal.valueOf(record.getAfter()))
                .bDate(TimeUtil.parseLocalDateTime(record.getBdate()))
                .isBuyFeature(record.getIsBuyFeature())
                .createTime(LocalDateTime.now())
                .pullTime(TimeUtil.parseLocalDateTime(record.getBdate()).plusSeconds(1L))
                .hasSettled(false)
                .gameName(GameNameEnum.of(record.getGameID().toString()))
                .build();


    }

}
