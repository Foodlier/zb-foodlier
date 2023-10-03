package com.zerobase.foodlier.common.aop.notification.aspect;

import com.zerobase.foodlier.common.aop.notification.annotation.Notification;
import com.zerobase.foodlier.common.aop.notification.dto.Notify;
import com.zerobase.foodlier.common.aop.notification.type.SendType;
import com.zerobase.foodlier.global.notification.facade.NotificationFacade;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.notification.domain.type.NotifyMessage;
import com.zerobase.foodlier.module.notification.domain.type.NotifyUrl;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Aspect
@Component
@EnableAsync
@RequiredArgsConstructor
public class NotificationAspect {

    private final NotificationFacade notificationFacade;

    @Pointcut("@annotation(com.zerobase.foodlier.common.aop.notification.annotation.Notification)")
    public void annotationPointcut() {
    }

    @Async
    @AfterReturning(pointcut = "annotationPointcut()", returning = "result")
    public void checkValue(JoinPoint joinPoint, Object result) throws Throwable {
        Notify notifyProxy = (Notify) result;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Notification notification = signature.getMethod().getAnnotation(Notification.class);
        Member receiver = this.decideReceiver(notification, notifyProxy);
        String message = NotifyMessage.getMessage(notification, notifyProxy);
        String url = NotifyUrl.getUrl(notifyProxy);

        notificationFacade.send(
                receiver,
                notifyProxy.getNotificationType(),
                message,
                url
        );
    }

    private Member decideReceiver(Notification notification, Notify notify){

        if(notification.notificationType()==notify.getNotificationType()
        && notification.sendTo()==SendType.REQUESTER){
            return notify.getAssosiatedMember();
        }
        return notify.getAssosiatedOtherMember();
    }
}
