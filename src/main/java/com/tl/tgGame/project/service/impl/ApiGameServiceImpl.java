package com.tl.tgGame.project.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.URLEncoder;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tl.tgGame.auth.captcha.core.Randoms;
import com.tl.tgGame.common.IpTool;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.dto.*;
import com.tl.tgGame.project.service.ApiGameService;
import com.tl.tgGame.project.util.AesGameUtil;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import com.tl.tgGame.util.HttpUtil;
import com.tl.tgGame.util.Md5Util;
import com.tl.tgGame.util.TimeUtil;
import com.tl.tgGame.util.crypto.Crypto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.util.DigestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/4 , 17:43
 */
@Slf4j
@Service
public class ApiGameServiceImpl implements ApiGameService {

    @Value("${game_api.addMember}")
    private String addMemberUrl;

    @Value("${game_api.login}")
    private String loginUrl;

    @Value("${game_api.kickOut}")
    private String kickOutUrl;

    @Value("${game_api.kickOutAll}")
    private String kickOutAllUrl;

    @Value("${game_api.getPlayReport}")
    private String getPlayReportUrl;

    @Value("${game_api.getOnlineMember}")
    private String getOnlineMemberUrl;

    @Value("${game_api.getMemberGameReport}")
    private String getMemberGameReportUrl;

    @Value("${game_api.searchMember}")
    private String searchMember;

    @Value("${game_api.setPoints}")
    private String setPoints;

    @Value("${game_api.getSingleBill}")
    private String getSingleBill;

    @Value("${game_api.getRecordList}")
    private String getRecordList;

    @Value("${game_api.eg.createUser}")
    private String egCreateUser;

    @Value("${game_api.eg.deposit}")
    private String egDeposit;
    @Value("${game_api.eg.withdraw}")
    private String egWithdraw;
    @Value("${game_api.eg.logout}")
    private String egLogout;
    @Value("${game_api.eg.gameList}")
    private String egGameList;
    @Value("${game_api.eg.enterGame}")
    private String egEnterGame;
    @Value("${game_api.eg.roundRecordByTime}")
    private String egRoundRecordByTime;

    @Value("${game_api.wl.transferUrl}")
    private String wlTransferUrl;
    @Value("${game_api.wl.queryOrder}")
    private String wlQueryOrderUrl;
    @Value("${game_api.wl.enterGameUrl}")
    private String wlEnterGameUrl;
    @Value("${game_api.wl.userBalanceUrl}")
    private String wlUserBalanceUrl;
    @Value("${game_api.wl.gameRecordUrl}")
    private String wlGameRecordUrl;
    private String paramFormat = "a=%s&t=%s&p=%s&k=%s";

    @Autowired
    private ConfigService configService;

    @Autowired
    private IpTool ipTool;

    @Override
    public void addUser(ApiAddMemberReq req) {
        try {
            String json = new Gson().toJson(req);
            String fcAgentKey = configService.get(ConfigConstants.FC_AGENT_KEY);
            String fcHost = configService.get(ConfigConstants.FC_HOST);
            String aesEncrypt = AesGameUtil.aesEncrypt(json, fcAgentKey);
            String sign = Md5Util.MD5(json, 32);
            Map<String, Object> params = buildParam(aesEncrypt, sign);
            System.out.println("params:{}" + params);
            String body = HttpUtil.doPost(fcHost + addMemberUrl, params, "UTF-8", "multipart/form-data");
            System.out.println(body);
        } catch (Exception e) {

        }
    }

    @Override
    public String login(ApiLoginReq req) {
        Map<String, Object> params = null;
        String body = null;
        try {
            String json = new Gson().toJson(req);
            String fcAgentKey = configService.get(ConfigConstants.FC_AGENT_KEY);
            String fcHost = configService.get(ConfigConstants.FC_HOST);
            String aesEncrypt = AesGameUtil.aesEncrypt(json, fcAgentKey);
            String sign = Md5Util.MD5(json, 32);
            params = buildParam(aesEncrypt, sign);
            body = HttpUtil.doPost(fcHost + "/Login", params, "UTF-8", "multipart/form-data");
            log.info("FC发财电子游戏登录请求参数:{},返回结果:{}", params, body);
            if (body != null) {
                ApiLoginRes apiLoginRes = new Gson().fromJson(body, ApiLoginRes.class);
                if (!apiLoginRes.getResult().equals("0")) {
                    ErrorEnum.INTERNAL_ERROR.throwException(apiLoginRes.getResult());
                }
                return apiLoginRes.getUrl();
            }
        } catch (Exception e) {
            log.error("FC发财电子获取游戏登录链接失败Exception:{},request:{},params:{},response:{}", e, req.toString(), params, body);
        }
        return null;
    }

