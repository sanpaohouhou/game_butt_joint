package com.tl.tgGame.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tl.tgGame.project.dto.*;
import com.tl.tgGame.project.entity.Game;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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


    /**
     * 以下为EG接口
     */
    /**
     * 创建账号
     */
    Boolean egCreateUser(ApiEgCreateUserReq req);

    /**
     * 玩家存款(充值)
     */
    ApiEgDepositRes egDeposit(ApiEgDepositReq req);

    /**
     * 玩家提款
     */
    ApiEgDepositRes egWithdraw(ApiEgWithdrawReq req);

    /**
     * 取得游戏列表
     */
    ApiEgGameListRes egGameList();

    /**
     * 进入游戏
     */
    String egEnterGame(ApiEgEnterGameReq req);

    /**
     * 根据时间钱包转移记录
     */
    void egTransferRecordByTime(ApiEgTransferRecordTimeReq req);

    /**
     * 根据时间捞取游戏记录
     */
    ApiEgRoundRecordRes egRoundRecordByTime(ApiEgRoundRecordTimeReq req);


    /**
     * 以下都是瓦力游戏接口
     */

    /**
     * 获取用户游戏余额
     * @return
     */
    ApiWlGameOrderRes wlGetUserBalance(Long userId);

    /**
     * 进入游戏
     * @return
     */
    String wlEnterGame(Long userId,String gameId,HttpServletRequest request);

    /**
     * 划转订单
     * @param
     */
    ApiWlGameRes wlPayOrder(Long userId,BigDecimal amount);

    /**
     * wl获取游戏记录列表
     */
    ApiWlGameResponse wlGameRecord(LocalDateTime startTime,LocalDateTime endTime);

}
