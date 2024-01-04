package com.tl.tgGame.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.dto.ApiGameRecordListDTO;
import com.tl.tgGame.project.entity.Bet;
import com.tl.tgGame.project.entity.GameBet;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.enums.*;
import com.tl.tgGame.project.mapper.BetMapper;
import com.tl.tgGame.project.service.*;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import com.tl.tgGame.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 17:41
 */
@Service
public class BetServiceImpl extends ServiceImpl<BetMapper, Bet> implements BetService {

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean addBet(List<ApiGameRecordListDTO> result) {
        List<Bet> list = new ArrayList<>();
        List<GameBet> gameBets = new ArrayList<>();
        for (ApiGameRecordListDTO record : result) {
            Bet bet = buildBet(record);
            User user = userService.queryByMemberAccount(record.getAccount());
            bet.setUserId(user.getId());
            Bet one = getOne(new LambdaQueryWrapper<Bet>().eq(Bet::getRecordId, record.getRecordID()));
            if(one == null){
               list.add(bet);
            }
            GameBusiness business = GameBusiness.FC;
            if(Arrays.asList(21008,21003,21006,21007,21004).contains(record.getGameID())){
                business = GameBusiness.FC_BY;
            }
            GameBet gameBet = buildGameBet(bet,business);
            gameBets.add(gameBet);
        }
        if(CollectionUtils.isEmpty(list)){
            return false;
        }
        if(!saveBatch(list)){
            ErrorEnum.API_GAME_RECORD_ADD_FAIL.throwException();
        }
        boolean saved = gameBetService.saveBatch(gameBets);
        if(!saved){
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
                        , UserCommissionType.BACK_WATER, GameBusiness.FC.getKey(), backWater, rate, actualWinLose,bet.getId());
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
    private GameBet buildGameBet(Bet bet,GameBusiness business){
        return GameBet.builder()
                .backWaterAmount(BigDecimal.ZERO)
                .gameBusiness(business.getKey())
                .createTime(LocalDateTime.now())
                .tax(BigDecimal.ZERO)
                .profit(bet.getWinLose())
                .topCommission(BigDecimal.ZERO)
                .gameAccount(bet.getGameAccount())
                .bet(bet.getBet())
                .hasSettled(false)
                .userId(bet.getUserId())
                .gameName(bet.getGameName())
                .recordId(bet.getRecordId())
                .validBet(bet.getValidBet())
                .gameId(bet.getGameId().toString())
                .recordTime(TimeUtil.shanghaiCharge(bet.getBDate()))
                .build();
    }

}
