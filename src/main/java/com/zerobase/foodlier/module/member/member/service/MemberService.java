package com.zerobase.foodlier.module.member.member.service;

import com.zerobase.foodlier.module.member.member.dto.DefaultProfileDtoResponse;
import com.zerobase.foodlier.module.member.member.dto.MemberRegisterDto;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.dto.PasswordFindForm;
import com.zerobase.foodlier.module.member.member.dto.RequestedMemberDto;
import com.zerobase.foodlier.module.member.member.dto.SignInForm;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberPrivateProfileResponse;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberUpdateDto;
import com.zerobase.foodlier.module.member.member.profile.dto.PasswordChangeForm;
import com.zerobase.foodlier.module.member.member.type.RequestedOrderingType;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    void register(MemberRegisterDto memberRegisterDto);

    TokenDto signIn(SignInForm form);

    void signOut(String email);

    MemberPrivateProfileResponse getPrivateProfile(String email);

    void updatePrivateProfile(MemberUpdateDto memberUpdateDto,Member member);
    ListResponse<RequestedMemberDto> getRequestedMemberList(Long memberId,
                                                            RequestedOrderingType type,
                                                            Pageable pageable);

    String updatePassword(MemberAuthDto memberAuthDto,
                          PasswordChangeForm form);

    String updateRandomPassword(PasswordFindForm form, String newPassword);

    String withdraw(MemberAuthDto memberAuthDto);

    DefaultProfileDtoResponse getDefaultProfile(Long memberId);
    Member findByEmail(String email);

    String reissue(String refreshToken);
}
