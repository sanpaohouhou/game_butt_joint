package com.tl.tgGame.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class IdGeneratorService implements ApplicationRunner {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String ID_KEY = "id:generator";

    private static final String START_ID = "10000";

    public Long incrementId() {
        return stringRedisTemplate.opsForValue().increment(ID_KEY);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        stringRedisTemplate.boundValueOps(ID_KEY).setIfAbsent(START_ID);
    }
}