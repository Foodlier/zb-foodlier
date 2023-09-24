package com.zerobase.foodlier.common.security.provider;


import com.zerobase.foodlier.common.redis.dto.RefreshTokenDto;
import com.zerobase.foodlier.common.redis.service.RefreshTokenService;
import com.zerobase.foodlier.common.security.exception.JwtException;
import com.zerobase.foodlier.common.security.provider.constants.TokenExpiredConstant;
import com.zerobase.foodlier.common.security.provider.dto.CreateTokenDto;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    public String createAccessToken(MemberAuthDto memberAuthDto) {
        return setToken(CreateTokenDto.builder()
                .id(memberAuthDto.getId())
                .email(memberAuthDto.getEmail())
                .roles(memberAuthDto.getRoles())
                .keyRoles(KEY_ROLES)
                .tokenExpiredTime(tokenExpiredConstant.getAccessTokenExpiredTime())
                .secretKey(accessSecretKey)
                .build());
    }

    public String createRefreshToken(MemberAuthDto memberAuthDto) {
        return setToken(CreateTokenDto.builder()
                .id(memberAuthDto.getId())
                .email(memberAuthDto.getEmail())
                .tokenExpiredTime(tokenExpiredConstant.getRefreshTokenExpiredTime())
                .secretKey(accessSecretKey)
                .build());
    }

    public TokenDto createToken(MemberAuthDto memberAuthDto) {

        String accessToken = createAccessToken(memberAuthDto);
        String refreshToken = createRefreshToken(memberAuthDto);

        refreshTokenService.save(RefreshTokenDto.builder()
                .userEmail(memberAuthDto.getEmail())
                .refreshToken(refreshToken)
                .timeToLive(tokenExpiredConstant.getRefreshTokenExpiredTime())
                .build());

        return new TokenDto(accessToken, refreshToken);
    }

    public String reissue(String accessToken) {
        Claims accessTokenClaims = this.parseClaims(accessToken);
        String userEmail = accessTokenClaims.getSubject();
        Long userId = Long.parseLong(accessTokenClaims.getId());
        List<String> roles = getRoles(accessToken);
        if (!refreshTokenService.isRefreshTokenExisted(userEmail)) {
            throw new JwtException(REFRESH_TOKEN_NOT_FOUND);
        }
        return this.createAccessToken(MemberAuthDto.builder()
                                                    .id(userId)
                                                    .email(userEmail)
                                                    .roles(roles)
                                                    .build());
    }

    private List<String> getRoles(String token) {
        Claims claims = parseClaims(token);
        List<?> list = claims.get(KEY_ROLES, List.class);
        return list.stream().map(String::valueOf).collect(Collectors.toList());
    }

    public void deleteRefreshToken(String email) {
        refreshTokenService.delete(email);
    }

    public String setToken(CreateTokenDto createTokenDto) {
        Claims claims = Jwts.claims().setSubject(createTokenDto.getEmail());
        claims.setId(String.valueOf(createTokenDto.getId()));
        if (StringUtils.hasText(createTokenDto.getKeyRoles())) {
            claims.put(createTokenDto.getKeyRoles(), createTokenDto.getRoles());
        }

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

    public Authentication getAuthentication(String token) {
        List<SimpleGrantedAuthority> grantedAuthorities = getRoles(token).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        MemberVo memberVo = getMemberVo(token);

        return new UsernamePasswordAuthenticationToken(
                memberVo, token, grantedAuthorities);
    }

    private List<String> getRoles(String token) {
        Claims claims = parseClaims(token);
        List<?> list = claims.get(KEY_ROLES, List.class);

        return list.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    private MemberVo getMemberVo(String token) {
        Claims claims = parseClaims(token);
        List<String> roles = getRoles(token);

        return MemberVo.builder()
                .id(Long.valueOf(claims.getId()))
                .email(claims.getSubject())
                .roles(roles)
                .build();
    }

    private boolean existRefreshToken(String refreshToken) {
        return StringUtils.hasText(refreshToken);
    }

    private boolean isTokenExpired(Claims claims) {
    public Authentication getAuthentication(String token) {

        Claims claims = this.parseClaims(token);
        String userEmail = claims.getSubject();
        Long id = Long.parseLong(claims.getId());
        List<String> roles = this.getRoles(token);

        return new UsernamePasswordAuthenticationToken();

    }

    public boolean existRefreshToken(String accessToken) {

        Claims claims = this.parseClaims(accessToken);
        String userEmail = claims.getSubject();

        return refreshTokenService.isRefreshTokenExisted(userEmail);
    }

    public boolean isTokenExpired(String token) {
        Claims claims = this.parseClaims(token);
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