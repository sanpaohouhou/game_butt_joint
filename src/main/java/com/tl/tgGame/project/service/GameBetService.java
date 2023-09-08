package com.tl.tgGame.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tl.tgGame.project.entity.GameBet;
import com.tl.tgGame.project.enums.GameBusiness;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/6 , 11:28
 */
public interface GameBetService extends IService<GameBet> {


    BigDecimal sumAmount(Long userId, Boolean hasSettled, String gameBusiness);

    BigDecimal sumBetAmount(Long userId, LocalDateTime startTime, LocalDateTime endTime, Boolean hasSettled);

    BigDecimal sumWinLose(Long userId,LocalDateTime startTime,LocalDateTime endTime,Boolean hasWinLose,String gameBusiness);

    Integer todayBetUserCount(LocalDateTime startTime,LocalDateTime endTime);

}
