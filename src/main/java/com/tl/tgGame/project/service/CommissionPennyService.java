package com.tl.tgGame.project.service;

import com.tl.tgGame.project.entity.Bet;
import com.tl.tgGame.project.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/24 , 15:32
 */
@Slf4j
@Service
public class CommissionPennyService {


    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private BetService betService;

    @Autowired
    private UserCommissionService userCommissionService;


    /**
     * FC游戏给上级分佣金
     */
    @Transactional
    public void fcCommissionPenny(Bet bet, User user){


    }





}
