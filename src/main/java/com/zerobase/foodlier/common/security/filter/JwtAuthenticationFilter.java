package com.zerobase.foodlier.common.security.filter;

import com.zerobase.foodlier.common.security.exception.JwtException;
import com.zerobase.foodlier.common.security.provider.JwtProvider;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.zerobase.foodlier.common.security.constants.AuthorizationConstants.*;
import static com.zerobase.foodlier.common.security.exception.JwtErrorCode.*;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> MAIN_PAGE_URLS = List.of("/recipe/detail", "/recipe/search",
            "/recipe/main", "/recipe/default", "/recipe/recommended", "/profile/public/topChef");
    private static final String REISSUE_URL = "/reissue";
    private final JwtProvider tokenProvider;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String authorization;
        if (uri.contains(REISSUE_URL)) {
            authorization = this.getTokenFromRequestBy(request, REFRESH_HEADER);
            String refreshToken = this.resolveToken(authorization);
            if (!StringUtils.hasText(refreshToken)) {
                throw new JwtException(EMPTY_TOKEN);
            }
            setSecurityContext(refreshToken);
        } else if (isMainPageUrl(uri)) {
            authorization = this.getTokenFromRequestBy(request, ACCESS_HEADER);
            String accessToken = this.resolveTokenWhenNonLogin(authorization);
            setSecurityContext(accessToken);
        } else {
            authorization = this.getTokenFromRequestBy(request, ACCESS_HEADER);
            String accessToken = this.resolveToken(authorization);

            if (this.tokenProvider.isTokenExpired(accessToken)
                    && !this.tokenProvider.existRefreshToken(accessToken)) {
                throw new JwtException(ALL_TOKEN_EXPIRED);
            }

            if (this.tokenProvider.isTokenExpired(accessToken)) {
                throw new JwtException(ACCESS_TOKEN_EXPIRED);
            }
            setSecurityContext(accessToken);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(String token) {
        validateEmptyToken(token);
        validateValidToken(token);
        return getTokenWithoutPrefix(token);
    }

    private String resolveTokenWhenNonLogin(String token) {

        if (!StringUtils.hasText(token)) {
            return this.tokenProvider.createVisitorToken(new Date());
        }

        validateEmptyToken(token);
        validateValidToken(token);

        return getTokenWithoutPrefix(token);
    }

    private String getTokenFromRequestBy(@NonNull HttpServletRequest request, String headerKey) {
        return request.getHeader(headerKey);
    }

    private String getTokenWithoutPrefix(String authorization) {
        return authorization.substring(TOKEN_PREFIX.length());
    }

    private void validateEmptyToken(String token) {
        if (ObjectUtils.isEmpty(token)) {
            throw new JwtException(EMPTY_TOKEN);
        }
    }

    private void validateValidToken(String token) {
        if (!token.startsWith(TOKEN_PREFIX)) {
            throw new JwtException(MALFORMED_JWT_REQUEST);
        }
    }

    private void setSecurityContext(String accessToken) {
        SecurityContextHolder
                .getContext()
                .setAuthentication(this.tokenProvider.getAuthentication(accessToken));
    }

    private boolean isMainPageUrl(String uri) {
        for (String mainPageUrl : MAIN_PAGE_URLS) {
            if (uri.contains(mainPageUrl)) {
                return true;
            }
        }
        return false;
    }
}