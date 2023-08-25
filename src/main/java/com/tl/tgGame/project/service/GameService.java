package com.tl.tgGame.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tl.tgGame.project.dto.*;
import com.tl.tgGame.project.entity.Game;

import java.util.List;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/4 , 17:42
 */
public interface GameService extends IService<Game> {

    /**
     * 新增玩家
     * @param req
     */
    void addUser(ApiAddMemberReq req);

    /**
     * 取得试玩游戏网址
     */

    /**登录游戏
     *
     * @param req
     * @return
     */
    String login(ApiLoginReq req);

    /**踢出玩家
     *
     * @param req
     */
    void kickOut(ApiAddMemberReq req);

    /**踢出全部玩家
     *
     */
    void kickOutAll();

    /**查询玩家基本信息
     *
     * @param req
     */
    void searchMember(ApiAddMemberReq req);

    /**玩家钱包充值
     *
     * @param req
     */
    ApiSetPointRes setPoints(ApiSetPointReq req);

    /**交易记录单笔查询
     *
     * @param req
     */
    void getSingleBill(ApiGetSingleBillReq req);

    /**取得玩家报表
     *
     * @param req
     */
    void getPlayerReport(ApiGetPlayerReportReq req);

    /**取得游戏记录
     * 查询在特定时间内的游戏记录。
     * 每次查询时间范围最多为 15 分钟。
     * 仅能查询 2 小时以內的游戏纪录。
     */
    List<ApiGameRecordListDTO> getRecordList(ApiRecordListReq req);

    /**签名验证测试
     *
     * @param addMemberReq
     */
    void testSign(ApiAddMemberReq addMemberReq);

}
