package com.tl.tgGame.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tl.tgGame.common.lock.RedisLock;
import com.tl.tgGame.project.entity.Bet;
import com.tl.tgGame.project.mapper.BetMapper;
import com.tl.tgGame.project.service.BetService;
import com.tl.tgGame.util.RedisKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 17:41
 */
@Service
public class BetServiceImpl extends ServiceImpl<BetMapper, Bet> implements BetService {


    @Autowired
    private RedisKeyGenerator redisKeyGenerator;

    @Autowired
    private RedisLock redisLock;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean bet(Long userId,Bet bet) {
        String key = redisKeyGenerator.generateKey("bet", bet.getBankId(), userId);
        redisLock.redissonLock(key);
        try {

        }finally {
            redisLock._redissonLock(key);
        }
        return null;
    }
}
