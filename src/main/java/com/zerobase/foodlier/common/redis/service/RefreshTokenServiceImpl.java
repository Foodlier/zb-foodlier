package com.zerobase.foodlier.common.redis.service;

import com.zerobase.foodlier.common.redis.domain.model.RefreshToken;
import com.zerobase.foodlier.common.redis.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void createRefreshToken(String refreshToken, String email) {
        RefreshToken token = RefreshToken.builder()
                .refreshToken(refreshToken)
                .email(email)
                .build();

        refreshTokenRepository.save(token);
    }

    @Override
    public void signOut(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findById(refreshToken)
                .orElseThrow();

        refreshTokenRepository.delete(token);
    }
}
