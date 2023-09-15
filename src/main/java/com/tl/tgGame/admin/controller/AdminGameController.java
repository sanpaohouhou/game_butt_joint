package com.tl.tgGame.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tl.tgGame.admin.dto.AdminBetReq;
import com.tl.tgGame.admin.dto.AdminGameReq;
import com.tl.tgGame.common.dto.Response;
import com.tl.tgGame.project.dto.GameBetStatisticsListRes;
import com.tl.tgGame.project.entity.Game;
import com.tl.tgGame.project.entity.GameBet;
import com.tl.tgGame.project.enums.GameBusiness;
import com.tl.tgGame.project.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 17:15
 */
@RestController
@RequestMapping("/api/admin/game")
public class AdminGameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameBetService gameBetService;


    @GetMapping("gameRate")
    public Response gameRate(AdminGameReq req) {
        return Response.pageResult(gameService.queryByList(req));
    }


    @GetMapping("betList")
    public Response betList(AdminBetReq req) {
        Page<GameBet> page = gameBetService.page(new Page<>(req.getPage(), req.getSize()), new LambdaQueryWrapper<GameBet>()
                .eq(Objects.nonNull(req.getGameName()), GameBet::getGameName, req.getGameName())
                .eq(!StringUtils.isEmpty(req.getGameAccount()),GameBet::getGameAccount,req.getGameAccount())
                .eq(req.getId() != null, GameBet::getId, req.getId())
                .eq(!StringUtils.isEmpty(req.getGameBusiness()),GameBet::getGameBusiness,req.getGameBusiness())
                .ge(Objects.nonNull(req.getStartTime()), GameBet::getCreateTime, req.getStartTime())
                .le(Objects.nonNull(req.getEndTime()), GameBet::getCreateTime, req.getEndTime()));
        return Response.pageResult(page);
    }

    @GetMapping("gameList")
    public Response gameList(AdminGameReq req){
        Page<Game> page = gameService.queryByList(req);
        List<Game> records = page.getRecords();
        List<GameBetStatisticsListRes> gameBetStatisticsListRes = gameBetService.betStatistics(req);
        Map<String, GameBetStatisticsListRes> gameListResMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(gameBetStatisticsListRes)){
            gameListResMap = gameBetStatisticsListRes.stream().collect(Collectors.toMap(GameBetStatisticsListRes::getGameBusiness, k -> k));
        }
        List<Game> list = new ArrayList<>();
        for (Game game : records) {
            String key = GameBusiness.gameName(game.getGameName());
            if(gameListResMap.containsKey(key)){
                GameBetStatisticsListRes res = gameListResMap.get(key);
                game.setBetAmount(res.getBetAmount());
                game.setBetNumber(res.getBetNumber());
                game.setUserProfit(res.getUserProfit());
                game.setValidAmount(res.getValidAmount());
                game.setUserCommission(res.getUserCommission());
                game.setBackWaterAmount(res.getBackWaterAmount());
            }
            list.add(game);
        }
        page.setRecords(list);
        return Response.pageResult(page);
    }
}