    @Override
    public void kickOut(ApiAddMemberReq req) {
        try {
            String fcAgentKey = configService.get(ConfigConstants.FC_AGENT_KEY);
            String fcHost = configService.get(ConfigConstants.FC_HOST);
            String aesEncrypt = AesGameUtil.aesEncrypt(new Gson().toJson(req), fcAgentKey);
            String sign = Md5Util.MD5(new Gson().toJson(req), 32);
            Map<String, Object> params = buildParam(aesEncrypt, sign);
            String body = HttpUtil.doPost(fcHost + kickOutUrl, params, "UTF-8", "multipart/form-data");
            System.out.println(body);
        } catch (Exception e) {

        }
    }

    @Override
    public void kickOutAll() {
        try {
            String fcAgentKey = configService.get(ConfigConstants.FC_AGENT_KEY);
            String fcHost = configService.get(ConfigConstants.FC_HOST);
            String aesEncrypt = AesGameUtil.aesEncrypt(new Gson().toJson("{}"), fcAgentKey);
            String sign = Md5Util.MD5(new Gson().toJson(""), 32);
            Map<String, Object> params = buildParam(aesEncrypt, sign);
            String body = HttpUtil.doPost(fcHost + kickOutAllUrl, params, "UTF-8", "multipart/form-data");
            System.out.println(body);

        } catch (Exception e) {

        }
    }

    @Override
    public void searchMember(ApiAddMemberReq req) {
        try {
            String fcAgentKey = configService.get(ConfigConstants.FC_AGENT_KEY);
            String fcHost = configService.get(ConfigConstants.FC_HOST);
            String json = new Gson().toJson(req);
            String aesEncrypt = AesGameUtil.aesEncrypt(json, fcAgentKey);
            String sign = Md5Util.MD5(json, 32);
            Map<String, Object> params = buildParam(aesEncrypt, sign);
            String body = HttpUtil.doPost(fcHost + searchMember, params, "UTF-8", "multipart/form-data");
            System.out.println(body);
        } catch (Exception e) {

        }
    }

    @Override
    public ApiSetPointRes setPoints(ApiSetPointReq req) {
        Map<String, Object> params = null;
        String body = null;
        try {
            String fcAgentKey = configService.get(ConfigConstants.FC_AGENT_KEY);
            String fcHost = configService.get(ConfigConstants.FC_HOST);
            String json = new Gson().toJson(req);
            String aesEncrypt = AesGameUtil.aesEncrypt(json, fcAgentKey);
            String sign = Md5Util.MD5(json, 32);
            params = buildParam(aesEncrypt, sign);
            body = HttpUtil.doPost(fcHost + setPoints, params, "UTF-8", "multipart/form-data");
            log.info("FC发财电子游戏充提请求参数:{},返回结果:{}", params, body);
            return new Gson().fromJson(body, ApiSetPointRes.class);
        } catch (Exception e) {
            log.error("FC发财电子充提失败Exception:{},request:{},params:{},response:{}", e, req.toString(), params, body);
        }
        return null;
    }

    @Override
    public void getSingleBill(ApiGetSingleBillReq req) {
        try {
            String fcAgentKey = configService.get(ConfigConstants.FC_AGENT_KEY);
            String fcHost = configService.get(ConfigConstants.FC_HOST);
            String json = new Gson().toJson(req);
            String aesEncrypt = AesGameUtil.aesEncrypt(json, fcAgentKey);
            String sign = Md5Util.MD5(json, 32);
            Map<String, Object> params = buildParam(aesEncrypt, sign);
            String body = HttpUtil.doPost(fcHost + getSingleBill, params, "UTF-8", "multipart/form-data");
            System.out.println(body);
        } catch (Exception e) {

        }
    }

    @Override
    public void getPlayerReport(ApiGetPlayerReportReq req) {
        try {
            String fcAgentKey = configService.get(ConfigConstants.FC_AGENT_KEY);
            String fcHost = configService.get(ConfigConstants.FC_HOST);
            String json = new Gson().toJson(req);
            String aesEncrypt = AesGameUtil.aesEncrypt(json, fcAgentKey);
            String sign = Md5Util.MD5(json, 32);
            Map<String, Object> params = buildParam(aesEncrypt, sign);
            String body = HttpUtil.doPost(fcHost + getPlayReportUrl, params, "UTF-8", "multipart/form-data");
            System.out.println(body);
        } catch (Exception e) {

        }
    }

    @Override
    public List<ApiGameRecordListDTO> getRecordList(ApiRecordListReq req) {
        List<ApiGameRecordListDTO> results = new ArrayList<>();
        Map<String, Object> params = null;
        String body = null;
        try {
            String fcAgentKey = configService.get(ConfigConstants.FC_AGENT_KEY);
            String fcHost = configService.get(ConfigConstants.FC_HOST);
            String json = new Gson().toJson(req);
            String aesEncrypt = AesGameUtil.aesEncrypt(json, fcAgentKey);
            String sign = Md5Util.MD5(json, 32);
            params = buildParam(aesEncrypt, sign);
            body = HttpUtil.doPost(fcHost + getRecordList, params, "UTF-8", "multipart/form-data");
            log.info("FC发财电子游戏获取下注记录请求参数:{},返回结果:{}", params, body);
            ApiGameRecordListRes apiGameRecordListRes = new Gson().fromJson(body, ApiGameRecordListRes.class);
            if (apiGameRecordListRes.getResult().equals(0)) {
                return apiGameRecordListRes.getRecords();
            }
        } catch (Exception e) {
            log.error("FC发财电子查询游戏记录异常exception:{},request:{},params:{},response:{}", e, req.toString(), params, body);
            ErrorEnum.GAME_GET_RECORD_LIST_FAIL.throwException();
        }
        return results;
    }


