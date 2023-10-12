package com.zerobase.foodlier.common.aop.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class LogAop {
    @Pointcut("execution(* com.zerobase.foodlier.global..controller.*Controller.*(..))")
    private void cut() {
    }

    @Before("cut()")
    public void beforeParameterLog(JoinPoint joinPoint) {
        Method method = getMethod(joinPoint);
        log.info("method name = {}", method.getName());

        Object[] args = joinPoint.getArgs();
        if (args.length == 0) {
            log.info("no parameter");
        }
        for (Object arg : args) {
            log.info("parameter tpye = {}", arg.getClass().getSimpleName());
        }
    }

    @AfterReturning(value = "cut()", returning = "returnObj")
    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {
        Method method = getMethod(joinPoint);
        log.info("method name = {}", method.getName());

        log.info("return type = {}", returnObj.getClass().getSimpleName());
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        return signature.getMethod();
    }
}
