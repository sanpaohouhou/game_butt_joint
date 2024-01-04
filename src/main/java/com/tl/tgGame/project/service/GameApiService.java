package com.tl.tgGame.project.service;

import com.tl.tgGame.project.dto.ApiGameCancelBetReq;
import com.tl.tgGame.project.dto.ApiGameCommonDTO;
import com.tl.tgGame.project.dto.ApiGameReq;
import com.tl.tgGame.project.dto.GameApiResponse;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/7 , 16:13
 */
public interface GameApiService {

    /**
     * 取消下注与游戏结果
     */
    GameApiResponse cancelBetAndInfo(ApiGameReq req);

    /**
     * 取得余额
     */
    GameApiResponse getBalance(ApiGameReq req);

    /**
     * 下注信息及游戏结果
     */
    GameApiResponse getBetOrInfo(ApiGameReq req);

    /**
     * 活动派彩接口
     */
    GameApiResponse activity(ApiGameReq req);

    /**
     * 下注接口
     */
    GameApiResponse bet(ApiGameReq req);

    /**
     * 结算接口
     */
    GameApiResponse Settle(ApiGameReq req);

    /**
     * 取消下注
     */
    GameApiResponse cancelBet(ApiGameReq req);
}
