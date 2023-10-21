package com.zerobase.foodlier.common.redis.service;

import com.zerobase.foodlier.common.redis.domain.model.RefreshToken;
import com.zerobase.foodlier.common.redis.dto.RefreshTokenDto;
import com.zerobase.foodlier.common.redis.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public boolean isRefreshTokenExisted(String email) {
        return refreshTokenRepository.existsById(email);
    }

    public void save(RefreshTokenDto refreshTokenDto) {
        refreshTokenRepository.save(RefreshToken.builder()
                .email(refreshTokenDto.getMemberEmail())
                .refreshToken(refreshTokenDto.getRefreshToken())
                .expiration(refreshTokenDto.getTimeToLive())
                .build());
    }

    public void delete(String email) {
        if (refreshTokenRepository.existsById(email)) {
            refreshTokenRepository.deleteById(email);
        }
    }
}
