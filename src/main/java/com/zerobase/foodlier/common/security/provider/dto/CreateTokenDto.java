package com.zerobase.foodlier.common.security.provider.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTokenDto {

    private Long id;
    private String email;
    private List<String> roles;
    private String keyRoles;
    private long tokenExpiredTime;
    private String secretKey;
}
