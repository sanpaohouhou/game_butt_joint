package com.tl.tgGame.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tl.tgGame.project.dto.ApiWlGameRecordData;
import com.tl.tgGame.project.entity.WlBet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/1 , 16:42
 */
public interface WlBetService extends IService<WlBet> {

    Boolean addWlBet(ApiWlGameRecordData data);

    Boolean wlCommission(WlBet wlBet);
}
