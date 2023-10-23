package com.zerobase.foodlier.common.redisson.service;

import com.zerobase.foodlier.common.redisson.exception.RedissonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.zerobase.foodlier.common.redisson.exception.RedissonErrorCode.LOCK_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedissonLockService {
    private final RedissonClient redissonClient;

    private final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    public void lock(String group, String key, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(getLockKey(group, key));

        try {
            boolean isLock = lock.tryLock(waitTime, leaseTime, TIME_UNIT);
            if (!isLock) {
                throw new RedissonException(LOCK_ERROR);
            }
        } catch (RedissonException e) {
            throw e;
        } catch (Exception e) {
            log.error("Redisson lock failed");
        }
    }

    public void unlock(String group, String key) {
        RLock lock = redissonClient.getLock(getLockKey(group, key));
        try {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        } catch (IllegalMonitorStateException e) {
            log.error("Redisson unlock failed");
        }
    }

    private static String getLockKey(String group, String key) {
        return "Lock : " + group + "-" + key;
    }
}
