package com.zerobase.foodlier.global.profile.facade;

import com.zerobase.foodlier.common.s3.service.S3Service;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.local.dto.CoordinateResponseDto;
import com.zerobase.foodlier.module.member.member.local.service.LocalService;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberPrivateProfileForm;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberUpdateDto;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ProfileFacade {
    private final MemberService memberService;
    private final S3Service s3Service;
    private final LocalService localService;

    public void deleteProfileUrlAndGetAddressUpdateProfile(
            MemberAuthDto memberAuthDto,
            MemberPrivateProfileForm form
    ) {
        Member member = memberService.findById(memberAuthDto.getId());
        CoordinateResponseDto coordinateResponseDto = localService.getCoordinate(
                Objects.nonNull(form.getRoadAddress()) ? form.getRoadAddress() :
                        member.getAddress().getRoadAddress()
        );
        String imageUrl = Objects.nonNull(form.getProfileImage()) ?
                s3Service.getImageUrl(form.getProfileImage()) :
                member.getProfileUrl();
        if (!imageUrl.equals(member.getProfileUrl())
                && Objects.nonNull(member.getProfileUrl())) {
            s3Service.deleteImage(member.getProfileUrl());
        }
        memberService.updatePrivateProfile(
                MemberUpdateDto.from(
                        form, coordinateResponseDto, imageUrl
                ), member
        );
    }
}
