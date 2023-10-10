package com.zerobase.foodlier.common.security.provider;

import com.zerobase.foodlier.common.redis.dto.RefreshTokenDto;
import com.zerobase.foodlier.common.redis.service.RefreshTokenService;
import com.zerobase.foodlier.common.security.exception.JwtErrorCode;
import com.zerobase.foodlier.common.security.exception.JwtException;
import com.zerobase.foodlier.common.security.provider.constants.TokenExpiredConstant;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.module.member.member.type.RoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class JwtTokenProviderTest {
    private static final int TRY_ONCE = 1;
    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private TokenExpiredConstant tokenExpiredConstant;

    private JwtTokenProvider jwtTokenProvider;

    private final String accessSecretKey = "secret_key"; // Access secret key for testing
    private final Long accessTokenExpiredTime = 36000000L;
    private final Long refreshTokenExpiredTime = 24 * 36000000L;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        jwtTokenProvider = new JwtTokenProvider(accessSecretKey,
                tokenExpiredConstant,
                refreshTokenService
        );
    }

    Claims parseClaims(String token) {
        return Jwts.parser().setSigningKey(accessSecretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    @Test
    @DisplayName("접근 토큰 발급 성공")
    void success_Create_AccessToken() {

        // given
        MemberAuthDto memberAuthDto = MemberAuthDto.builder()
                .id(1L)
                .email("test@gmail.com")
                .roles(List.of(RoleType.ROLE_USER.name()))
                .build();
        Date date = new Date();
        Date expiredDate = new Date(date.getTime() + accessTokenExpiredTime);
        when(tokenExpiredConstant.getAccessTokenExpiredDate(date))
                .thenReturn(expiredDate);

        // when
        String accessToken = jwtTokenProvider.createAccessToken(memberAuthDto, date);

        // then
        Claims claims = parseClaims(accessToken);
        Date actualExpiredDate = claims.get("exp", Date.class);
        LocalDateTime expectedExpired = transformFrom(expiredDate);
        LocalDateTime actualExpired = transformFrom(actualExpiredDate);
        assertAll(
                () -> assertEquals(claims.getSubject(), memberAuthDto.getEmail()),
                () -> assertEquals(Long.parseLong(claims.getId()), memberAuthDto.getId()),
                () -> assertEquals(claims.get("roles"), memberAuthDto.getRoles()),
                () -> assertEquals(expectedExpired, actualExpired)

        );
    }

    @Test
    @DisplayName("재발급 토큰 발급 성공")
    void success_Create_RefreshToken() {

        // given
        MemberAuthDto memberAuthDto = MemberAuthDto.builder()
                .id(1L)
                .email("test@gmail.com")
                .roles(List.of(RoleType.ROLE_USER.name()))
                .build();
        Date date = new Date();
        Date expectedExpiredDate = new Date(date.getTime() + refreshTokenExpiredTime);
        when(tokenExpiredConstant.getRefreshTokenExpiredDate(date))
                .thenReturn(expectedExpiredDate);

        // when
        String accessToken = jwtTokenProvider.createRefreshToken(memberAuthDto, date);

        // then
        Claims claims = parseClaims(accessToken);
        Date actualExpiredDate = claims.get("exp", Date.class);
        LocalDateTime expectedExpired = transformFrom(expectedExpiredDate);
        LocalDateTime actualExpired = transformFrom(actualExpiredDate);
        assertAll(
                () -> assertEquals(claims.getSubject(), memberAuthDto.getEmail()),
                () -> assertEquals(Long.parseLong(claims.getId()), memberAuthDto.getId()),
                () -> assertEquals(expectedExpired, actualExpired)

        );
    }

    @Test
    @DisplayName("접근 토큰과 재발급 토큰 발급 성공")
    void success_Create_Token_All() {

        // given
        MemberAuthDto memberAuthDto = MemberAuthDto.builder()
                .id(1L)
                .email("test@gmail.com")
                .roles(List.of(RoleType.ROLE_USER.name()))
                .build();
        Date date = new Date();
        Date accessTokenExpiredDate = new Date(date.getTime() + accessTokenExpiredTime);
        Date refreshTokenExpiredDate = new Date(date.getTime() + refreshTokenExpiredTime);

        when(tokenExpiredConstant.getAccessTokenExpiredDate(date))
                .thenReturn(accessTokenExpiredDate);
        when(tokenExpiredConstant.getRefreshTokenExpiredDate(date))
                .thenReturn(refreshTokenExpiredDate);
        when(tokenExpiredConstant.getRefreshTokenExpiredMinute())
                .thenReturn(refreshTokenExpiredTime);
        doNothing().when(refreshTokenService).save(any());
        // when
        ArgumentCaptor<RefreshTokenDto> captor = ArgumentCaptor.forClass(RefreshTokenDto.class);

        TokenDto tokenDto = jwtTokenProvider.createToken(memberAuthDto, date);

        // then
        verify(refreshTokenService, times(TRY_ONCE)).save(captor.capture());

        assertAll(
                () -> assertNotNull(tokenDto.getAccessToken()),
                () -> assertNotNull(tokenDto.getRefreshToken()),
                () -> assertEquals(captor.getValue().getMemberEmail(), memberAuthDto.getEmail()),
                () -> assertEquals(captor.getValue().getRefreshToken(), tokenDto.getRefreshToken()),
                () -> assertEquals(captor.getValue().getTimeToLive(), refreshTokenExpiredTime)
        );
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void success_Reissue_Token_All() {

        // given
        MemberAuthDto memberAuthDto = MemberAuthDto.builder()
                .id(1L)
                .email("test@gmail.com")
                .roles(List.of(RoleType.ROLE_USER.name()))
                .build();
        Date date = new Date();
        Date expectedAccessTokenExpiredDate = new Date(date.getTime() + accessTokenExpiredTime);

        when(tokenExpiredConstant.getAccessTokenExpiredDate(date))
                .thenReturn(date);
        String accessToken = jwtTokenProvider.createAccessToken(memberAuthDto, date);

        given(refreshTokenService.isRefreshTokenExisted(anyString()))
                .willReturn(true);

        when(tokenExpiredConstant.getAccessTokenExpiredDate(date))
                .thenReturn(expectedAccessTokenExpiredDate);

        // when

        String reissue = jwtTokenProvider.reissue(accessToken,
                memberAuthDto.getRoles().stream()
                .map(String::valueOf)
                        .collect(Collectors.toList()),
                date);

        // then
        Claims claims = parseClaims(reissue);
        Date actualExpiredDate = claims.get("exp", Date.class);
        LocalDateTime expectedExpired = transformFrom(expectedAccessTokenExpiredDate);

        LocalDateTime actualExpired = transformFrom(actualExpiredDate);
        assertAll(
                () -> assertEquals(claims.getSubject(), memberAuthDto.getEmail()),
                () -> assertEquals(Long.parseLong(claims.getId()), memberAuthDto.getId()),
                () -> assertEquals(expectedExpired, actualExpired)

        );
    }


    @Test
    @DisplayName("토큰 재발급 실패 - 재발급 토큰 만료")
    void fail_Reissue_Token_All() {

        // given
        MemberAuthDto memberAuthDto = MemberAuthDto.builder()
                .id(1L)
                .email("test@gmail.com")
                .roles(List.of(RoleType.ROLE_USER.name()))
                .build();
        Date date = new Date();

        when(tokenExpiredConstant.getAccessTokenExpiredDate(date))
                .thenReturn(date);
        String accessToken = jwtTokenProvider.createAccessToken(memberAuthDto, date);

        given(refreshTokenService.isRefreshTokenExisted(anyString()))
                .willReturn(false);

        // when
        JwtException jwtException = assertThrows(JwtException.class, () -> jwtTokenProvider.reissue(
                accessToken,
                memberAuthDto.getRoles().stream()
                .map(String::valueOf)
                .collect(Collectors.toList()),
                date));
        // then

        assertAll(
                () -> assertEquals(jwtException.getErrorCode(), JwtErrorCode.REFRESH_TOKEN_NOT_FOUND),
                () -> assertEquals(jwtException.getDescription(), JwtErrorCode.REFRESH_TOKEN_NOT_FOUND.getDescription())
        );
    }

    @Test
    @DisplayName("인증 정보 생성 성공")
    void success_create_authentication() {

        // given
        MemberAuthDto memberAuthDto = MemberAuthDto.builder()
                .id(1L)
                .email("test@gmail.com")
                .roles(List.of(RoleType.ROLE_USER.name()))
                .build();

        Date date = new Date();

        when(tokenExpiredConstant.getAccessTokenExpiredDate(date))
                .thenReturn(new Date(date.getTime() + accessTokenExpiredTime));
        String accessToken = jwtTokenProvider.createAccessToken(memberAuthDto, date);

        // when
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        // then
        Object principal = authentication.getPrincipal();
        MemberAuthDto actualMemberAuthDto = (MemberAuthDto) principal;
        assertAll(
                () -> assertEquals(memberAuthDto.getId(), actualMemberAuthDto.getId()),
                () -> assertEquals(memberAuthDto.getEmail(), actualMemberAuthDto.getEmail()),
                () -> assertEquals(memberAuthDto.getRoles(), actualMemberAuthDto.getRoles())
        );
    }

    @Test
    @DisplayName("재발급 토큰 삭제 성공")
    void success_delete_refresh_token(){

        // given
        MemberAuthDto memberAuthDto = MemberAuthDto.builder()
                .id(1L)
                .email("test@gmail.com")
                .roles(List.of(RoleType.ROLE_USER.name()))
                .build();

        // when

        jwtTokenProvider.deleteRefreshToken(memberAuthDto.getEmail());

        // then
        verify(refreshTokenService, times(TRY_ONCE)).delete(memberAuthDto.getEmail());

    }

    @Test
    @DisplayName("재발급 토큰 찾기 성공")
    void success_Find_RefreshToken(){

        // given
        MemberAuthDto memberAuthDto = MemberAuthDto.builder()
                .id(1L)
                .email("test@gmail.com")
                .roles(List.of(RoleType.ROLE_USER.name()))
                .build();
        Date date = new Date();

        when(tokenExpiredConstant.getAccessTokenExpiredDate(date))
                .thenReturn(new Date(date.getTime() + accessTokenExpiredTime));
        String accessToken = jwtTokenProvider.createAccessToken(memberAuthDto, date);

        when(refreshTokenService.isRefreshTokenExisted(memberAuthDto.getEmail()))
                .thenReturn(true);

        // when

        boolean findingResult = jwtTokenProvider.existRefreshToken(accessToken);

        // then
        assertTrue(findingResult);

    }

    @Test
    @DisplayName("재발급 토큰 찾기 실패 - 재발급 토큰 저장 시간 만료")
    void fail_Find_RefreshToken(){

        // given
        MemberAuthDto memberAuthDto = MemberAuthDto.builder()
                .id(1L)
                .email("test@gmail.com")
                .roles(List.of(RoleType.ROLE_USER.name()))
                .build();
        Date date = new Date();

        when(tokenExpiredConstant.getAccessTokenExpiredDate(date))
                .thenReturn(new Date(date.getTime() + accessTokenExpiredTime));
        String accessToken = jwtTokenProvider.createAccessToken(memberAuthDto, date);

        when(refreshTokenService.isRefreshTokenExisted(memberAuthDto.getEmail()))
                .thenReturn(false);

        // when

        boolean findingResult = jwtTokenProvider.existRefreshToken(accessToken);

        // then
        assertFalse(findingResult);

    }

    private LocalDateTime transformFrom(Date date){

           return date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .withNano(0);

    }
}