package com.zerobase.foodlier.common.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedissonLock {
    String key() default "";
    String group() default "";
}