    @Override
    public Boolean egCreateUser(ApiEgCreateUserReq req) {
        String body = null;
        try {
            String json = new Gson().toJson(req);
            String egPlatform = configService.get(ConfigConstants.EG_PLATFORM);
            String egHashKey = configService.get(ConfigConstants.EG_HASH_KEY);
            String egHost = configService.get(ConfigConstants.EG_HOST);
            String hashValue = Crypto.hmacToString(DigestFactory.createSHA256(), egHashKey, json.getBytes());
            HttpResponse execute = cn.hutool.http.HttpUtil.createPost(egHost + egPlatform + egCreateUser + "?hash=" + hashValue)
                    .body(json).contentType("application/json").execute();
            body = execute.body();
            log.info("EG电子游戏创建用户请求参数:{},hash:{},返回结果:{}", json, hashValue, body);
            if (StringUtils.isEmpty(body)) {
                return true;
            }
        } catch (Exception e) {
            log.error("EG电子游戏创建用户异常exception:{},request:{},response:{}", e, req, body);
        }
        return false;
    }

    @Override
    public ApiEgDepositRes egDeposit(ApiEgDepositReq req) {
        String body = null;
        String url = null;
        try {
            String json = new Gson().toJson(req);
            String egPlatform = configService.get(ConfigConstants.EG_PLATFORM);
            String egHashKey = configService.get(ConfigConstants.EG_HASH_KEY);
            String egHost = configService.get(ConfigConstants.EG_HOST);
            String hash = Crypto.hmacToString(DigestFactory.createSHA256(), egHashKey, json.getBytes());
            url = egHost + egPlatform + egDeposit + "?hash=" + hash;
            HttpResponse execute = cn.hutool.http.HttpUtil.createPost(url).body(json).contentType("application/json").execute();
            body = execute.body();
            log.info("EG电子游戏充值请求参数:{},hash:{},返回结果:{}", json, hash, body);
            return new Gson().fromJson(body, ApiEgDepositRes.class);
        } catch (Exception e) {
            log.error("EG电子游戏充值异常exception:{},request:{},response:{}", e, req, body);
        }
        return null;
    }

    @Override
    public ApiEgDepositRes egWithdraw(ApiEgWithdrawReq req) {
        String hash = null;
        String body = null;
        try {
            String json = new Gson().toJson(req);
            String egPlatform = configService.get(ConfigConstants.EG_PLATFORM);
            String egHashKey = configService.get(ConfigConstants.EG_HASH_KEY);
            String egHost = configService.get(ConfigConstants.EG_HOST);
            hash = Crypto.hmacToString(DigestFactory.createSHA256(), egHashKey, json.getBytes());
            HttpResponse execute = cn.hutool.http.HttpUtil.createPost(egHost + egPlatform + egWithdraw + "?hash=" + hash)
                    .body(json).contentType("application/json").execute();
            body = execute.body();
            log.info("EG电子游戏提现请求参数:{},hash:{},返回结果:{}", json, hash, body);
            return new Gson().fromJson(body, ApiEgDepositRes.class);
        } catch (Exception e) {
            log.error("EG电子游戏提现异常exception:{},request:{},response:{}", e, req, body);
        }
        return null;
    }

    @Override
    public Boolean egLogout(ApiEgLogoutReq req) {
        String hash = null;
        String body = null;
        try {
            String json = new Gson().toJson(req);
            String egPlatform = configService.get(ConfigConstants.EG_PLATFORM);
            String egHashKey = configService.get(ConfigConstants.EG_HASH_KEY);
            String egHost = configService.get(ConfigConstants.EG_HOST);
            hash = Crypto.hmacToString(DigestFactory.createSHA256(), egHashKey, json.getBytes());
            HttpResponse execute = cn.hutool.http.HttpUtil.createPost(egHost + egPlatform + egLogout + "?hash=" + hash)
                    .body(json).contentType("application/json").execute();
            body = execute.body();
            log.info("EG电子游戏登出玩家请求参数:{},hash:{},返回结果:{}", json, hash, body);
            return true;
        } catch (Exception e) {
            log.error("EG电子游戏登出玩家异常exception:{},request:{},response:{}", e, req, body);
        }
        return null;
    }

    @Override
    public ApiEgGameListRes egGameList() {
        String body = null;
        try {
            String egPlatform = configService.get(ConfigConstants.EG_PLATFORM);
            String egHost = configService.get(ConfigConstants.EG_HOST);
            HttpResponse execute = cn.hutool.http.HttpUtil.createGet(egHost + egPlatform + egGameList).execute();
            body = execute.body();
            return new Gson().fromJson(body, ApiEgGameListRes.class);
        } catch (Exception e) {
            log.error("EG电子游戏充值异常exception:{},response:{}", e, body);
        }
        return null;
    }

