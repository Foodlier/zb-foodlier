package com.zerobase.foodlier.module.member.member.service;

import com.zerobase.foodlier.module.member.member.dto.MemberRegisterDto;

import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.module.member.member.dto.SignInForm;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberPrivateProfileResponse;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberUpdateDto;

public interface MemberService {
    void register(MemberRegisterDto memberRegisterDto);
    TokenDto signIn(SignInForm form);

    void signOut(String email);

    MemberPrivateProfileResponse getPrivateProfile(String email);

    void updatePrivateProfile(MemberUpdateDto memberUpdateDto,Member member);

    Member findByEmail(String email);
}
