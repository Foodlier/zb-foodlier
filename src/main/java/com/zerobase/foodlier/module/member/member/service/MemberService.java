package com.zerobase.foodlier.module.member.member.service;

import com.zerobase.foodlier.global.auth.dto.SignInForm;
import com.zerobase.foodlier.global.auth.dto.SignInResponse;
import com.zerobase.foodlier.global.profile.dto.MemberPrivateProfileForm;
import com.zerobase.foodlier.global.profile.dto.MemberPrivateProfileResponse;

public interface MemberService {
    SignInResponse signIn(SignInForm form);

    MemberPrivateProfileResponse getPrivateProfile(Long memberId);

    void updatePrivateProfile(Long memberId, MemberPrivateProfileForm form);
}
