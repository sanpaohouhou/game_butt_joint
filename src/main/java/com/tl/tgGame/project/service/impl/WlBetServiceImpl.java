package com.tl.tgGame.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.dto.ApiWlGameRecordData;
import com.tl.tgGame.project.dto.ApiWlGameRecordRes;
import com.tl.tgGame.project.entity.Game;
import com.tl.tgGame.project.entity.GameBet;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.entity.WlBet;
import com.tl.tgGame.project.enums.*;
import com.tl.tgGame.project.mapper.WlBetMapper;
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

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/1 , 16:42
 */
@Slf4j
@Service
public class WlBetServiceImpl extends ServiceImpl<WlBetMapper, WlBet> implements WlBetService {


    @Autowired
    private ConfigService configService;

    @Autowired
    private UserCommissionService userCommissionService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private UserService userService;

    @Autowired
    private GameBetService gameBetService;

    @Override
    public Boolean addWlBet(ApiWlGameRecordData data) {
        ApiWlGameRecordRes list = data.getList();
        List<WlBet> recordList = new ArrayList<>();
        List<GameBet> gameBets = new ArrayList<>();
        if (data.getCount() <= 0) {
            return true;
        }
        BigDecimal usdtPoint = configService.getDecimal(ConfigConstants.WL_GAME_USDT_POINT);
        for (int i = 0; i < data.getCount(); i++) {
            BigDecimal balance = list.getBalance()[i];
            BigDecimal bet = list.getBet()[i];
            Integer category = list.getCategory()[i];
            String detailUrl = "";
            if (list.getDetailUrl() != null) {
                detailUrl = list.getDetailUrl()[i];
            }
            Integer game = list.getGame()[i];
            String gameId = list.getGameId()[i];
            LocalDateTime gameStartTime = TimeUtil.getStringDisplayLocalDateTime(list.getGameStartTime()[i]);
            BigDecimal profit = list.getProfit()[i];
            String recordId = list.getRecordId()[i];
            LocalDateTime recordTime = TimeUtil.getStringDisplayLocalDateTime(list.getRecordTime()[i]);
            BigDecimal tax = list.getTax()[i];
            BigDecimal validBet = list.getValidBet()[i];
            Long userId = list.getUid()[i];
            LocalDateTime pullTime = TimeUtil.getStringDisplayLocalDateTime(data.getUntil()).plusSeconds(1);

            WlBet wlBet = buildWlBet(balance, bet, category, detailUrl, game,
                    gameId, gameStartTime, profit, recordId, recordTime, tax, validBet, userId, pullTime);
            WlBet record = this.getOne(new LambdaQueryWrapper<WlBet>().eq(WlBet::getRecordId, recordId));
            if (record == null) {
                recordList.add(wlBet);
            }
            User user = userService.getById(userId);
            GameBet gameBet = buildGameBet(wlBet, user.getGameAccount(),usdtPoint);
            gameBets.add(gameBet);
        }
        if (!saveBatch(recordList)) {
            ErrorEnum.API_GAME_RECORD_ADD_FAIL.throwException();
        }
        if(!gameBetService.saveBatch(gameBets)){
            ErrorEnum.API_GAME_RECORD_ADD_FAIL.throwException();
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean wlCommission(WlBet wlBet) {
        String gameBackWaterRate = configService.getOrDefault(ConfigConstants.GAME_BACK_WATER_RATE, "0.002");
        BigDecimal rate = new BigDecimal(gameBackWaterRate);
        //profit > 0代表用户输钱,系统赢钱
        if (wlBet.getProfit().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal usdtPoint = configService.getDecimal(ConfigConstants.WL_GAME_USDT_POINT);
            BigDecimal backWater = wlBet.getProfit().divide(usdtPoint, 2, RoundingMode.DOWN);
            if (backWater.compareTo(BigDecimal.ZERO) > 0) {
                GameBusiness business = GameBusiness.WL;
                if (wlBet.getGame().equals(81)) {
                    business = GameBusiness.WL_BJL;
                }
                if (wlBet.getGame().equals(100)) {
                    business = GameBusiness.WL_TY;
                }
                Boolean commission = userCommissionService.insertUserCommission(wlBet.getUserId(), wlBet.getUserId(), wlBet.getGameId(), wlBet.getGameName()
                        , UserCommissionType.BACK_WATER, business.getKey(), backWater, rate, wlBet.getProfit());
                if (commission) {
                    currencyService.increase(wlBet.getId(), UserType.USER, BusinessEnum.BACK_WATER, backWater, wlBet.getRecordId(), "瓦力用户输钱返水");
                }
                return update(new LambdaUpdateWrapper<WlBet>().set(WlBet::getHasSettled, true).set(WlBet::getBackWaterAmount, backWater)
                        .set(WlBet::getUpdateTime, LocalDateTime.now())
                        .eq(WlBet::getHasSettled, false).eq(WlBet::getId, wlBet.getId()));
            } else {
                return update(new LambdaUpdateWrapper<WlBet>().set(WlBet::getHasSettled, true)
                        .set(WlBet::getUpdateTime, LocalDateTime.now())
                        .eq(WlBet::getHasSettled, false).eq(WlBet::getId, wlBet.getId()));
            }
        } else {
            return update(new LambdaUpdateWrapper<WlBet>().set(WlBet::getHasSettled, true)
                    .set(WlBet::getUpdateTime, LocalDateTime.now())
                    .eq(WlBet::getHasSettled, false).eq(WlBet::getId, wlBet.getId()));
        }
    }

    private WlBet buildWlBet(BigDecimal balance, BigDecimal bet, Integer category, String detailUrl,
                             Integer game, String gameId, LocalDateTime gameStartTime, BigDecimal profit,
                             String recordId, LocalDateTime recordTime, BigDecimal tax, BigDecimal validBet,
                             Long uid, LocalDateTime until) {
        return WlBet.builder()
                .balance(balance)
                .bet(bet)
                .category(category)
                .detailUrl(detailUrl)
                .game(game)
                .gameId(gameId)
                .gameStartTime(gameStartTime)
                .profit(profit)
                .recordId(recordId)
                .recordTime(recordTime)
                .tax(tax)
                .validBet(validBet)
                .userId(uid)
                .pullTime(until)
                .gameName(WlGameName.of(game))
                .hasSettled(false)
                .createTime(LocalDateTime.now()).build();
    }

    private GameBet buildGameBet(WlBet wlBet, String gameAccount,BigDecimal usdtPoint) {
        GameBusiness business = GameBusiness.WL;
        if (wlBet.getGame().equals(81)) {
            business = GameBusiness.WL_BJL;
        }
        if (wlBet.getGame().equals(100)) {
            business = GameBusiness.WL_TY;
        }
        return GameBet.builder()
                .backWaterAmount(BigDecimal.ZERO)
                .gameBusiness(business.getKey())
                .createTime(LocalDateTime.now())
                .tax(wlBet.getTax())
                .profit(wlBet.getProfit().negate())
                .topCommission(BigDecimal.ZERO)
                .gameAccount(gameAccount)
                .bet(wlBet.getBet())
                .hasSettled(false)
                .userId(wlBet.getUserId())
                .gameName(wlBet.getGameName())
                .recordId(wlBet.getRecordId())
                .validBet(wlBet.getValidBet())
                .gameId(wlBet.getGame().toString())
                .recordTime(wlBet.getRecordTime())
                .build();
    }
}
