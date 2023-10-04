package com.zerobase.foodlier.common.aop.notification.dto;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;

public interface Notify {

    Member getAssosiatedMember();
    Member getAssosiatedOtherMember();
    NotificationType getNotificationType();
    String getRequestName();
    Long getUrlId();

}
