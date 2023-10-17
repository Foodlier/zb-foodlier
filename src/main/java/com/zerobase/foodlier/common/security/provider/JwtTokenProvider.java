package com.zerobase.foodlier.common.security.provider;


import com.zerobase.foodlier.common.redis.dto.RefreshTokenDto;
import com.zerobase.foodlier.common.redis.service.RefreshTokenService;
import com.zerobase.foodlier.common.security.exception.JwtException;
import com.zerobase.foodlier.common.security.provider.constants.TokenExpiredConstant;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.zerobase.foodlier.common.security.constants.AuthorizationConstants.TOKEN_PREFIX;
import static com.zerobase.foodlier.common.security.exception.JwtErrorCode.*;

@Component
public class JwtTokenProvider {

    private static final String KEY_ROLES = "roles";
    private static final String TOKEN_TYPE = "type";
    private static final String ACCESS_TOKEN = "AT";
    private static final String REFRESH_TOKEN = "RT";
    private final TokenExpiredConstant tokenExpiredConstant;
    private final RefreshTokenService refreshTokenService;
    private final String accessSecretKey;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String accessSecretKey,
                            TokenExpiredConstant tokenExpiredConstant,
                            RefreshTokenService refreshTokenService) {
        this.accessSecretKey = accessSecretKey;
        this.refreshTokenService = refreshTokenService;
        this.tokenExpiredConstant = tokenExpiredConstant;
    }

    /**
     * 작성자: 이종욱
     * 작성일: 2023-09-26
     * 사용자 정보를 바탕으로 접근 토큰 생성
     */
    public String createAccessToken(MemberAuthDto memberAuthDto, Date date) {
        Claims claims = Jwts.claims().setSubject(memberAuthDto.getEmail());
        claims.setId(String.valueOf(memberAuthDto.getId()));
        claims.put(KEY_ROLES, memberAuthDto.getRoles());
        claims.put(TOKEN_TYPE, ACCESS_TOKEN);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(tokenExpiredConstant.getAccessTokenExpiredDate(date))
                .signWith(SignatureAlgorithm.HS512, accessSecretKey)
                .compact();
    }

    /**
     * 작성자: 이종욱
     * 작성일: 2023-09-26
     * 사용자 정보를 바탕으로 재발급 토큰 생성
     */
    public String createRefreshToken(MemberAuthDto memberAuthDto, Date date) {
        Claims claims = Jwts.claims().setSubject(memberAuthDto.getEmail());
        claims.setId(String.valueOf(memberAuthDto.getId()));
        claims.put(TOKEN_TYPE, REFRESH_TOKEN);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(tokenExpiredConstant.getRefreshTokenExpiredDate(date))
                .signWith(SignatureAlgorithm.HS512, accessSecretKey)
                .compact();
    }

    /**
     * 작성자: 이종욱
     * 작성일: 2023-09-26
     * 사용자 정보를 바탕으로 접근 토큰과 재발급 토큰 생성
     */
    public TokenDto createToken(MemberAuthDto memberAuthDto, Date date) {

        String accessToken = createAccessToken(memberAuthDto, date);
        String refreshToken = createRefreshToken(memberAuthDto, date);

        refreshTokenService.save(RefreshTokenDto.builder()
                .memberEmail(memberAuthDto.getEmail())
                .refreshToken(refreshToken)
                .timeToLive(tokenExpiredConstant.getRefreshTokenExpiredMinute())
                .build());

        return new TokenDto(accessToken, refreshToken);
    }

    /**
     * 작성자: 이종욱
     * 작성일: 2023-09-26
     * 접근 토큰 만료 시 재발급 토큰이 유효할 경우 새로운 접근 토큰 발급
     */
    public String reissue(String refreshToken, List<String> roles, Date date) {

        Claims refreshTokenClaims = this.parseClaims(refreshToken);
        String userEmail = refreshTokenClaims.getSubject();
        Long userId = Long.parseLong(refreshTokenClaims.getId());
        if (!refreshTokenService.isRefreshTokenExisted(userEmail)) {
            throw new JwtException(REFRESH_TOKEN_NOT_FOUND);
        }
        return this.createAccessToken(MemberAuthDto.builder()
                        .id(userId)
                        .email(userEmail)
                        .roles(roles)
                        .build(),
                date);
    }

    /**
     * 작성자: 이종욱
     * 작성일: 2023-09-26
     * 사용자의 재발급 토큰 삭제
     */
    public void deleteRefreshToken(String email) {
        refreshTokenService.delete(email);
    }

    /**
     * 작성자: 이종욱
     * 작성일: 2023-09-26
     * 사용자의 이메일, 식별자와 권한 정보를 토큰에서 추출
     */
    public Authentication getAuthentication(String token) {
        MemberAuthDto memberAuthDto = getMemberAuthDto(token);
        List<SimpleGrantedAuthority> grantedAuthorities =
                memberAuthDto.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(
                memberAuthDto, token, grantedAuthorities);
    }

    public String getEmail(String token){
        return this.parseClaims(token).getSubject();
    }


    /**
     * 작성자: 이종욱
     * 작성일: 2023-09-26
     * 사용자의 권한을 토큰으로부터 얻음
     */
    private List<String> getRoles(String token) {
        Claims claims = parseClaims(token);

        if(claims.get(TOKEN_TYPE).equals(REFRESH_TOKEN)) {
            return new ArrayList<>();
        }

        List<?> roles = claims.get(KEY_ROLES, List.class);

        return roles.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    /**
     * 작성자: 이종욱
     * 작성일: 2023-09-26
     * 사용자의 정보 객체를 토큰으로부터 생성
     */
    private MemberAuthDto getMemberAuthDto(String token) {
        Claims claims = parseClaims(token);
        List<String> roles = getRoles(token);

        return MemberAuthDto.builder()
                .id(Long.valueOf(claims.getId()))
                .email(claims.getSubject())
                .roles(roles)
                .build();
    }

    /**
     * 작성자: 이종욱
     * 작성일: 2023-09-26
     * 접근 토큰으로부터 사용자의 이메일을 얻어 재발급 토큰 저장 유무 판별
     */
    public boolean existRefreshToken(String accessToken) {

        Claims claims = this.parseClaims(accessToken);
        String memberEmail = claims.getSubject();

        return refreshTokenService.isRefreshTokenExisted(memberEmail);
    }

    /**
     * 작성자: 이종욱
     * 작성일: 2023-09-26
     * 전달받은 토큰의 만료 여부 판별
     */
    public boolean isTokenExpired(String token) {
        Claims claims = this.parseClaims(token);
        return claims.getExpiration().before(new Date());
    }

    /**
     * 작성자: 이종욱
     * 작성일: 2023-09-26
     * 전달받은 토큰을 비밀 키로 복호화하여 토큰 정보 추출
     */
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