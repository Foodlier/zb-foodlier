package com.zerobase.foodlier.common.redis.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "EmailVerification", timeToLive = 60 * 10) //10분 간만 존재함.
public class EmailVerification {

    @Id
    private String email;

    private String verificationCode;

    private LocalDateTime expiredAt;

    private boolean isAuthorized;

}
