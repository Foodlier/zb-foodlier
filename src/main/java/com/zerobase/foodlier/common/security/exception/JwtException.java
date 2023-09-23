package com.zerobase.foodlier.common.security.exception;


import com.zerobase.foodlier.common.exception.exception.BaseException;

public class JwtException extends BaseException {

    private final JwtErrorCode jwtErrorCode;
    private final String description;

    public JwtException(JwtErrorCode jwtErrorCode) {
        this.jwtErrorCode = jwtErrorCode;
        this.description = jwtErrorCode.getDescription();
    }

    @Override
    public Enum<?> getErrorCode() {
        return this.jwtErrorCode;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
