package com.tl.tgGame.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.tl.tgGame.auth.annotation.Uid;
import com.tl.tgGame.auth.service.AuthTokenService;
import com.tl.tgGame.common.dto.Response;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.dto.*;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.enums.GameBusiness;
import com.tl.tgGame.project.service.GameApiService;
import com.tl.tgGame.project.service.ApiGameService;
import com.tl.tgGame.project.service.UserService;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import com.tl.tgGame.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
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
    private ApiGameService apiGameService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthTokenService authTokenService;

    @Value("${security.key}")
    private String securityKey;


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
    @GetMapping("/gameList")
    public Response apiGameList(@RequestParam(defaultValue = "FC") String type,
                                @RequestParam(required = false) String gameAccount) throws IOException {
        List<ApiGameListDTO> list = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        if (type.equals("FC")) {
            String path = "./src/fcGameList.json";
            list = objectMapper.readValue(new File(path),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ApiGameListDTO.class));
        }
        if (type.equals("FCBY")) {
            String path = "./src/fcByGame.json";
            list = objectMapper.readValue(new File(path),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ApiGameListDTO.class));
        }
        if (type.equals("EG")) {
            ApiEgGameListRes apiEgGameListRes = apiGameService.egGameList();
            if (apiEgGameListRes.getCode() != null) {
                return Response.success();
            }
            String egGameList = configService.get(ConfigConstants.EG_GAME_LIST);
            Type type1 = new TypeToken<List<ApiGameListDTO>>() {
            }.getType();
            List<ApiGameListDTO> apiGameListDTOS = new Gson().fromJson(egGameList, type1);
            Map<String, ApiGameListDTO> map = apiGameListDTOS.stream().collect(Collectors.toMap(ApiGameListDTO::getGameId, o -> o));
            for (ApiEgGameNameRes res : apiEgGameListRes.getData()) {
                ApiGameListDTO gameListDTO = map.get(res.getGameId());
                list.add(gameListDTO);
            }
        }
        return Response.success(list);
    }

    @GetMapping("/bb/gameList")
    public Response bbGameList(@RequestParam(defaultValue = "BBDZ")String type) throws IOException {
        List<ApiBbGameList> list = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        if(type.equals("BBBY")){
            String path = "./src/BbBYGameList.json";
            list = objectMapper.readValue(new File(path),
                    objectMapper.getTypeFactory().constructCollectionType(List.class,ApiBbGameList.class));
        }
        if(type.equals("BBDZ")){
            String path = "./src/BbDZGameList.json";
            list = objectMapper.readValue(new File(path),
                    objectMapper.getTypeFactory().constructCollectionType(List.class,ApiBbGameList.class));
        }
        return Response.success(list);
    }

    /**
     * 通过游戏id获取游戏url
     */
    @GetMapping("/gameUrl")
    public Response gameUrl(@RequestParam(required = false) String gameId,
                            @RequestParam String type) {

        String token = authTokenService.token();
        String decrypt = AESUtil.decrypt(token, securityKey);
        User user = userService.getById(decrypt);
        if(Objects.isNull(user)){
            ErrorEnum.OBJECT_NOT_FOUND.throwException();
        }
        String url = null;
        if (type.equals("FC") || type.equals("FCBY")) {
            url = apiGameService.login(ApiLoginReq.builder().MemberAccount(user.getGameAccount())
                    .GameID(Integer.valueOf(gameId)).LoginGameHall(false).LanguageID(2).build());
            userService.gameRecharge(user, GameBusiness.FC.getKey());
        }
        if (type.equals("EG")) {
            userService.gameRecharge(user, GameBusiness.WL.getKey());
            String merch = configService.get(ConfigConstants.EG_AGENT_CODE);
            url = apiGameService.egEnterGame(ApiEgEnterGameReq.builder().merch(merch).gameId(gameId).lang("zh_CN").playerId(user.getGameAccount()).build());
        }
        if(type.equals("BBBY")){
//            userService.gameRecharge(user.getTgId(),GameBusiness.BB.getKey());
            String sessionId = apiGameService.bBCreateSession(user.getGameAccount());
            List<ApiBbGameUrlRes> apiBbGameUrlRes = new ArrayList<>();
            if(Arrays.asList("38001","38002").contains(gameId)){
                apiBbGameUrlRes = apiGameService.bBGameUrlBy38(sessionId,Integer.valueOf(gameId));
            }else {
                apiBbGameUrlRes = apiGameService.bBGameUrlBy30(sessionId, Integer.valueOf(gameId));
            }
            if(CollectionUtils.isEmpty(apiBbGameUrlRes)){
                ErrorEnum.SYSTEM_ERROR.throwException();
            }
            url = apiBbGameUrlRes.get(0).getHtml5();
        }
        if(type.equals("BBDZ")){
//            userService.gameRecharge(user.getTgId(),GameBusiness.BB.getKey());
            String sessionId = apiGameService.bBCreateSession(user.getGameAccount());
            List<ApiBbGameUrlRes> apiBbGameUrlRes = apiGameService.bBGameUrlBy5(sessionId, Integer.valueOf(gameId));
            if(!CollectionUtils.isEmpty(apiBbGameUrlRes)){
                url = apiBbGameUrlRes.get(0).getHtml5();
            }
        }
        if(type.equals("BBSX")){
            String sessionId = apiGameService.bBCreateSession(user.getGameAccount());
            List<ApiBbSXGameUrlRes> globals = apiGameService.bBGameUrlBy3(sessionId, "global");
            if(!CollectionUtils.isEmpty(globals)){
                url = globals.get(0).getMobile();
            }
        }
        if(type.equals("BBTY")){
            String sessionId = apiGameService.bBCreateSession(user.getGameAccount());
            List<ApiBbSXGameUrlRes> apiBbGameUrlRes = apiGameService.bBGameUrlBy31(sessionId);
            if(!CollectionUtils.isEmpty(apiBbGameUrlRes)){
                url = apiBbGameUrlRes.get(0).getMobile();
            }
        }
        return Response.success(url);
    }




    public static void main(String[] args) {
        String path = "./src/fcGameList.json";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<ApiGameListDTO> gameLists = objectMapper.readValue(new File(path),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ApiGameListDTO.class));
            System.out.println(gameLists);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
