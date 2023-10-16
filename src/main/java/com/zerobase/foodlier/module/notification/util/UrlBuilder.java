package com.zerobase.foodlier.module.notification.util;

import com.zerobase.foodlier.common.aop.notification.type.NotifyUrl;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlBuilder {
    private static final String OPEN_BRACKET ="{";
    private static final String CLOSE_BRACKET ="}";

    private NotifyUrl notifyUrl;
    private Long id;

    public String getUrl(){
        return notifyUrl.getUrl() + OPEN_BRACKET + id + CLOSE_BRACKET;
    }

}
