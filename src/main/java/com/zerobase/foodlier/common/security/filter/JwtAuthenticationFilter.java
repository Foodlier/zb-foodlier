package com.zerobase.foodlier.common.security.filter;

import com.zerobase.foodlier.common.security.exception.JwtException;
import com.zerobase.foodlier.common.security.provider.JwtTokenProvider;
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

    private final List<String> MAIN_PAGE_URLS = List.of("/recipe/detail/", "/recipe/search", "/recipe/main", "/recipe/default", "/recipe/recommended");
    private final JwtTokenProvider tokenProvider;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        if (uri.contains("reissue")) {
            String refreshToken = this.resolveTokenFromRequest(request, REFRESH_HEADER);
            if (!StringUtils.hasText(refreshToken)) {
                throw new JwtException(EMPTY_TOKEN);
            }
            setSecurityContext(refreshToken);
        } else if (isMainPageUrl(uri)) {
            String accessToken = this.resolveTokenFromRequestWhenPublic(request);
            setSecurityContext(accessToken);
        } else {
            String accessToken = this.resolveTokenFromRequest(request, TOKEN_HEADER);

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

    private String resolveTokenFromRequest(@NonNull HttpServletRequest request,
                                           String header) {
        String token = request.getHeader(header);

        if (ObjectUtils.isEmpty(token)) {
            throw new JwtException(EMPTY_TOKEN);
        }

        if (!token.startsWith(TOKEN_PREFIX)) {
            throw new JwtException(MALFORMED_JWT_REQUEST);
        }

        return token.substring(TOKEN_PREFIX.length());
    }

    private String resolveTokenFromRequestWhenPublic(@NonNull HttpServletRequest request)
    {
        String token = request.getHeader(TOKEN_HEADER);

        if (!StringUtils.hasText(token)) {
            return this.tokenProvider.createVisitorToken(new Date());
        }

        if (ObjectUtils.isEmpty(token)) {
            throw new JwtException(EMPTY_TOKEN);
        }

        if (!token.startsWith(TOKEN_PREFIX)) {
            throw new JwtException(MALFORMED_JWT_REQUEST);
        }

        return token.substring(TOKEN_PREFIX.length());
    }

    private void setSecurityContext(String accessToken) {
        SecurityContextHolder
                .getContext()
                .setAuthentication(this.tokenProvider
                        .getAuthentication(accessToken));
    }

    private boolean isMainPageUrl(String uri) {

        for (String mainPageUrl : MAIN_PAGE_URLS) {
            if (uri.startsWith(mainPageUrl)) {
                return true;
            }
        }
        return false;
    }
}