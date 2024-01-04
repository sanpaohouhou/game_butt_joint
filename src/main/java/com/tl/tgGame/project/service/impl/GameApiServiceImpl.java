package com.tl.tgGame.project.service.impl;

import com.google.gson.Gson;
import com.tl.tgGame.project.dto.*;
import com.tl.tgGame.project.entity.Currency;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.enums.UserType;
import com.tl.tgGame.project.service.BetService;
import com.tl.tgGame.project.service.CurrencyService;
import com.tl.tgGame.project.service.GameApiService;
import com.tl.tgGame.project.service.UserService;
import com.tl.tgGame.project.util.AesGameUtil;
import com.tl.tgGame.util.Md5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/7 , 16:13
 */
@Service
@Slf4j
public class GameApiServiceImpl implements GameApiService {

    private static final String AGENT_KEY = "rK8dho7YOHoUUKy8";

    private static final String URL = "https://api.fcg666.net";


    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private UserService userService;

    @Autowired
    private BetService betService;

    @Override
    public GameApiResponse cancelBetAndInfo(ApiGameReq req) {
        try {
            String result = AesGameUtil.aesDecrypt(req.getParams(), AGENT_KEY);
            ApiGameCancelBetReq cancelBetReq = new Gson().fromJson(result, ApiGameCancelBetReq.class);
            if(checkSign(result,req.getSign())){
                return new GameApiResponse("604","验证失败");
            }
            User user = userService.queryByMemberAccount(cancelBetReq.getMemberAccount());
            if(user == null){
                return new GameApiResponse("500","账号不存在");
            }
            Currency currency = currencyService.getOrCreate(user.getId(), UserType.USER);
            // TODO: 2023/8/14 处理下注的问题
            return new GameApiResponse("0",currency.getRemain().doubleValue());
        } catch (Exception e) {
            return new GameApiResponse("999","无法预知的错误");
        }
    }

    @Override
    public GameApiResponse getBalance(ApiGameReq req) {
        try {
            String result = AesGameUtil.aesDecrypt(req.getParams(), AGENT_KEY);
            ApiGameBalanceReq gameBalanceReq = new Gson().fromJson(result, ApiGameBalanceReq.class);
            if(checkSign(result,req.getSign())){
                return new GameApiResponse("604","验证失败");
            }
            User user = userService.queryByMemberAccount(gameBalanceReq.getMemberAccount());
            if(user == null){
                return new GameApiResponse("500","账号不存在");
            }
            Currency currency = currencyService.getOrCreate(user.getId(), UserType.USER);
            return new GameApiResponse("0",currency.getRemain().doubleValue());
        } catch (Exception e) {


        }
        return null;
    }

    @Override
    public GameApiResponse getBetOrInfo(ApiGameReq req) {
        try {
            String result = AesGameUtil.aesDecrypt(req.getParams(), AGENT_KEY);
            ApiGameBetAndResultReq gameBetAndResultReq = new Gson().fromJson(result, ApiGameBetAndResultReq.class);
            if(checkSign(result,req.getSign())){
                return new GameApiResponse("604","验证失败");
            }
            User user = userService.queryByMemberAccount(gameBetAndResultReq.getMemberAccount());
            if(user == null){
                return new GameApiResponse("500","账号不存在");
            }
            Currency currency = currencyService.getOrCreate(user.getId(), UserType.USER);

            if(currency.getRemain().compareTo(BigDecimal.valueOf(gameBetAndResultReq.getBet())) < 0){
                return new GameApiResponse("203","玩家余额不足");
            }

//            boolean save = betService.save(buildBet(user.getId(), gameBetAndResultReq));

            return new GameApiResponse("0",currency.getRemain().doubleValue());
        } catch (Exception e) {

        }

        return null;
    }

    @Override
    public GameApiResponse activity(ApiGameReq req) {
        return null;
    }

    @Override
    public GameApiResponse bet(ApiGameReq req) {
        return null;
    }

    @Override
    public GameApiResponse Settle(ApiGameReq req) {
        return null;
    }

    @Override
    public GameApiResponse cancelBet(ApiGameReq req) {
        return null;
    }



    public Boolean checkSign(String result,String sign){
        String md5 = Md5Util.MD5(result, 32);
        if(md5 == sign){
            return true;
        }
        return false;
    }

//    private Bet buildBet(Long userId,ApiGameBetAndResultReq req){
//        return Bet.builder()
//                .bet(req.getBet())
//                .gameId(req.getGameID())
//                .jpBet(req.getJPBet())
//                .allBackWaterAmount(BigDecimal.ZERO)
//                .backWaterAmount(BigDecimal.ZERO)
//                .bankId(req.getBankID())
//                .gameDate(req.getGameDate())
//                .gameName(GameNameEnum.of(req.getGameID()).getGameName())
//                .gameType(req.getGameType())
//                .createDate(req.getCreateDate())
//                .hasBackWater(false)
//                .hasSettled(false)
//                .isBuyFeature(req.getIsBuyFeature())
//                .currency(req.getCurrency())
//                .jpPrize(req.getJPPrize())
//                .memberAccount(req.getMemberAccount())
//                .netWin(req.getNetWin())
//                .createTime(LocalDateTime.now())
//                .recordId(req.getRecordID())
//                .requireAmt(req.getRequireAmt())
//                .topCommission(BigDecimal.ZERO)
//                .ts(req.getTs())
//                .userId(userId).build();
//    }
}
