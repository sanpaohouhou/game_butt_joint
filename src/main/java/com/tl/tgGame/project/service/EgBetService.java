package com.tl.tgGame.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tl.tgGame.project.dto.ApiEgGameRecordRes;
import com.tl.tgGame.project.entity.EgBet;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/1 , 16:51
 */
public interface EgBetService extends IService<EgBet> {

    Boolean addEgBet(List<ApiEgGameRecordRes> date, LocalDateTime pullTime);

    Boolean egCommission(EgBet egBet);
}