    @Override
    public String egEnterGame(ApiEgEnterGameReq req) {
        String hash = null;
        String body = null;
        try {
            String json = new Gson().toJson(req);
            String egPlatform = configService.get(ConfigConstants.EG_PLATFORM);
            String egHost = configService.get(ConfigConstants.EG_HOST);
            String hashKey = configService.get(ConfigConstants.EG_HASH_KEY);
            hash = Crypto.hmacToString(DigestFactory.createSHA256(), hashKey, json);
            HttpResponse execute = cn.hutool.http.HttpUtil.createPost(egHost + egPlatform + egEnterGame + "?hash=" + hash)
                    .body(json).contentType("application/json").execute();
            body = execute.body();
            log.info("EG电子游戏进入游戏请求参数:{},hash:{},返回结果:{}", json, hash, body);
            ApiEgEnterGameRes apiEgEnterGameRes = new Gson().fromJson(body, ApiEgEnterGameRes.class);
            if (StringUtils.isEmpty(apiEgEnterGameRes.getCode())) {
                return apiEgEnterGameRes.getUrl();
            }
            return null;
        } catch (Exception e) {
            log.error("EG电子游戏充值异常exception:{},request:{},hash:{},response:{}", e, req, hash, body);
        }
        return null;
    }

    @Override
    public void egTransferRecordByTime(ApiEgTransferRecordTimeReq req) {
//        String hash = null;
//        String body = null;
//        try {
//            String json = new Gson().toJson(req);
//            String egPlatform = configService.get(ConfigConstants.EG_PLATFORM);
//            String egHost = configService.get(ConfigConstants.EG_HOST);
//            String hashKey = configService.get(ConfigConstants.EG_HASH_KEY);
//            hash = Crypto.hmacToString(DigestFactory.createSHA256(), hashKey, json);
//            HttpResponse execute = cn.hutool.http.HttpUtil.createPost(egHost + egPlatform + eg; + "?hash=" + hash)
//                    .body(json).contentType("application/json").execute();
//            return new Gson().fromJson(body, ApiEgGameListRes.class);
//        } catch (Exception e) {
//            log.error("EG电子游戏充值异常exception:{},response:{}", e, body);
//        }
    }

    @Override
    public ApiEgRoundRecordRes egRoundRecordByTime(ApiEgRoundRecordTimeReq req) {
        String body = null;
        String url = null;
        try {
            String json = new Gson().toJson(req);
            String egPlatform = configService.get(ConfigConstants.EG_PLATFORM);
            String egHashKey = configService.get(ConfigConstants.EG_HASH_KEY);
            String egHost = configService.get(ConfigConstants.EG_HOST);
            String hashValue = Crypto.hmacToString(DigestFactory.createSHA256(), egHashKey, json);
            url = egHost + egPlatform + egRoundRecordByTime + "?hash=" + hashValue;
            HttpResponse execute = cn.hutool.http.HttpUtil.createPost(url)
                    .body(json).contentType("application/json").execute();
            body = execute.body();
            ApiEgRoundRecordRes apiEgRoundRecordRes = new Gson().fromJson(body, ApiEgRoundRecordRes.class);
            log.info("EG电子游戏拉取游戏记录request:{},url:{},response:{}", json, url, body);
            return apiEgRoundRecordRes;
        } catch (Exception e) {
            log.error("EG电子游戏拉取游戏记录异常exception:{},url:{},request:{},response:{}", e, url, req, body);
        }
        return null;
    }

    @Override
    public ApiWlGameOrderRes wlGetUserBalance(Long userId) {

        String param = "uid=" + userId;
        String wlHost = configService.get(ConfigConstants.WL_HOST);
        String quartet = paramGen(param, String.valueOf(TimeUtil.nowTimeStamp(LocalDateTime.now())));
        String res = HttpRequest.of(wlHost + wlUserBalanceUrl + quartet, null).execute().body();
        ApiWlGameRes gameRes = new Gson().fromJson(res, ApiWlGameRes.class);
        if (gameRes.getCode() != 0) {
            ErrorEnum.SYSTEM_ERROR.throwException();
        }
        return gameRes.getData();
    }

