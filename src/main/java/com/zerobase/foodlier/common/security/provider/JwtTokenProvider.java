package com.zerobase.foodlier.common.security.provider;


import com.zerobase.foodlier.common.redis.domain.model.RefreshToken;
import com.zerobase.foodlier.common.redis.dto.RefreshTokenDto;
import com.zerobase.foodlier.common.redis.service.RefreshTokenService;
import com.zerobase.foodlier.common.security.exception.JwtException;
import com.zerobase.foodlier.common.security.provider.constants.TokenExpiredConstant;
import com.zerobase.foodlier.common.security.provider.dto.CreateTokenDto;
import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

import static com.zerobase.foodlier.common.security.exception.JwtErrorCode.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final String KEY_ROLES = "roles";

    private final TokenExpiredConstant tokenExpiredConstant;
    private final RefreshTokenService refreshTokenService;

    @Value("${spring.jwt.secret}")
    private String accessSecretKey;

    public String createAccessToken(Member member) {
        return setToken(CreateTokenDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .roles(member.getRoles())
                .keyRoles(KEY_ROLES)
                .tokenExpiredTime(tokenExpiredConstant.getAccessTokenExpiredTime())
                .secretKey(accessSecretKey)
                .build());
    }

    public String createRefreshToken(Member member) {
        return setToken(CreateTokenDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .tokenExpiredTime(tokenExpiredConstant.getRefreshTokenExpiredTime())
                .secretKey(accessSecretKey)
                .build());
    }

    public TokenDto generateToken(Member member) {

        String accessToken = createAccessToken(member);
        String refreshToken = createRefreshToken(member);

        refreshTokenService.save(RefreshTokenDto.builder()
                .userEmail(member.getEmail())
                .refreshToken(refreshToken)
                .timeToLive(tokenExpiredConstant.getRefreshTokenExpiredTime())
                .build());

        return new TokenDto(accessToken, refreshToken);
    }


    public TokenDto regenerateByRefreshToken(Member member, TokenDto tokenDto) {
        Claims accessTokenClaims = this.parseClaims(tokenDto.getAccessToken());

        if (!isTokenExpired(accessTokenClaims)) {
            return tokenDto;
        }
        refreshTokenService.validRefreshToken(tokenDto.getRefreshToken());
        RefreshToken refreshToken = refreshTokenService.findRefreshToken(member.getEmail());
        refreshTokenService.delete(refreshToken);

        return this.generateToken(member);
    }

    public void deleteRefreshToken(String email) {
        RefreshToken refreshToken = refreshTokenService.findRefreshToken(email);
        refreshTokenService.delete(refreshToken);
    }

    public String setToken(CreateTokenDto createTokenDto) {
        Claims claims = Jwts.claims().setSubject(createTokenDto.getEmail());
        claims.put(createTokenDto.getKeyRoles(), createTokenDto.getRoles());

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + createTokenDto.getTokenExpiredTime());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, createTokenDto.getSecretKey())
                .compact();
    }


    public void validateToken(String accessToken, String refreshToken) {
        if (!StringUtils.hasText(accessToken)) {
            throw new JwtException(MALFORMED_JWT_REQUEST);
        }
        Claims claims = this.parseClaims(accessToken);

        if (isTokenExpired(claims)) {
            if (!this.existRefreshToken(refreshToken)) {
                throw new JwtException(ACCESS_TOKEN_EXPIRED);
            }

            Claims refreshClaims = this.parseClaims(refreshToken);

            if (this.isTokenExpired(refreshClaims)) {
                throw new JwtException(ALL_TOKEN_EXPIRED);
            }
        }
    }

    private boolean existRefreshToken(String refreshToken) {
        return StringUtils.hasText(refreshToken);
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(this.accessSecretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException expiredJwtException) {
            return expiredJwtException.getClaims();
        } catch (MalformedJwtException malformedJwtException) {
            throw new MalformedJwtException(MALFORMED_JWT.getDescription());
        } catch (SignatureException signatureException) {
            throw new SignatureException(INVALID_SIGNATURE.getDescription());
        } catch (UnsupportedJwtException unsupportedJwtException) {
            throw new UnsupportedJwtException(INVALID_JWT_COMPONENT.getDescription());
        }
    }
}