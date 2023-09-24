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

import static com.zerobase.foodlier.common.security.constants.AuthorizationConstants.*;
import static com.zerobase.foodlier.common.security.exception.JwtErrorCode.*;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    

    private final JwtTokenProvider tokenProvider;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {


        String accessToken = this.resolveTokenFromRequest(request);

        if (this.tokenProvider.isTokenExpired(accessToken) && !this.tokenProvider.existRefreshToken(accessToken)) {
            throw new JwtException(ALL_TOKEN_EXPIRED);
        }

        if (this.tokenProvider.isTokenExpired(accessToken) && this.tokenProvider.existRefreshToken(accessToken)) {
            String reissuedAccessToken = this.tokenProvider.reissue(accessToken);
            response.setHeader(TOKEN_HEADER, reissuedAccessToken);
            throw new JwtException(REFRESH_TOKEN_NOT_FOUND);
        }

        if (!this.tokenProvider.isTokenExpired(accessToken)) {
            setSecurityContext(accessToken);
        }
    }

    private String resolveTokenFromRequest(@NonNull HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);

        if (ObjectUtils.isEmpty(token)) {
            throw new JwtException(EMPTY_TOKEN);
        }

        if (!token.startsWith(TOKEN_PREFIX)) {
            throw new JwtException(MALFORMED_JWT_REQUEST);
        }

        return token.substring(TOKEN_PREFIX.length());
    }

    private void setSecurityContext(String accessToken) {
        SecurityContextHolder.getContext().setAuthentication(this.tokenProvider.getAuthentication(accessToken));
    }
}
