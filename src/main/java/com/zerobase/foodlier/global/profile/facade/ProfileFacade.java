package com.zerobase.foodlier.global.profile.facade;

import com.zerobase.foodlier.common.s3.service.S3Service;
import com.zerobase.foodlier.global.profile.dto.MemberPrivateProfileForm;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ProfileFacade {
    private final MemberService memberService;
    private final S3Service s3Service;

    public void deleteProfileUrlAndUpdateProfile(MultipartFile multipartFile, String email, MemberPrivateProfileForm form) {
        String imageUrl = s3Service.getImageUrl(multipartFile);
        Member member = memberService.findByEmail(email);
        s3Service.deleteImage(member.getProfileUrl());
        memberService.updatePrivateProfile(email, form, imageUrl);
    }
}
