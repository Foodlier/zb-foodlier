package com.zerobase.foodlier.common.redisson.service;

import com.zerobase.foodlier.common.aop.RedissonLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RedissonLockAopTest {
    @Mock
    private RedissonLockService redissonLockService;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private MethodSignature signature;


    @InjectMocks
    private RedissonLockAop redissonLockAop;

    static class TestService {
        @RedissonLock(key = "#key", group = "group", waitTime = 15L, leaseTime = 2L)
        public String testMethod() {
            return "Result";
        }
    }

    @Test
    @DisplayName("lock 성공")
    void success_lock() throws Throwable {
        //given
        given(joinPoint.getSignature())
                .willReturn(signature);
        given(signature.getMethod())
                .willReturn(TestService.class.getMethod("testMethod"));
        given(signature.getParameterNames())
                .willReturn(new String[]{"key"});
        given(joinPoint.getArgs())
                .willReturn(new Object[]{"key"});
        given(joinPoint.proceed())
                .willReturn("Result");

        ArgumentCaptor<String> lockCaptorGroup =
                ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> lockCaptorKey =
                ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> unlockCaptorGroup =
                ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> unlockCaptorKey =
                ArgumentCaptor.forClass(String.class);

        //when
        Object result = redissonLockAop.lock(joinPoint);

        //then
        verify(redissonLockService, times(1))
                .lock(lockCaptorGroup.capture(), lockCaptorKey.capture(), anyLong(), anyLong());
        verify(redissonLockService, times(1))
                .unlock(unlockCaptorGroup.capture(), unlockCaptorKey.capture());

        assertAll(
                () -> assertEquals("group", lockCaptorGroup.getValue()),
                () -> assertEquals("key", lockCaptorKey.getValue()),
                () -> assertEquals("group", unlockCaptorGroup.getValue()),
                () -> assertEquals("key", unlockCaptorKey.getValue()),
                () -> assertEquals("Result", result)
        );
    }
}