    @Override
    public String wlEnterGame(Long userId, String gameId, HttpServletRequest request) {
        String quartet = null;
        String res = null;
        try {
            String ip = ipTool.getIp();
            String param = null;
            if (gameId != null) {
                param = String.format("uid=%s&ip=%s&game=%s", userId, ip, gameId);
            } else {
                param = String.format("uid=%s&ip=" + ip, userId);
            }
            String wlHost = configService.get(ConfigConstants.WL_HOST);
            quartet = paramGen(param, String.valueOf(TimeUtil.nowTimeStamp(LocalDateTime.now())));
            res = HttpRequest.of(wlHost + wlEnterGameUrl + quartet, null).execute().body();
            ApiWlGameRes gameRes = new Gson().fromJson(res, ApiWlGameRes.class);
            if (gameRes.getCode() != 0) {
                if (gameRes.getMsg() != null) {
                    ErrorEnum.SYSTEM_ERROR.throwException();
                }
            }
            return gameRes.getData().getGameUrl();
        } catch (Exception e) {
            log.error("WL电子游戏进入游戏记录异常exception:{},request:{},response:{}", e, quartet, res);
        }
        return null;
    }


    @Override
    public ApiWlGameRes wlPayOrder(Long userId, BigDecimal amount) {
        String wlAgentId = configService.get(ConfigConstants.WL_AGENT_ID);
        String wlHost = configService.get(ConfigConstants.WL_HOST);
        String orderId = orderIdGen(wlAgentId, LocalDateTime.now(), userId.toString());
        String param = String.format("orderId=%s&uid=%s&credit=%s", orderId, userId, amount);
        String url = paramGen(param, String.valueOf(TimeUtil.nowTimeStamp(LocalDateTime.now())));
        String res = HttpRequest.of(wlHost + wlTransferUrl + url, null).execute().body();
        ApiWlGameRes wlGameRes = new Gson().fromJson(res, ApiWlGameRes.class);
        wlGameRes.setOrderId(orderId);
        return wlGameRes;
    }

    @Override
    public ApiWlGameResponse wlGameRecord(LocalDateTime startTime, LocalDateTime endTime) {

        String startTimeStr = DateUtil.format(startTime, DatePattern.PURE_DATETIME_PATTERN);
        String engTimeStr = DateUtil.format(endTime, DatePattern.PURE_DATETIME_PATTERN);
        String param = "from=" + startTimeStr + "&" + "until=" + engTimeStr + "&" + "detail=2" + "&" + "flag=1";
        String quartet = paramGen(param, String.valueOf(TimeUtil.nowTimeStamp(LocalDateTime.now())));
        String wlHost = configService.get(ConfigConstants.WL_HOST);
        String body = HttpRequest.of(wlHost + wlGameRecordUrl + quartet, null).execute().body();
        return new Gson().fromJson(body, ApiWlGameResponse.class);
    }

    @Override
    public ApiBBRes bBCreateMember(String username) {
        String keyA = Randoms.generateRandomString(6);
        String keyC = Randoms.generateRandomString(1);
        String localDate = TimeUtil.zoneCharge("America/New_York").toLocalDate().format(dtf);
//        String bbWebSite = configService.get(ConfigConstants.BB_WEB_SITE);
        String bbWebSite = "3898be";
        String bbKeyB = "yldbkxMJ7L";
//        String bbKeyB = configService.get(ConfigConstants.BB_CREATE_MEMBER_KEY_B);
        String signStr = bbWebSite + username + bbKeyB + localDate;
        String key = keyA + Md5Util.MD5(signStr, 32) + keyC;
        String params = String.format("website=%s&uppername=%s&username=%s&key=%s", "3898be", "deetestff", username, key);
        String body = HttpRequest.of("http://linkapi.ttg123.com/app/WebService/JSON/display.php/" + "CreateMember?" + params, null).execute().body();
        return new Gson().fromJson(body, ApiBBRes.class);
    }

    @Override
    public List<ApiBBGetDemoUrlRes> bBGetDemoUrl(String lobby, Integer gameType) {
        String keyA = Randoms.generateRandomString(1);
        String keyC = Randoms.generateRandomString(6);
        String localDate = TimeUtil.zoneCharge("America/New_York").toLocalDate().format(dtf);
        String bbWebSite = "3898be";
        String bbKeyB = "96nIslg4te";
        String key = keyA + Md5Util.MD5(bbWebSite + bbKeyB + localDate, 32) + keyC;
        String params = null;
        if (gameType == null) {
            params = String.format("website=%s&lang=%s&lobby=%s&key=%s", bbWebSite, "zh-cn", lobby, key);
        } else {
            params = String.format("website=%s&lang=%s&lobby=%s&gametype=%s&key=%s", bbWebSite, "zh-cn", lobby, gameType, key);
        }
        String body = HttpRequest.of("http://linkapi.ttg123.com/app/WebService/JSON/display.php/" + "GetDemoUrl?" + params, null).execute().body();
        ApiBBRes apiBBRes = new Gson().fromJson(body, ApiBBRes.class);
        if(!apiBBRes.getResult()){
            ErrorEnum.SYSTEM_ERROR.throwException();
        }

        Type type = new TypeToken<List<ApiBBGetDemoUrlRes>>() {}.getType();
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String json = gson.toJson(apiBBRes.getData());
        return gson.fromJson(json,type);
    }

