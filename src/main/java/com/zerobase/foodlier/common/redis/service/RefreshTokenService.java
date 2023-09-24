package com.zerobase.foodlier.common.redis.service;

public interface RefreshTokenService {
    void createRefreshToken(String refreshToken,String email);
    void signOut(String refreshToken);
}
