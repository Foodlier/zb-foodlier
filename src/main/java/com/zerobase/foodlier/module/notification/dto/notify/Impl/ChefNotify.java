package com.zerobase.foodlier.module.notification.dto.notify.Impl;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;
import com.zerobase.foodlier.module.notification.dto.notify.Notify;
import com.zerobase.foodlier.module.notification.dto.notify.NotifyInfoDto;
import com.zerobase.foodlier.module.request.domain.model.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.zerobase.foodlier.module.notification.constant.MessageConstant.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChefNotify implements Notify {
    private Member receiver;
    private String performerNickname;
    private Long targetSubjectId;
    private String targetTitle;
    private NotifyInfoDto notifyInfoDto;

    public static ChefNotify from(Request request, NotifyInfoDto notifyInfoDto){
        return ChefNotify.builder()
                .receiver(request.getChefMember().getMember())
                .performerNickname(request.getMember().getNickname())
                .targetSubjectId(request.getId())
                .targetTitle(request.getTitle())
                .notifyInfoDto(notifyInfoDto)
                .build();
    }


    @Override
    public String getMessage() {
        List<String> messageComponent = List.of(notifyInfoDto.getPerformer(),
                this.performerNickname, HONORIFIC_TITLE, this.targetTitle, notifyInfoDto.getAction());
        return String.join(DELIMITER, messageComponent);
    }


    @Override
    public NotificationType getNotificationType() {
        return notifyInfoDto.getNotificationType();
    }

    @Override
    public String getReceiverEmail() {
        return this.receiver.getEmail();
    }
}
