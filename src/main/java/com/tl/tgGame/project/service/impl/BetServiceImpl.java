package com.tl.tgGame.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tl.tgGame.common.lock.RedisLock;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.dto.ApiGameRecordListDTO;
import com.tl.tgGame.project.entity.Bet;
import com.tl.tgGame.project.entity.EgBet;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.enums.*;
import com.tl.tgGame.project.mapper.BetMapper;
import com.tl.tgGame.project.service.BetService;
import com.tl.tgGame.project.service.CurrencyService;
import com.tl.tgGame.project.service.UserCommissionService;
import com.tl.tgGame.project.service.UserService;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import com.tl.tgGame.util.RedisKeyGenerator;
import com.tl.tgGame.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Autowired
    private ConfigService configService;

    @Autowired
    private UserCommissionService userCommissionService;

    @Autowired
    private CurrencyService currencyService;

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
            ErrorEnum.API_GAME_RECORD_ADD_FAIL.throwException();
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean fcCommission(Bet bet) {
        //只有输钱才会返水,拿输的钱,进行比例返水
        String gameBackWaterRate = configService.getOrDefault(ConfigConstants.GAME_BACK_WATER_RATE, "0.002");
        BigDecimal rate = new BigDecimal(gameBackWaterRate);
        if(bet.getWinLose().compareTo(BigDecimal.ZERO) < 0){
            BigDecimal actualWinLose = bet.getWinLose().negate();
            BigDecimal backWater = actualWinLose.multiply(rate).setScale(2,RoundingMode.DOWN);
            if(backWater.compareTo(BigDecimal.ZERO) > 0){
                Boolean commission = userCommissionService.insertUserCommission(bet.getUserId(), bet.getUserId(), bet.getGameId().toString(), bet.getGameName()
                        , UserCommissionType.BACK_WATER, GameBusiness.FC.getKey(), backWater, rate, actualWinLose);
                if(commission){
                    currencyService.increase(bet.getUserId(),UserType.USER,BusinessEnum.BACK_WATER,backWater,bet.getRecordId(),"fc用户输钱返水");
                }
                return update(new LambdaUpdateWrapper<Bet>().set(Bet::getHasSettled,true).set(Bet::getBackWaterAmount,backWater)
                        .set(Bet::getUpdateTime,LocalDateTime.now())
                        .eq(Bet::getHasSettled,false).eq(Bet::getId,bet.getId()));
            }else {
                return update(new LambdaUpdateWrapper<Bet>().set(Bet::getHasSettled,true)
                        .set(Bet::getUpdateTime,LocalDateTime.now())
                        .eq(Bet::getHasSettled,false).eq(Bet::getId,bet.getId()));
            }
        }else {
            return update(new LambdaUpdateWrapper<Bet>().set(Bet::getHasSettled,true)
                    .set(Bet::getUpdateTime,LocalDateTime.now())
                    .eq(Bet::getHasSettled,false).eq(Bet::getId,bet.getId()));
        }
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
                .gameName(FcGameName.of(record.getGameID().toString()))
                .build();


    }

}
