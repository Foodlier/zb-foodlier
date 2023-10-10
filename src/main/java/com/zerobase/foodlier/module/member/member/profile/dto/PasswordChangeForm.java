package com.zerobase.foodlier.module.member.member.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordChangeForm {
    private String currentPassword;
    private String newPassword;
}