    @Override
    public ApiBBRes bBTransfer(String username, Integer remitno, String action, BigDecimal remit) {
        String keyA = Randoms.generateRandomString(1);
        String keyC = Randoms.generateRandomString(2);
        String localDate = TimeUtil.zoneCharge("America/New_York").toLocalDate().format(dtf);
        String bbWebSite = "3898be";
        String bbKeyB = "OY1aYfx";
        String uppername = "deetestff";
        String key = keyA + Md5Util.MD5(bbWebSite + username + remitno + bbKeyB + localDate, 32) + keyC;
        String params = String.format("website=%s&username=%s&uppername=%s&remitno=%s&action=%s&remit=%s&key=%s"
                , bbWebSite, username, uppername, remitno, action, remit, key);
        String body = HttpRequest.of("http://linkapi.ttg123.com/app/WebService/JSON/display.php" + "/Transfer?" + params, null).execute().body();
        return new Gson().fromJson(body,ApiBBRes.class);
    }

    @Override
    public String bBCreateSession(String username) {
         String keyA = Randoms.generateRandomString(4);
        String keyC = Randoms.generateRandomString(1);
        String localDate = TimeUtil.zoneCharge("America/New_York").toLocalDate().format(dtf);
        String bbWebSite = "3898be";
        String bbKeyB = "KVd0DuGbP";
        String uppername = "deetestff";
        String key = keyA + Md5Util.MD5(bbWebSite + username + bbKeyB + localDate, 32) + keyC;
        String params = String.format("website=%s&username=%s&uppername=%s&key=%s", bbWebSite, username, uppername, key);
        System.out.println("session:" + params);
        String body = HttpRequest.of("http://linkapi.ttg123.com/app/WebService/JSON/display.php/" + "CreateSession?" + params, null).execute().body();
        ApiBBRes apiBBRes = new Gson().fromJson(body, ApiBBRes.class);
        if(!apiBBRes.getResult()){
            ErrorEnum.SYSTEM_ERROR.throwException();
        }
        Type type = new TypeToken<Map<String, String>>() {}.getType();
        Map<String,String> map = new Gson().fromJson(String.valueOf(apiBBRes.getData()),type);
        return map.get("sessionid");
    }

    @Override
    public List<ApiBbGameUrlRes> bBGameUrlBy30(String sessionId, Integer gameType) {
        String keyA = Randoms.generateRandomString(1);
        String keyC = Randoms.generateRandomString(7);
        String localDate = TimeUtil.zoneCharge("America/New_York").toLocalDate().format(dtf);
        String bbWebSite = "3898be";
        String bbKeyB = "XJxyEzq7U";
        String key = keyA + Md5Util.MD5(bbWebSite + bbKeyB + localDate, 32) + keyC;
        String params = String.format("website=%s&lang=%s&sessionid=%s&gametype=%s&key=%s", bbWebSite, "zh-cn", sessionId, gameType, key);
        String body = HttpRequest.of("http://linkapi.ttg123.com/app/WebService/JSON/display.php/" + "GameUrlBy30?" + params, null).execute().body();
        ApiBBRes apiBBRes = new Gson().fromJson(body, ApiBBRes.class);
        if(!apiBBRes.getResult()){
            ErrorEnum.SYSTEM_ERROR.throwException();
        }
        Type type = new TypeToken<List<ApiBbGameUrlRes>>() {}.getType();
        Gson gson = new Gson();
        String json = gson.toJson(apiBBRes.getData());
        return new Gson().fromJson(json,type);
    }

    @Override
    public List<ApiBbGameUrlRes> bBGameUrlBy38(String sessionId,Integer gameType) {
        String keyA = Randoms.generateRandomString(6);
        String keyC = Randoms.generateRandomString(6);
        String localDate = TimeUtil.zoneCharge("America/New_York").toLocalDate().format(dtf);
        String bbWebsite = "3898be";
        String bbKeyB = "REFZUxCy";
        String key = keyA + Md5Util.MD5(bbWebsite + bbKeyB + localDate, 32) + keyC;
        String params = String.format("website=%s&lang=%s&sessionid=%s&gametype=%s&key=%s", bbWebsite, "zh-cn", sessionId,gameType, key);
        String body = HttpRequest.of("http://linkapi.ttg123.com/app/WebService/JSON/display.php/" + "GameUrlBy38?" + params, null).execute().body();
        Gson gson = new Gson();
        ApiBBRes apiBBRes = gson.fromJson(body, ApiBBRes.class);
        if(!apiBBRes.getResult()){
            ErrorEnum.SYSTEM_ERROR.throwException();
        }
        Type type = new TypeToken<List<ApiBbGameUrlRes>>() {}.getType();
        String json = gson.toJson(apiBBRes.getData());
        return gson.fromJson(json,type);
    }

