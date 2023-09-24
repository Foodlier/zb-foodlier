package com.zerobase.foodlier.common.redis.service;

import com.zerobase.foodlier.common.redis.domain.model.RefreshToken;
import com.zerobase.foodlier.common.redis.dto.RefreshTokenDto;

public interface RefreshTokenService {

    void validRefreshToken(String email);
    void save(RefreshTokenDto refreshTokenDto);
    void delete(RefreshToken refreshToken);
    RefreshToken findRefreshToken(String email);

}
