package com.zerobase.foodlier.common.redis.service;

import com.zerobase.foodlier.common.redis.domain.model.RefreshToken;
import com.zerobase.foodlier.common.redis.dto.RefreshTokenDto;

public interface RefreshTokenService {

    boolean isRefreshTokenExisted(String email);

    void save(RefreshTokenDto refreshTokenDto);

    void delete(String email);

    RefreshToken findRefreshToken(String email);

}
