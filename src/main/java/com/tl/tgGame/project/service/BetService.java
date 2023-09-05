package com.tl.tgGame.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tl.tgGame.project.dto.ApiGameRecordListDTO;
import com.tl.tgGame.project.entity.Bet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 17:41
 */
public interface BetService extends IService<Bet> {

    Boolean bet(Long userId,Bet bet);

    Boolean addBet(List<ApiGameRecordListDTO> result);

    Boolean fcCommission(Bet bet);

    BigDecimal sumAmount(Long userId,Boolean hasSettled);

    BigDecimal sumBetAmount(Long userId, LocalDateTime startTime,LocalDateTime endTime);

    BigDecimal sumWinLose(Long userId,LocalDateTime startTime,LocalDateTime endTime,Boolean hasWinLose);
}
