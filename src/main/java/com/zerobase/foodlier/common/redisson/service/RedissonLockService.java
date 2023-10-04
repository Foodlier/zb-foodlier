package com.zerobase.foodlier.common.redisson.service;

public interface RedissonLockService {

    void lock(String group, String key);


    void unlock(String group, String key);
}
