package com.zerobase.foodlier.module.notification.dto.notify.impl;

import com.zerobase.foodlier.module.heart.domain.model.Heart;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;
import com.zerobase.foodlier.module.notification.dto.notify.Notify;
import com.zerobase.foodlier.module.notification.dto.notify.NotifyInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.zerobase.foodlier.module.notification.constant.MessageConstant.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeartNotify implements Notify {

    private Member receiver;
    private String performerNickname;
    private Long targetSubjectId;
    private String targetTitle;
    private NotifyInfoDto notifyInfoDto;

    public static HeartNotify from(Heart heart, NotifyInfoDto notifyInfoDto){
        return HeartNotify.builder()
                .receiver(heart.getRecipe().getMember())
                .performerNickname(heart.getMember().getNickname())
                .targetSubjectId(heart.getRecipe().getId())
                .targetTitle(heart.getRecipe().getSummary().getTitle())
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
    public Member getReceiver() {
        return this.receiver;
    }

    @Override
    public NotificationType getNotificationType() {
        return this.notifyInfoDto.getNotificationType();
    }

    @Override
    public String getReceiverEmail() {
        return this.receiver.getEmail();
    }
}
