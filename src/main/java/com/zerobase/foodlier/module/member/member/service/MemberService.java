package com.zerobase.foodlier.module.member.member.service;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.dto.*;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberPrivateProfileResponse;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberUpdateDto;
import com.zerobase.foodlier.module.member.member.profile.dto.PasswordChangeForm;
import com.zerobase.foodlier.module.member.member.social.dto.OAuthInfoResponse;
import com.zerobase.foodlier.module.member.member.type.RequestedOrderingType;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface MemberService {
    void register(MemberRegisterDto memberRegisterDto);

    TokenDto signIn(SignInForm form, Date nowDate);

    void signOut(String email);

    MemberPrivateProfileResponse getPrivateProfile(MemberAuthDto memberAuthDto);

    void updatePrivateProfile(MemberUpdateDto memberUpdateDto, Member member);

    ListResponse<RequestedMemberDto> getRequestedMemberList(Long memberId,
                                                            RequestedOrderingType type,
                                                            Pageable pageable);

    String updatePassword(MemberAuthDto memberAuthDto,
                          PasswordChangeForm form);

    String updateRandomPassword(PasswordFindForm form, String newPassword);

    String withdraw(MemberAuthDto memberAuthDto);

    DefaultProfileDtoResponse getDefaultProfile(Long memberId);

    Member findById(Long id);

    Member findByEmail(String email);

    String reissue(String refreshToken);

    Member findOrCreateMember(OAuthInfoResponse oAuthInfoResponse);

    void checkNickname(String nickname);

    void checkPhoneNumber(String phoneNumber);

    void checkEmail(String email);
}