    @Override
    public List<ApiBbSXGameUrlRes> bBGameUrlBy3(String sessionId, String tag) {
        String keyA = Randoms.generateRandomString(6);
        String keyC = Randoms.generateRandomString(4);
        String localDate = TimeUtil.zoneCharge("America/New_York").toLocalDate().format(dtf);
        String bbWebSite = "3898be";
        String bbKeyB = "f3yXsjuZ";
        String key = keyA + Md5Util.MD5(bbWebSite + bbKeyB + localDate, 32) + keyC;
        String params = String.format("website=%s&lang=%s&sessionid=%s&tag=%s&key=%s", bbWebSite, "zh-cn", sessionId, tag, key);
        String body = HttpRequest.of("http://linkapi.ttg123.com/app/WebService/JSON/display.php/" + "GameUrlBy3?" + params, null).execute().body();
        ApiBBRes apiBBRes = new Gson().fromJson(body, ApiBBRes.class);
        if(!apiBBRes.getResult()){
            ErrorEnum.SYSTEM_ERROR.throwException();
        }
        Type type = new TypeToken<List<ApiBbSXGameUrlRes>>() {}.getType();
        Gson gson = new Gson();
        String json = gson.toJson(apiBBRes.getData());
        return gson.fromJson(json,type);
    }

    @Override
    public List<ApiBbGameUrlRes> bBGameUrlBy5(String sessionId,Integer gameType) {
        String keyA = Randoms.generateRandomString(9);
        String keyC = Randoms.generateRandomString(8);
        String localDate = TimeUtil.zoneCharge("America/New_York").toLocalDate().format(dtf);
        String bbWebSite = "3898be";
        String bbKeyB = "KeNSSkwR";
        String key = keyA + Md5Util.MD5(bbWebSite + bbKeyB + localDate, 32) + keyC;
        String params = String.format("website=%s&lang=%s&sessionid=%s&gametype=%s&key=%s", bbWebSite, "zh-cn", sessionId, gameType,key);
        String body = HttpRequest.of("http://linkapi.ttg123.com/app/WebService/JSON/display.php/" + "GameUrlBy5?" + params, null).execute().body();
        Gson gson = new Gson();
        ApiBBRes apiBBRes = gson.fromJson(body, ApiBBRes.class);
        if(!apiBBRes.getResult()){
            ErrorEnum.SYSTEM_ERROR.throwException();
        }
        Type type = new TypeToken<List<ApiBbGameUrlRes>>() {}.getType();
        String json = gson.toJson(apiBBRes.getData());
        return gson.fromJson(json,type);
    }

    @Override
    public List<ApiBbSXGameUrlRes> bBGameUrlBy31(String sessionId) {
        String keyA = Randoms.generateRandomString(4);
        String keyC = Randoms.generateRandomString(6);
        String localDate = TimeUtil.zoneCharge("America/New_York").toLocalDate().format(dtf);
        String bbWebSite = "3898be";
        String bbKeyB = "U9o19R";
        String key = keyA + Md5Util.MD5(bbWebSite + bbKeyB + localDate, 32) + keyC;
        String params = String.format("website=%s&lang=%s&sessionid=%s&key=%s", bbWebSite, "zh-cn", sessionId, key);
        String body = HttpRequest.of("http://linkapi.ttg123.com/app/WebService/JSON/display.php/" + "GameUrlBy31?" + params, null).execute().body();
        Gson gson = new Gson();
        ApiBBRes apiBBRes = gson.fromJson(body, ApiBBRes.class);
        if(!apiBBRes.getResult()){
            ErrorEnum.SYSTEM_ERROR.throwException();
        }
        Type type = new TypeToken<List<ApiBbSXGameUrlRes>>() {}.getType();
        String json = gson.toJson(apiBBRes.getData());
        return gson.fromJson(json,type);
    }

    @Override
    public ApiBBRes bBWagersRecordBy3(String action, LocalDate date , String startTime, String endTime) {
        String keyA = Randoms.generateRandomString(6);
        String keyC = Randoms.generateRandomString(3);
        String localDate = TimeUtil.zoneCharge("America/New_York").toLocalDate().format(dtf);
        String bbWebSite = "3898be";
        String bbKeyB = "trgTgmN64";
        String uppername = "deetestff";
        String key = keyA + Md5Util.MD5(bbWebSite + bbKeyB + localDate, 32) + keyC;
        String params = String.format("website=%s&action=%s&uppername=%s&date=%s&starttime=%s&endtime=%s&key=%s",
                bbWebSite, action, uppername, date, startTime, endTime, key);
        String body = HttpRequest.of("http://linkapi.ttg123.com/app/WebService/JSON/display.php/" + "WagersRecordBy3?" + params, null).execute().body();
        ApiBBRes apiBBRes = new Gson().fromJson(body, ApiBBRes.class);

        return null;
    }

    @Override
    public ApiBBRes bBWagersRecordBy5(String action,LocalDate date,String startTime,String endTime,Integer subGameKind) {
        String keyA = Randoms.generateRandomString(1);
        String keyC = Randoms.generateRandomString(2);
        String localDate = TimeUtil.zoneCharge("America/New_York").toLocalDate().format(dtf);
        String bbWebSite = "3898be";
        String bbKeyB = "216VB";
        String uppername = "deetestff";
        String key = keyA + Md5Util.MD5(bbWebSite + bbKeyB + localDate, 32) + keyC;
        String params = String.format("website=%s&action=%s&uppername=%s&date=%s&starttime=%s&endtime=%s&subgamekind=%s&key=%s",
                bbWebSite, action, uppername, date, startTime, endTime, subGameKind);
        String body = HttpRequest.of("http://linkapi.ttg123.com/app/WebService/JSON/display.php/" + "WagersRecordBy5?" + params, null).execute().body();
        ApiBBRes apiBBRes = new Gson().fromJson(body, ApiBBRes.class);
        return null;
    }

