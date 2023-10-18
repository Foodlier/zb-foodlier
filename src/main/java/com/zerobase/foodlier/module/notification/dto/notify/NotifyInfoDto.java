package com.zerobase.foodlier.module.notification.dto.notify;

import com.zerobase.foodlier.module.notification.domain.type.ActionType;
import com.zerobase.foodlier.module.notification.domain.type.PerformerType;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotifyInfoDto {

    private PerformerType performerType;
    private ActionType actionType;
    private NotificationType notificationType;
    public String getPerformer(){
        return this.performerType.getKorean();
    }

    public String getAction(){
        return this.actionType.getKorean();
    }


}
