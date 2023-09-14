package com.tl.tgGame.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.dto.ApiEgGameRecordRes;
import com.tl.tgGame.project.entity.EgBet;
import com.tl.tgGame.project.entity.GameBet;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.enums.*;
import com.tl.tgGame.project.mapper.EgBetMapper;
import com.tl.tgGame.project.service.*;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import com.tl.tgGame.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
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
 * @date 2023/9/1 , 16:51
 */
@Slf4j
@Service
public class EgBetServiceImpl extends ServiceImpl<EgBetMapper, EgBet> implements EgBetService {


    @Autowired
    private UserService userService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private UserCommissionService userCommissionService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private GameBetService gameBetService;

    @Override
    public Boolean addEgBet(List<ApiEgGameRecordRes> date,LocalDateTime pullTime) {
        List<EgBet> list = new ArrayList<>();
        List<GameBet> gameBets = new ArrayList<>();
        for (ApiEgGameRecordRes record : date) {
            EgBet bet = buildEgBet(record,pullTime);
            User user = userService.queryByMemberAccount(record.getPlayerId());
            bet.setUserId(user.getId());
            EgBet one = getOne(new LambdaQueryWrapper<EgBet>().eq(EgBet::getRoundId, record.getRoundId()));
            if(one == null){
                list.add(bet);
            }
            GameBet gameBet = buildGameBet(bet);
            gameBets.add(gameBet);
        }
        if(!saveBatch(list)){
            ErrorEnum.API_GAME_RECORD_ADD_FAIL.throwException();
        }
        if(!gameBetService.saveBatch(gameBets)){
            ErrorEnum.API_GAME_RECORD_ADD_FAIL.throwException();
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean egCommission(EgBet egBet) {
        //只有输钱才会返水,拿输的钱,进行比例返水
        String gameBackWaterRate = configService.getOrDefault(ConfigConstants.GAME_BACK_WATER_RATE, "0.002");
        BigDecimal egWin = new BigDecimal(egBet.getWin());
        BigDecimal egNetWin = new BigDecimal(egBet.getNetWin()).negate();
        BigDecimal gameRate = new BigDecimal(gameBackWaterRate);
        if(egWin.compareTo(BigDecimal.ZERO) <= 0){
            BigDecimal backWater = egNetWin.multiply(gameRate).setScale(2, RoundingMode.DOWN);
            if(backWater.compareTo(BigDecimal.ZERO) > 0){
                Boolean commissionResult = userCommissionService.insertUserCommission(
                        egBet.getUserId(), egBet.getUserId(), egBet.getGameId(), egBet.getGameName(),
                        UserCommissionType.BACK_WATER, GameBusiness.EG.getKey(), backWater, gameRate, egNetWin,egBet.getId());
                if(commissionResult){
                    currencyService.increase(egBet.getUserId(), UserType.USER,BusinessEnum.BACK_WATER,backWater,egBet.getRoundId(),"eg电子用户返水");
                }
                return update(new LambdaUpdateWrapper<EgBet>().set(EgBet::getBackWaterAmount, backWater)
                        .set(EgBet::getHasSettled, true).set(EgBet::getUpdateTime, LocalDateTime.now())
                        .eq(EgBet::getHasSettled, false).eq(EgBet::getId, egBet.getId()));
            }else {
                return update(new LambdaUpdateWrapper<EgBet>()
                        .set(EgBet::getHasSettled, true).set(EgBet::getUpdateTime, LocalDateTime.now())
                        .eq(EgBet::getHasSettled, false).eq(EgBet::getId, egBet.getId()));
            }
        }else {
            return update(new LambdaUpdateWrapper<EgBet>()
                    .set(EgBet::getHasSettled, true).set(EgBet::getUpdateTime, LocalDateTime.now())
                    .eq(EgBet::getHasSettled, false).eq(EgBet::getId, egBet.getId()));
        }
    }


    private EgBet buildEgBet(ApiEgGameRecordRes record,LocalDateTime pullTime){
        return EgBet.builder()
                .betTime(record.getBetTime())
                .betType(record.getBetType())
                .bet(record.getBet())
                .createTime(LocalDateTime.now())
                .afterBalance(record.getAfterBalance())
                .gameId(record.getGameId())
                .hasSettled(false)
                .gameName(EgGameName.of(record.getGameId()))
                .backWaterAmount(BigDecimal.ZERO)
                .beforeBalance(record.getBeforeBalance())
                .pullTime(pullTime)
                .playerId(record.getPlayerId())
                .currency(record.getCurrency())
                .extra(Objects.isNull(record.getExtra()) ? "" : new Gson().toJson(record.getExtra()))
                .netWin(record.getNetWin())
                .roundCode(record.getRoundCode())
                .win(record.getWin())
                .roundId(record.getRoundId())
                .topCommission(BigDecimal.ZERO)
                .winTime(record.getWinTime())
                .wpTime(record.getWpTime())
                .build();
    }

    private GameBet buildGameBet(EgBet egBet){
        return GameBet.builder()
                .backWaterAmount(BigDecimal.ZERO)
                .gameBusiness(GameBusiness.EG.getKey())
                .createTime(LocalDateTime.now())
                .tax(BigDecimal.ZERO)
                .profit(new BigDecimal(egBet.getNetWin()))
                .topCommission(BigDecimal.ZERO)
                .gameAccount(egBet.getPlayerId())
                .bet(new BigDecimal(egBet.getBet()))
                .hasSettled(false)
                .userId(egBet.getUserId())
                .gameName(egBet.getGameName())
                .recordId(egBet.getRoundId())
                .validBet(new BigDecimal(egBet.getBet()))
                .gameId(egBet.getGameId())
                .recordTime(TimeUtil.parseTimestamp(egBet.getWpTime()))
                .build();
    }
}
