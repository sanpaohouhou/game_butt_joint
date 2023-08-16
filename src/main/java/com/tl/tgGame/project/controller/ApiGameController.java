package com.tl.tgGame.project.controller;

import com.tl.tgGame.project.dto.ApiGameReq;
import com.tl.tgGame.project.dto.GameApiResponse;
import com.tl.tgGame.project.service.GameApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 13:55
 */
@RestController("/api")
public class ApiGameController {

    @Autowired
    private GameApiService gameApiService;


    @PostMapping("/getBalance")
    public GameApiResponse getBalance(ApiGameReq apiGameReq){
        return gameApiService.getBalance(apiGameReq);
    }

    @PostMapping("cancelBetAndInfo")
    public GameApiResponse cancelBetAndInfo(ApiGameReq apiGameReq){
        return gameApiService.cancelBetAndInfo(apiGameReq);
    }

    @PostMapping("getBetAndInfo")
    public GameApiResponse getBetAndInfo(ApiGameReq apiGameReq){
        return gameApiService.getBetOrInfo(apiGameReq);
    }

    @PostMapping("activity")
    public GameApiResponse activity(ApiGameReq apiGameReq){
        return gameApiService.activity(apiGameReq);
    }

    @PostMapping("bet")
    public GameApiResponse bet(ApiGameReq apiGameReq){
        return gameApiService.bet(apiGameReq);
    }

    @PostMapping("settle")
    public GameApiResponse settle(ApiGameReq apiGameReq){
        return gameApiService.Settle(apiGameReq);
    }

    @PostMapping("cancelBet")
    public GameApiResponse cancelBet(ApiGameReq apiGameReq){
        return gameApiService.cancelBet(apiGameReq);
    }
}
