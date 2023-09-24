package com.zerobase.foodlier.common.redis.service;

import com.zerobase.foodlier.common.redis.domain.model.RefreshToken;
import com.zerobase.foodlier.common.redis.dto.RefreshTokenDto;
import com.zerobase.foodlier.common.redis.exception.RefreshTokenException;
import com.zerobase.foodlier.common.redis.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.foodlier.common.redis.exception.RefreshTokenErrorCode.REFRESH_NOT_FOUND;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findRefreshToken(String email) {
        return refreshTokenRepository.findById(email)
                .orElseThrow(()->new RefreshTokenException(REFRESH_NOT_FOUND));
    }

    public void validRefreshToken(String email) {
        if (!refreshTokenRepository.existsById(email)) {
            throw new RefreshTokenException(REFRESH_NOT_FOUND);
        }
    }

    public void save(RefreshTokenDto refreshTokenDto) {
        refreshTokenRepository.save(RefreshToken.builder()
                .email(refreshTokenDto.getUserEmail())
                .refreshToken(refreshTokenDto.getRefreshToken())
                .expiration(refreshTokenDto.getTimeToLive())
                .build());
    }

    public void delete(RefreshToken refreshToken) {
        if (refreshTokenRepository.existsById(refreshToken.getEmail())) {
            refreshTokenRepository.delete(refreshToken);
        }
    }
}
