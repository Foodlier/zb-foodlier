package com.zerobase.foodlier.module.member.member.service;

import com.zerobase.foodlier.common.security.provider.JwtTokenProvider;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.common.security.provider.dto.TokenDto;
import com.zerobase.foodlier.global.auth.dto.SignInForm;
import com.zerobase.foodlier.global.profile.dto.MemberPrivateProfileForm;
import com.zerobase.foodlier.global.profile.dto.MemberPrivateProfileResponse;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    /**
     * 작성자 : 이승현
     * 작성일 :
     * 이메일과 비밀번호를 받아와서 access token과 refresh token값을 반환해줍니다.
     */
    @Override
    public TokenDto signIn(SignInForm form) {
        Member member = memberRepository.findByEmail(form.getEmail()).stream()
//                .filter(m-> passwordEncoder.matches(form.getPassword(), m.getPassword()))
                .filter(m -> form.getPassword().equals(m.getPassword()))
                .findFirst()
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        return tokenProvider.createToken(MemberAuthDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .roles(member.getRoles())
                .build());
    }

    /**
     * 작성자 : 이승현
     * 작성일 :
     * access token의 정보를 통해 redis 서버에 refresh token이 있다면 삭제해줍니다.
     */
    @Override
    public void signOut(String email) {
        tokenProvider.deleteRefreshToken(email);
    }

    /**
     * 작성자 : 이승현
     * 작성일 :
     * 유저 개인 정보를 가져옵니다.
     */
    @Override
    public MemberPrivateProfileResponse getPrivateProfile(String email) {
        Member member = findByEmail(email);

        return MemberPrivateProfileResponse.builder()
                .nickName(member.getNickname())
                .email(member.getEmail())
                .address(member.getAddress())
                .phoneNumber(member.getPhoneNumber())
                .profileUrl(member.getProfileUrl())
                .build();
    }

    /**
     * 작성자 : 이승현
     * 작성일 :
     * 프로필 정보를 수정합니다.
     */
    @Override
    public void updatePrivateProfile(String email, MemberPrivateProfileForm form, String imageUrl) {
        Member member = findByEmail(email);

        member.setNickname(form.getNickName());
        member.setPhoneNumber(form.getPhoneNumber());
        member.setAddress(form.getAddress());
        member.setProfileUrl(imageUrl);

        memberRepository.save(member);
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }
}
