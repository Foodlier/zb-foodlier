package com.zerobase.foodlier.common.aop.notification.type;

import com.zerobase.foodlier.common.aop.notification.annotation.Notification;
import com.zerobase.foodlier.common.aop.notification.dto.Notify;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;
import com.zerobase.foodlier.module.notification.exception.NotificationErrorCode;
import com.zerobase.foodlier.module.notification.exception.NotificationException;

import java.util.Arrays;

public enum NotifyMessage {

    MESSAGE_TO_REQUESTER(SendType.REQUESTER, NotificationType.REQUEST, (requestTitle, chefName, action)
            -> String.format("요청 제목 %s에 대해 요리사 %s님이 %s하셨습니다.", requestTitle, chefName, action)),
    MESSAGE_TO_CHEF(SendType.CHEF, NotificationType.REQUEST, (requestTitle, requesterName, action)
            -> String.format("요청 제목 %s에 대해 요청자 %s님이 %s하셨습니다.", requestTitle, requesterName, action)),
    MESSAGE_TO_WRITER_WHEN_PUSH_HEART(SendType.WRITER, NotificationType.HEART, (recipeTitle, whoPushHeart, action)
            -> String.format("레시피 제목 %s에 대해 사용자 %s님이 %s를 누르셨습니다.", recipeTitle, whoPushHeart, action)),
    MESSAGE_TO_WRITER_WHEN_COMMENT(SendType.WRITER, NotificationType.COMMENT,(recipeTitle, whoComment, action)
            -> String.format("레시피 제목 %s에 대해 사용자 %s님이 %s을 작성하셨습니다.", recipeTitle, whoComment, action)),
    MESSAGE_TO_COMMENTER_WHEN_RE_COMMENT(SendType.COMMENTER, NotificationType.RE_COMMENT, (recipeTitle, whoRecomment, action)
            -> String.format("레시피 제목 %s에 대해 사용자 %s님이 %s을 작성하셨습니다.", recipeTitle, whoRecomment, action))
    ;
    private final SendType sendTo;
    private final NotificationType notificationType;
    private final RequestNotifyConverter requestNotifyConverter;

    NotifyMessage(SendType sendTo, NotificationType notificationType, RequestNotifyConverter requestNotifyConverter) {
        this.requestNotifyConverter = requestNotifyConverter;
        this.notificationType = notificationType;
        this.sendTo = sendTo;
    }

    public String createMessage(String notifyObjectName, String performer, String action) {
        return this.requestNotifyConverter.getDisplayName(notifyObjectName, performer, action);
    }

    @FunctionalInterface
    public interface RequestNotifyConverter {
        String getDisplayName(String notifyObjectName, String performer, String action);
    }

    private static NotifyMessage findMessage(SendType sendTo, NotificationType notificationType){
        return Arrays.stream(NotifyMessage.values())
                .filter(notifyMessage -> notifyMessage.sendTo==sendTo
                        && notifyMessage.notificationType == notificationType)
                .findFirst().orElseThrow(()->new NotificationException(NotificationErrorCode.INVALID_NOTIFICATION));
    }

    public static String getMessage(Notification notification, Notify notify){
        NotifyMessage notifyMessage = findMessage(notification.sendTo(), notification.notificationType());
        if(notifyMessage == NotifyMessage.MESSAGE_TO_CHEF){
            return notifyMessage.createMessage(notify.getRequestName(),
                    notify.getAssosiatedOtherMember().getNickname(),
                    notification.action().getKorean());
        }

        return notifyMessage.createMessage(notify.getRequestName(),
                notify.getAssosiatedMember().getNickname(),
                notification.action().getKorean());
    }


}
