package com.tl.tgGame.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tl.tgGame.project.entity.GameTransfer;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2024/1/23 , 18:37
 */
public interface GameTransferService extends IService<GameTransfer> {

    Boolean add(Long userId, String gameAccount, BigDecimal beforeAmount,
                BigDecimal amount,BigDecimal afterAmount,String sn,String remark,String business);


}
