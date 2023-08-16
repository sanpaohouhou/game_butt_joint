package com.tl.tgGame.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tl.tgGame.project.entity.Bet;
import com.tl.tgGame.project.entity.GameBet;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 17:41
 */
public interface BetService extends IService<Bet> {

    Boolean bet(Long userId,Bet bet);
}
