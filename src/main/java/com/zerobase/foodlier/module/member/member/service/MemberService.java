package com.zerobase.foodlier.module.member.member.service;

import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.global.auth.dto.SignInForm;
import com.zerobase.foodlier.global.profile.dto.MemberPrivateProfileForm;
import com.zerobase.foodlier.global.profile.dto.MemberPrivateProfileResponse;
import com.zerobase.foodlier.module.member.member.domain.model.Member;

public interface MemberService {
    TokenDto signIn(SignInForm form);

    void signOut(String email);

    MemberPrivateProfileResponse getPrivateProfile(String email);

    void updatePrivateProfile(String email, MemberPrivateProfileForm form);

    Member findByEmail(String email);
}
