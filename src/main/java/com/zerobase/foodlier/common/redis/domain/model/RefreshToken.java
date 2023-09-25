package com.zerobase.foodlier.common.redis.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;



@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "RefreshToken") // refresh token의 만료 기간: 24시간
public class RefreshToken {

    @Id
    private String email;

    private String refreshToken;

    @TimeToLive
    private Long expiration;

}
