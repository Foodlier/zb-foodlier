package com.zerobase.foodlier.common.aop.notification.annotation;

import com.zerobase.foodlier.common.aop.notification.type.ActionType;
import com.zerobase.foodlier.common.aop.notification.type.SendType;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Notification {

    NotificationType notificationType();

    SendType sendTo();

    ActionType action();

}
