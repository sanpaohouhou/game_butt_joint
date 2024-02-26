package com.tl.tgGame.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tl.tgGame.project.entity.GameTransfer;
import com.tl.tgGame.project.mapper.GameTransferMapper;
import com.tl.tgGame.project.service.GameTransferService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @version 1.0
 * @auther w
 * @date 2024/1/23 , 18:38
 */
@Service
public class GameTransferServiceImpl extends ServiceImpl<GameTransferMapper, GameTransfer> implements GameTransferService {


    @Override
    public Boolean add(Long userId, String gameAccount, BigDecimal beforeAmount, BigDecimal amount, BigDecimal afterAmount, String sn, String remark, String business) {
        GameTransfer gameTransfer = buildGameTransfer(userId, gameAccount, beforeAmount, amount, afterAmount, sn, remark, business);
        return save(gameTransfer);
    }





    private GameTransfer buildGameTransfer(Long userId, String gameAccount, BigDecimal beforeAmount, BigDecimal amount, BigDecimal afterAmount, String sn, String remark, String business){
        return GameTransfer.builder()
                .createTime(LocalDateTime.now())
                .userId(userId)
                .gameAccount(gameAccount)
                .beforeAmount(beforeAmount)
                .amount(amount)
                .afterAmount(afterAmount)
                .sn(sn)
                .remark(remark)
                .business(business)
                .build();
    }
}
