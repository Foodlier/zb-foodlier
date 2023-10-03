package com.zerobase.foodlier.common.aop.notification.type;

import com.zerobase.foodlier.common.aop.notification.dto.Notify;
import com.zerobase.foodlier.module.notification.exception.NotificationException;

import java.util.Arrays;
import java.util.Locale;

import static com.zerobase.foodlier.module.notification.exception.NotificationErrorCode.INVALID_NOTIFICATION;

public enum NotifyUrl {
    REQUEST("request", (objectId)->"/cookForMe{"+objectId+"}"),
    HEART("heart",(objectId)->"/recipeDetailPage{"+objectId+"}"),
    COMMENT("comment", (objectId)->"/recipeDetailPage{"+objectId+"}"),
    ;
    private final String notificationType;
    private final UrlMaker urlMaker;

    private NotifyUrl(String notificationType, UrlMaker urlMaker) {
        this.urlMaker = urlMaker;
        this.notificationType = notificationType;
    }

    public String createUrl(Long objectId){
        return this.urlMaker.getUrl(objectId);
    }

    @FunctionalInterface
    public interface UrlMaker {
        String getUrl(Long objectId);
    }

    private static NotifyUrl findNotifyUrlBy(Notify notify){
        return Arrays.stream(NotifyUrl.values())
                .filter(notifyUrl -> notifyUrl.notificationType.toUpperCase(Locale.ROOT)
                        .equals(notify.getNotificationType().name()))
                .findFirst()
                .orElseThrow(()->new NotificationException(INVALID_NOTIFICATION));
    }

    public static String getUrl(Notify notify){
        NotifyUrl notifyUrl = findNotifyUrlBy(notify);
        return notifyUrl.createUrl(notify.getUrlId());
    }

}
