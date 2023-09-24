package com.zerobase.foodlier.common.security.provider.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final String secretKey = "secretKey";
    private final long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24;
    private final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 12;

    public String createAccessToken(String email, Long id) {
        Claims claims = Jwts.claims()
                .setSubject(email)
                .setId(id.toString());
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String createRefreshToken(String email, Long id) {
        Claims claims = Jwts.claims()
                .setSubject(email)
                .setId(id.toString());
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
}
