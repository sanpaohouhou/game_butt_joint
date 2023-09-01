package com.tl.tgGame.project.controller;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.tl.tgGame.auth.annotation.Uid;
import com.tl.tgGame.common.dto.Response;
import com.tl.tgGame.project.dto.*;
import com.tl.tgGame.project.service.GameApiService;
import com.tl.tgGame.project.service.GameService;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 13:55
 */
@RestController
@RequestMapping("/api/game")
public class ApiGameController {

    @Autowired
    private GameApiService gameApiService;

    @Autowired
    private GameService gameService;

    @Autowired
    private ConfigService configService;


    @PostMapping("/getBalance")
    public GameApiResponse getBalance(ApiGameReq apiGameReq) {
        return gameApiService.getBalance(apiGameReq);
    }

    @PostMapping("cancelBetAndInfo")
    public GameApiResponse cancelBetAndInfo(ApiGameReq apiGameReq) {
        return gameApiService.cancelBetAndInfo(apiGameReq);
    }

    @PostMapping("getBetAndInfo")
    public GameApiResponse getBetAndInfo(ApiGameReq apiGameReq) {
        return gameApiService.getBetOrInfo(apiGameReq);
    }

    @PostMapping("activity")
    public GameApiResponse activity(ApiGameReq apiGameReq) {
        return gameApiService.activity(apiGameReq);
    }

    @PostMapping("bet")
    public GameApiResponse bet(ApiGameReq apiGameReq) {
        return gameApiService.bet(apiGameReq);
    }

    @PostMapping("settle")
    public GameApiResponse settle(ApiGameReq apiGameReq) {
        return gameApiService.Settle(apiGameReq);
    }

    @PostMapping("cancelBet")
    public GameApiResponse cancelBet(ApiGameReq apiGameReq) {
        return gameApiService.cancelBet(apiGameReq);
    }


    /**
     * 获取eg游戏列表
     */
    @GetMapping("/egGameList")
    public Response egGameList() {
        ApiEgGameListRes apiEgGameListRes = gameService.egGameList();
        if (apiEgGameListRes.getCode() != null) {
            return Response.success();
        }
        String egGameList = configService.get(ConfigConstants.EG_GAME_LIST);
        Type type = new TypeToken<List<EgGameListDTO>>(){}.getType();
        List<EgGameListDTO> egGameListDTOS = new Gson().fromJson(egGameList, type);
        Map<String, EgGameListDTO> map = egGameListDTOS.stream().collect(Collectors.toMap(EgGameListDTO::getGameId, o -> o));
        List<EgGameListDTO> list = new ArrayList<>();
        for (ApiEgGameNameRes res : apiEgGameListRes.getData()) {
            EgGameListDTO gameListDTO = map.get(res.getGameId());
            list.add(gameListDTO);
        }
        return Response.success(list);
    }

    /**
     * 通过游戏id获取eg游戏url
     */
    @GetMapping("/gameUrl")
    public Response gameUrl(@Uid Long uid, @RequestParam String gameId) {
        String merch = configService.get(ConfigConstants.EG_AGENT_CODE);
        String url = gameService.egEnterGame(ApiEgEnterGameReq.builder().merch(merch).gameId(gameId).lang("zh_CN").playerId(uid.toString()).build());
        return Response.success(url);
    }

}
