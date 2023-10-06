package com.zerobase.foodlier.common.redisson.service;

import com.zerobase.foodlier.common.redisson.exception.RedissonException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import static com.zerobase.foodlier.common.redisson.exception.RedissonErrorCode.LOCK_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RedissonLockServiceImplTest {
    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock rLock;

    @InjectMocks
    private RedissonLockServiceImpl redissonLockService;

    @Test
    @DisplayName("lock 성공")
    void success_lock() throws InterruptedException {
        //given
        given(redissonClient.getLock(anyString()))
                .willReturn(rLock);
        given(rLock.tryLock(anyLong(), anyLong(), any()))
                .willReturn(true);

        //when

        //then
        assertDoesNotThrow(() -> redissonLockService.lock("heart", "1"));
    }

    @Test
    @DisplayName("lock 실패 - lock 오류")
    void fail_lock_lockError() throws InterruptedException {
        //given
        given(redissonClient.getLock(anyString()))
                .willReturn(rLock);
        given(rLock.tryLock(anyLong(), anyLong(), any()))
                .willReturn(false);

        //when
        RedissonException redissonException = assertThrows(RedissonException.class,
                () -> redissonLockService.lock("heart", "1"));

        //then
        assertEquals(LOCK_ERROR, redissonException.getErrorCode());
    }
}