package com.zerobase.foodlier.global.profile.facade;

import com.zerobase.foodlier.common.s3.service.S3Service;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberPrivateProfileForm;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.local.dto.CoordinateResponseDto;
import com.zerobase.foodlier.module.member.member.local.service.LocalService;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberUpdateDto;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileFacade {
    private final MemberService memberService;
    private final S3Service s3Service;
    private final LocalService localService;

    public void deleteProfileUrlAndGetAddressUpdateProfile(
            String email,
            MemberPrivateProfileForm form
    ) {
        Member member = memberService.findByEmail(email);
        CoordinateResponseDto coordinateResponseDto = localService.getCoordinate(
                form.getRoadAddress() != null ? form.getRoadAddress() :
                        member.getAddress().getRoadAddress()
        );
        String imageUrl = form.getProfileImage() != null ?
                s3Service.getImageUrl(form.getProfileImage()) :
                member.getProfileUrl();
        s3Service.deleteImage(member.getProfileUrl());
        memberService.updatePrivateProfile(
                MemberUpdateDto.from(
                        form, coordinateResponseDto, imageUrl
                ), member
        );
    }
}
