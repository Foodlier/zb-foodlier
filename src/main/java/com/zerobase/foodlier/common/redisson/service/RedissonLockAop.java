package com.zerobase.foodlier.common.redisson.service;

import com.zerobase.foodlier.common.aop.RedissonLock;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class RedissonLockAop {
    private final RedissonLockService redissonLockService;

    @Around("@annotation(com.zerobase.foodlier.common.aop.RedissonLock)")
    public Object lock(
            final ProceedingJoinPoint joinPoint
    ) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Method method = signature.getMethod();

        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);

        String keyExpression = redissonLock.key();
        String key = getKey(keyExpression, joinPoint);
        String group = redissonLock.group();

        redissonLockService.lock(group, key);
        try {
            return joinPoint.proceed();
        } finally {
            redissonLockService.unlock(group, key);
        }
    }

    private String getKey(String expression, ProceedingJoinPoint joinPoint) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        ExpressionParser parser = new SpelExpressionParser();
        return parser.parseExpression(expression).getValue(context, String.class);
    }
}
