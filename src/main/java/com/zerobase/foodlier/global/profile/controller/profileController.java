package com.zerobase.foodlier.global.profile.controller;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.global.profile.facade.ProfileFacade;
import com.zerobase.foodlier.global.profile.dto.MemberPrivateProfileForm;
import com.zerobase.foodlier.global.profile.dto.MemberPrivateProfileResponse;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class profileController {
    private final MemberService memberService;
    private final ProfileFacade profileFacade;

    @GetMapping("/private")
    public ResponseEntity<MemberPrivateProfileResponse> getPrivateProfile(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto
    ) {
        return ResponseEntity.ok(memberService.getPrivateProfile(memberAuthDto.getEmail()));
    }

    @PutMapping("/private")
    public ResponseEntity<String> updatePrivateProfile(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @RequestBody MemberPrivateProfileForm form,
            @RequestPart MultipartFile multipartFile
    ) {
        profileFacade.deleteProfileUrlAndUpdateProfile(multipartFile,
                memberAuthDto.getEmail(), form);
        return ResponseEntity.ok("내 정보 수정 완료");
    }
}
