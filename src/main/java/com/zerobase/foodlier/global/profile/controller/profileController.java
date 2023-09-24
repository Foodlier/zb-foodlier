package com.zerobase.foodlier.global.profile.controller;

import com.zerobase.foodlier.common.security.provider.vo.MemberVo;
import com.zerobase.foodlier.global.member.profile.facade.MemberProfileFacade;
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
    private final MemberProfileFacade memberProfileFacade;

    @GetMapping("/private")
    public ResponseEntity<MemberPrivateProfileResponse> getPrivateProfile(
            @AuthenticationPrincipal MemberVo memberVo
    ) {
        return ResponseEntity.ok(memberService.getPrivateProfile(memberVo.getEmail()));
    }

    @PutMapping("/private")
    public ResponseEntity<String> updatePrivateProfile(
            @AuthenticationPrincipal MemberVo memberVo,
            @RequestBody MemberPrivateProfileForm form,
            @RequestBody MultipartFile multipartFile
    ) {
        memberProfileFacade.deleteProfileUrlAndUpdateProfile(multipartFile,
                memberVo.getEmail(), form);
        return ResponseEntity.ok("내 정보 수정 완료");
    }
}
