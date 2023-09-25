package com.zerobase.foodlier.global.member.regiser.facade;

import com.zerobase.foodlier.common.redis.service.EmailVerificationService;
import com.zerobase.foodlier.common.s3.service.S3Service;
import com.zerobase.foodlier.module.member.member.dto.MemberInputDto;
import com.zerobase.foodlier.module.member.member.dto.MemberRegisterDto;
import com.zerobase.foodlier.module.member.member.local.dto.CoordinateResponseDto;
import com.zerobase.foodlier.module.member.member.local.service.LocalService;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import com.zerobase.foodlier.module.member.member.type.RegistrationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class MemberRegisterFacade {

    private final EmailVerificationService emailVerificationService;
    private final MemberService memberService;
    private final LocalService localService;
    private final S3Service s3Service;

    /**
     *  도메인 회원가입을 수행하는 메서드입니다.
     *
     *  이메일 검증 확인 -> 좌표 얻어오기 -> s3에 이미지 업로드 후 url가져오기 -> 회원가입
     *
     *  작성자 : 전현서
     *  작성일 : 2023-09-25
     */
    public void domainRegister(MemberInputDto memberInputDto){
        emailVerificationService.isAuthorized(memberInputDto.getEmail());

        CoordinateResponseDto coordinateResponseDto = localService.getCoordinate(
                memberInputDto.getRoadAddress()
        );
        String profileUrl = s3Service.getImageUrl(memberInputDto.getProfileImage());

        memberService.register(
                MemberRegisterDto.from(
                        memberInputDto, coordinateResponseDto, profileUrl,
                        RegistrationType.DOMAIN
                )
        );
    }

    public void socialRegister(){

    }

}
