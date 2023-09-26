package com.zerobase.foodlier.common.security.provider.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenExpiredConstant {

    private final Long MILLISECOND = 1000L;

    @Value("${spring.token-expired.access.second}")
    private long accessSecond;

    @Value("${spring.token-expired.access.minute}")
    private long accessMinute;

    @Value("${spring.token-expired.access.hour}")
    private long accessHour;

    @Value("${spring.token-expired.refresh.second}")
    private long refreshSecond;

    @Value("${spring.token-expired.refresh.minute}")
    private long refreshMinute;

    @Value("${spring.token-expired.refresh.hour}")
    private long refreshHour;

    private TokenExpiredConstant() {

    }

    public long getAccessTokenExpiredTime() {
        return accessHour * accessMinute * accessSecond * MILLISECOND;
    }

    public long getRefreshTokenExpiredTime() {
        return refreshHour * refreshMinute * refreshSecond * MILLISECOND;
    }

    public long getRefreshTokenExpiredMinute() {
        return refreshHour * refreshMinute * refreshSecond;
    }

    public Date getAccessTokenExpiredDate(Date date) {
        return new Date(date.getTime() + getAccessTokenExpiredTime());
    }

    public Date getRefreshTokenExpiredDate(Date date) {
        return new Date(date.getTime() + getRefreshTokenExpiredTime());
    }

}
