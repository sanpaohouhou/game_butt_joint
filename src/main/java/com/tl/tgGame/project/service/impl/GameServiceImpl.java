package com.tl.tgGame.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.tl.tgGame.project.dto.*;
import com.tl.tgGame.project.entity.Game;
import com.tl.tgGame.project.mapper.GameMapper;
import com.tl.tgGame.project.service.GameService;
import com.tl.tgGame.project.util.AesGameUtil;
import com.tl.tgGame.util.HttpUtil;
import com.tl.tgGame.util.Md5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/4 , 17:43
 */
@Slf4j
@Service
public class GameServiceImpl extends ServiceImpl<GameMapper, Game> implements GameService {

    private static final String AGENT_KEY = "rK8dho7YOHoUUKy8";

    private static final String URL = "https://api.fcg666.net";

    private static final String AGENT_CODE = "XM809";

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

    @Override
    public void addUser(ApiAddMemberReq req) {
        try {
            String json = new Gson().toJson(req);
            String aesEncrypt = AesGameUtil.aesEncrypt(json, AGENT_KEY);
            String sign = Md5Util.MD5(json, 32);
            Map<String, Object> params = buildParam(aesEncrypt, sign);
            System.out.println("params:{}" + params);
            String body = HttpUtil.doPost(URL + addMemberUrl, params, "UTF-8", "multipart/form-data");
            System.out.println(body);
        } catch (Exception e) {

        }
    }

    @Override
    public String login(ApiLoginReq req) {
        try {
            String json = new Gson().toJson(req);
            String aesEncrypt = AesGameUtil.aesEncrypt(json, AGENT_KEY);
            String sign = Md5Util.MD5(json, 32);
            Map<String, Object> params = buildParam(aesEncrypt, sign);
            log.info("params:{}", params);
            String body = HttpUtil.doPost(URL + "/Login", params, "UTF-8", "multipart/form-data");
            if (body != null) {
                System.out.println(body);
                ApiLoginRes apiLoginRes = new Gson().fromJson(body, ApiLoginRes.class);
                if (apiLoginRes.getResult().equals("0")) {
                    return apiLoginRes.getUrl();
                }
            }
        } catch (Exception e) {
            log.error("获取游戏登录链接失败Exception:{},gameId:{}", e, req.getGameID());
        }
        return null;
    }

    @Override
    public void kickOut(ApiAddMemberReq req) {
        try {
            String aesEncrypt = AesGameUtil.aesEncrypt(new Gson().toJson(req), AGENT_KEY);
            String sign = Md5Util.MD5(new Gson().toJson(req), 32);
            Map<String, Object> params = buildParam(aesEncrypt, sign);
            String body = HttpUtil.doPost(URL + kickOutUrl, params, "UTF-8", "multipart/form-data");
            System.out.println(body);
        } catch (Exception e) {

        }
    }

    @Override
    public void kickOutAll() {
        try {
            String aesEncrypt = AesGameUtil.aesEncrypt(new Gson().toJson("{}"), AGENT_KEY);
            String sign = Md5Util.MD5(new Gson().toJson(""), 32);
            Map<String, Object> params = buildParam(aesEncrypt, sign);
            String body = HttpUtil.doPost(URL + kickOutAllUrl, params, "UTF-8", "multipart/form-data");
            System.out.println(body);

        } catch (Exception e) {

        }
    }

    @Override
    public void searchMember(ApiAddMemberReq req) {
        try {
            String json = new Gson().toJson(req);
            String aesEncrypt = AesGameUtil.aesEncrypt(json, AGENT_KEY);
            String sign = Md5Util.MD5(json, 32);
            Map<String, Object> params = buildParam(aesEncrypt, sign);
            String body = HttpUtil.doPost(URL + searchMember, params, "UTF-8", "multipart/form-data");
            System.out.println(body);
        } catch (Exception e) {

        }
    }

    @Override
    public void setPoints(ApiSetPointReq req) {
        try {
            String json = new Gson().toJson(req);
            String aesEncrypt = AesGameUtil.aesEncrypt(json, AGENT_KEY);
            String sign = Md5Util.MD5(json, 32);
            Map<String, Object> params = buildParam(aesEncrypt, sign);
            String body = HttpUtil.doPost(URL + setPoints, params, "UTF-8", "multipart/form-data");
            System.out.println(body);

        } catch (Exception e) {

        }
    }

    @Override
    public void getSingleBill(ApiGetSingleBillReq req) {
        try {
            String json = new Gson().toJson(req);
            String aesEncrypt = AesGameUtil.aesEncrypt(json, AGENT_KEY);
            String sign = Md5Util.MD5(json, 32);
            Map<String, Object> params = buildParam(aesEncrypt, sign);
            String body = HttpUtil.doPost(URL + getSingleBill, params, "UTF-8", "multipart/form-data");
            System.out.println(body);
        } catch (Exception e) {

        }
    }

    @Override
    public void getPlayerReport(ApiGetPlayerReportReq req) {
        try {
            String json = new Gson().toJson(req);
            String aesEncrypt = AesGameUtil.aesEncrypt(json, AGENT_KEY);
            String sign = Md5Util.MD5(json, 32);
            Map<String, Object> params = buildParam(aesEncrypt, sign);
            String body = HttpUtil.doPost(URL + getPlayReportUrl, params, "UTF-8", "multipart/form-data");
            System.out.println(body);
        } catch (Exception e) {

        }
    }

    @Override
    public void getRecordList(ApiRecordListReq req) {
        try {
            String json = new Gson().toJson(req);
            String aesEncrypt = AesGameUtil.aesEncrypt(json, AGENT_KEY);
            String sign = Md5Util.MD5(json, 32);
            Map<String, Object> params = buildParam(aesEncrypt, sign);
            String body = HttpUtil.doPost(URL + getRecordList, params,"UTF-8","multipart/form-data");
            System.out.println(body);

        } catch (Exception e) {

        }
    }

    @Override
    public void testSign(ApiAddMemberReq addMemberReq) {
        try {
            String json = new Gson().toJson(addMemberReq);
            String aes = AesGameUtil.aesEncrypt(json, AGENT_KEY);
            Map<String, Object> map = new HashMap<>();
            map.put("Params", json);
            map.put("ReParams", aes);
            map.put("AgentKey", AGENT_KEY);
            String s = HttpUtil.doPost(URL + "/Key", map, "UTF-8", "multipart/form-data");
            System.out.println(s);
        } catch (Exception e) {

        }

    }

    private Map<String, Object> buildParam(String params, String sign) {
        Map<String, Object> map = new HashMap<>();
        map.put("AgentCode", AGENT_CODE);
        map.put("Currency", "USDT");
        map.put("Params", params);
        map.put("Sign", sign);
        return map;
    }
}