    @Override
    public ApiBBRes bBWagersRecordBy30(String action,LocalDate date,String startTime,String endTime) {
        String keyA = Randoms.generateRandomString(3);
        String keyC = Randoms.generateRandomString(6);
        String localDate = TimeUtil.zoneCharge("America/New_York").toLocalDate().format(dtf);
        String bbWebSite = "3898be";
        String bbKeyB = "UNYfSFx";
        String uppername = "deetestff";
        String key = keyA + Md5Util.MD5(bbWebSite + bbKeyB + localDate, 32) + keyC;
        String params = String.format("website=%s&action=%s&uppername=%s&date=%s&starttime=%s&endtime=%s&key=%s",
                bbWebSite, action, uppername, date, startTime, endTime, key);
        String body = HttpRequest.of("http://linkapi.ttg123.com/app/WebService/JSON/display.php/" + "WagersRecordBy30?" + params, null).execute().body();
        ApiBBRes apiBBRes = new Gson().fromJson(body, ApiBBRes.class);
        return null;
    }

    @Override
    public ApiBBRes bBWagersRecordBy31(String action,LocalDate date,String startTime,String endTime) {
        String keyA = Randoms.generateRandomString(4);
        String keyC = Randoms.generateRandomString(9);
        String localDate = TimeUtil.zoneCharge("America/New_York").toLocalDate().format(dtf);
        String bbWebSite = "3898be";
        String bbKeyB = "vl8MnDLi";
        String uppername = "deetestff";
        String key = keyA + Md5Util.MD5(bbWebSite + bbKeyB + localDate, 32) + keyC;
        String params = String.format("website=%s&action=%s&uppername=%s&date=%s&startime=%s&endtime=%s&key=%s",
                bbWebSite, action, uppername, date, startTime, endTime);
        String body = HttpRequest.of("http://linkapi.ttg123.com/app/WebService/JSON/display.php/" + "WagersRecordBy31?" + params, null).execute().body();
        ApiBBRes apiBBRes = new Gson().fromJson(body, ApiBBRes.class);
        return null;
    }

    @Override
    public ApiBBRes bBWagersRecordBy38(String action,LocalDate date,String startTime,String endTime) {
        String keyA = Randoms.generateRandomString(9);
        String keyC = Randoms.generateRandomString(1);
        String localDate = TimeUtil.zoneCharge("America/New_York").toLocalDate().format(dtf);
        String bbWebSite = "3898be";
        String bbKeyB = "YYTowffDj";
        String uppername = "deetestff";
        String key = keyA + Md5Util.MD5(bbWebSite + bbKeyB + localDate, 32) + keyC;
        String params = String.format("website=%s&action=%s&uppername=%s&date=%s&starttime=%s&endtime=%s&key=%s",
                bbWebSite, action, uppername, date, startTime, endTime, key);
        String body = HttpRequest.of("http://linkapi.ttg123.com/app/WebService/JSON/display.php/" + "WagersRecordBy38?" + params, null).execute().body();
        ApiBBRes apiBBRes = new Gson().fromJson(body, ApiBBRes.class);
        return null;
    }

    private String paramGen(String param, String unixTime) {
        String wlParamKey = configService.getAndDecrypt(ConfigConstants.WL_PARAM_KEY);
        String wlReqKey = configService.getAndDecrypt(ConfigConstants.WL_REQ_KEY);
        String wlAccount = configService.get(ConfigConstants.WL_ACCOUNT);
        AES aes = SecureUtil.aes(wlParamKey.getBytes(StandardCharsets.UTF_8));
        String p = aes.encryptBase64(param);
        String k = SecureUtil.md5(p + unixTime + wlReqKey);
        return String.format(paramFormat, wlAccount, unixTime, URLEncoder.createAll().encode(p, StandardCharsets.UTF_8), k);
    }

    private String orderIdGen(String agentId, LocalDateTime dateTime, String uid) {
        String order_id_gen = "%s_%s_%s";
        String time = DateUtil.format(dateTime, DatePattern.PURE_DATETIME_MS_PATTERN);
        return String.format(order_id_gen, agentId, time, uid);
    }

    private Map<String, Object> buildParam(String params, String sign) {
        String fcAgentCode = configService.get(ConfigConstants.FC_AGENT_CODE);
        Map<String, Object> map = new HashMap<>();
        map.put("AgentCode", fcAgentCode);
        map.put("Currency", "USDT");
        map.put("Params", params);
        map.put("Sign", sign);
        return map;
    }

    public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");


}
