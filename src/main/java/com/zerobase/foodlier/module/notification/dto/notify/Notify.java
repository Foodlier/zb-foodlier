package com.zerobase.foodlier.module.notification.dto.notify;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;

public interface Notify {

    String getMessage();
    Member getReceiver();
    NotificationType getNotificationType();
    String getReceiverEmail();
}
