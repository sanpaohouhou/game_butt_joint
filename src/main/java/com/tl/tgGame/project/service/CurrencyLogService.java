package com.tl.tgGame.project.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.tl.tgGame.project.entity.CurrencyLog;
import com.tl.tgGame.project.enums.BusinessEnum;
import com.tl.tgGame.project.enums.CurrencyLogType;
import com.tl.tgGame.project.enums.UserType;
import com.tl.tgGame.project.mapper.CurrencyLogMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 余额变动记录表 服务实现类
 * </p>
 *
 * @author hd
 * @since 2020-12-04
 */
@Service
public class CurrencyLogService extends ServiceImpl<CurrencyLogMapper, CurrencyLog> {

    public void add(long uid, UserType type, CurrencyLogType logType, BusinessEnum business, BigDecimal amount, Object sn,
                    String des, BigDecimal balance, BigDecimal freeze, BigDecimal remain) {
        CurrencyLog currencyLog = CurrencyLog.builder()
                .uid(uid)
                .sn(String.valueOf(sn))
                .userType(type)
                .logType(logType)
                .business(business)
                .des(des)
                .balance(balance)
                .freeze(freeze)
                .remain(remain)
                .amount(amount)
                .createTime(LocalDateTime.now())
                .build();
        save(currencyLog);
    }
}
