package com.tl.tgGame.common.lock;

import com.tl.tgGame.exception.ErrorEnum;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisLock {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private Redisson redisson;

    public void lock(String key, Long expireTime, TimeUnit timeUnit) {
        if (!this._lock(key, expireTime, timeUnit)) ErrorEnum.SYSTEM_BUSY.throwException();
    }

    public boolean _lock(String key, Long expireTime, TimeUnit timeUnit) {
        String uuid = UUID.randomUUID().toString();
        BoundValueOperations<String, String> ops = stringRedisTemplate.boundValueOps(key);
        Boolean absent = ops.setIfAbsent(uuid, expireTime, timeUnit);
        if (absent != null && absent) {
            Map<String, String> map = threadLocal.get();
            if (map == null) {
                map = new HashMap<>();
                threadLocal.set(map);
            }
            map.put(key, uuid);
            return true;
        } else {
            return false;
        }
    }

    public void redissonLock(String key) {
        redissonLock(key, -1L);
    }

    public void redissonLock(String key, long waitTime) {
        redissonLock(key, waitTime, TimeUnit.MILLISECONDS);

    }

    public void redissonLock(String key, long waitTime, TimeUnit timeUnit) {
        redissonLock(key, waitTime, -1, timeUnit);
    }

    public void redissonLock(String key, long waitTime, long leaseTime, TimeUnit timeUnit) {
        if (!this._redissonLock(key, waitTime, leaseTime, timeUnit)) ErrorEnum.SYSTEM_BUSY.throwException();
    }

    public boolean _redissonLock(String key) {
        return _redissonLock(key, -1L);
    }

    public boolean _redissonLock(String key, long waitTime) {
        return _redissonLock(key, waitTime, TimeUnit.MILLISECONDS);

    }

    public boolean _redissonLock(String key, long waitTime, TimeUnit timeUnit) {
        return _redissonLock(key, waitTime, -1, timeUnit);
    }


    /**
     * redisson 分布式锁
     *
     * @param key       加锁key
     * @param waitTime  尝试加锁的时长
     * @param leaseTime 加锁时长
     * @param timeUnit  时间单位
     * @return 加锁的状态
     */
    public boolean _redissonLock(String key, long waitTime, long leaseTime, @NotNull TimeUnit timeUnit) {
        RLock lock = redisson.getLock(key);
        try {
            boolean tryLock = lock.tryLock(waitTime, leaseTime, timeUnit);
            if (tryLock) {
                Set<String> set = threadLocal2.get();
                if (set == null) {
                    set = new HashSet<>();
                    threadLocal2.set(set);
                }
                set.add(key);
            }
            return tryLock;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void redissonUnLock() {
        Set<String> set = threadLocal2.get();
        if (set != null) {
            for (String key : set) {
                _redissonUnLock(key);
            }
        }
    }

    public void _redissonUnLock(String key) {
        RLock lock = redisson.getLock(key);
        if (lock.isLocked() && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    public void unlock(String key) {
        String attribute = null;
        Map<String, String> map = threadLocal.get();
        if (map != null) {
            attribute = map.get(key);
        }
        if (attribute != null && !attribute.isEmpty()) {
            BoundValueOperations<String, String> ops = stringRedisTemplate.boundValueOps(key);
            String value = ops.get();
            if (!StringUtils.isEmpty(value) && value.equals(attribute)) {
                stringRedisTemplate.delete(key);
                map.remove(key);
            }
        }
    }


    public void unlock() {
        Map<String, String> map = threadLocal.get();
        if (map != null) {
            Set<String> stringSet = new HashSet<>(map.keySet());
            for (String key : stringSet) {
                unlock(key);
            }
        }
    }

    private final ThreadLocal<Map<String, String>> threadLocal = new ThreadLocal<>();
    private final ThreadLocal<Set<String>> threadLocal2 = new ThreadLocal<>();
}
