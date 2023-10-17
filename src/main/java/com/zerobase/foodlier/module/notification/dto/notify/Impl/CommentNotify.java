package com.zerobase.foodlier.module.notification.dto.notify.Impl;

import com.zerobase.foodlier.module.comment.comment.domain.model.Comment;
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
public class CommentNotify implements Notify {
    private Member receiver;
    private String performerNickname;
    private Long targetSubjectId;
    private String targetTitle;
    private NotifyInfoDto notifyInfoDto;

    public static CommentNotify from(Comment comment, NotifyInfoDto notifyInfoDto){
        return CommentNotify.builder()
                .receiver(comment.getRecipe().getMember())
                .performerNickname(comment.getMember().getNickname())
                .targetSubjectId(comment.getRecipe().getId())
                .targetTitle(comment.getRecipe().getSummary().getTitle())
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
        return this.notifyInfoDto.getNotificationType();
    }

    @Override
    public String getReceiverEmail() {
        return this.receiver.getEmail();
    }
}
