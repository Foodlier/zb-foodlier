package com.zerobase.foodlier.module.notification.util;


import com.zerobase.foodlier.common.aop.notification.type.ActionType;
import com.zerobase.foodlier.common.aop.notification.type.PerformerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageBuilder {
    private static final String HONORIFIC_TITLE = "님이";
    private static final String DELIMITER = " ";
    private PerformerType performerType;
    private ActionType actionType;
    private String nickname;
    private String subject;

    public String getMessage() {
        List<String> messageComponent = List.of(this.performerType.getKorean(),
                nickname, HONORIFIC_TITLE, subject, actionType.getKorean());
        return String.join(DELIMITER, messageComponent);
    }


}
