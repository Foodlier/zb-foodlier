package com.zerobase.foodlier.common.security.filter;

import com.zerobase.foodlier.common.security.exception.JwtException;
import com.zerobase.foodlier.common.security.provider.JwtTokenProvider;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.zerobase.foodlier.common.security.constants.AuthorizationConstants.*;
import static com.zerobase.foodlier.common.security.exception.JwtErrorCode.EMPTY_TOKEN;
import static com.zerobase.foodlier.common.security.exception.JwtErrorCode.MALFORMED_JWT_REQUEST;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String EMPTY_REFRESH_TOKEN = "";

    private final JwtTokenProvider tokenProvider;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {



        String accessToken = this.resolveTokenFromRequest(request, TOKEN_HEADER);
        String refreshToken = this.extractRefreshToken(request);
        this.tokenProvider.validateToken(accessToken, refreshToken);

        filterChain.doFilter(request, response);
    }

    private String resolveTokenFromRequest(@NonNull HttpServletRequest request, String tokenHeader) {
        String token = request.getHeader(tokenHeader);

        if (ObjectUtils.isEmpty(token)) {
            throw new JwtException(EMPTY_TOKEN);
        }

        if (!token.startsWith(TOKEN_PREFIX)) {
            throw new JwtException(MALFORMED_JWT_REQUEST);
        }

        return token.substring(TOKEN_PREFIX.length());
    }

    public String extractRefreshToken(HttpServletRequest request){
        if (StringUtils.hasText(request.getHeader(REFRESH_HEADER))) {
            return this.resolveTokenFromRequest(request, REFRESH_HEADER);
        }
        return EMPTY_REFRESH_TOKEN;
    }

}
