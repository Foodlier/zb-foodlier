package com.zerobase.foodlier.common.redis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenDto {
    private String userEmail;
    private String refreshToken;
    private Long timeToLive;
}
