package com.zerobase.foodlier.module.member.member.service;

import com.zerobase.foodlier.module.member.member.dto.DefaultProfileDtoResponse;
import com.zerobase.foodlier.module.member.member.dto.MemberRegisterDto;

import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.module.member.member.dto.RequestedMemberDto;
import com.zerobase.foodlier.module.member.member.dto.SignInForm;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberPrivateProfileResponse;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberUpdateDto;
import com.zerobase.foodlier.module.member.member.type.RequestedOrderingType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {
    void register(MemberRegisterDto memberRegisterDto);
    TokenDto signIn(SignInForm form);

    void signOut(String email);

    MemberPrivateProfileResponse getPrivateProfile(String email);

    void updatePrivateProfile(MemberUpdateDto memberUpdateDto,Member member);
    List<RequestedMemberDto> getRequestedMemberList(Long memberId,
                                                    RequestedOrderingType type,
                                                    Pageable pageable);
    DefaultProfileDtoResponse getDefaultProfile(Long memberId);
    Member findByEmail(String email);
}
