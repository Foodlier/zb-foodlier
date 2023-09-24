package com.zerobase.foodlier.common.security.provider.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberVo {
    private Long id;
    private String email;
    private List<String> roles;
}
