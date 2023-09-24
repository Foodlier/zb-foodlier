package com.zerobase.foodlier.common.security.provider.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class TokenExpiredConstant {

    @Value("${spring.token-expired.millisecond-unit}")
    private long accessMillis;

    @Value("${spring.token-expired.access.second}")
    private long accessSecond;

    @Value("${spring.token-expired.access.minute}")
    private long accessMinute;

    @Value("${spring.token-expired.access.hour}")
    private long accessHour;

    @Value("${spring.token-expired.millisecond-unit}")
    private long refreshMillis;

    @Value("${spring.token-expired.refresh.second}")
    private long refreshSecond;

    @Value("${spring.token-expired.refresh.minute}")
    private long refreshMinute;

    @Value("${spring.token-expired.refresh.hour}")
    private long refreshHour;

    private TokenExpiredConstant(){

    }

    public long getAccessTokenExpiredTime(){
        return accessHour * accessMinute * accessSecond * accessMillis;
    }

    public long getRefreshTokenExpiredTime(){
        return refreshHour * refreshMinute * refreshSecond * refreshMillis;
    }


}
