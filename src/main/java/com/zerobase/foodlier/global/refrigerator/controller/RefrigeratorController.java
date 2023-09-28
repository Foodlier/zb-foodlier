package com.zerobase.foodlier.global.refrigerator.controller;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.global.refrigerator.facade.RefrigeratorFacade;
import com.zerobase.foodlier.module.request.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/refrigerator")
public class RefrigeratorController {
    private final RequestService requestService;
    private final RefrigeratorFacade refrigeratorFacade;

    @PatchMapping("/send")
    public ResponseEntity<String> sendRequest(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @RequestParam Long requestFormId,
            @RequestParam Long chefMemberId
    ) {
        requestService.sendRequest(memberAuthDto.getId(), requestFormId, chefMemberId);
        return ResponseEntity.ok("요청서가 전송되었습니다.");
    }

    @PatchMapping("/cancel/{requestId}")
    public ResponseEntity<String> cancelRequest(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable(name = "requestId") Long requestId
    ) {
        requestService.cancelRequest(memberAuthDto.getId(), requestId);
        return ResponseEntity.ok("요청이 취소되었습니다.");
    }

    @PostMapping("/requester/approve/{requestId}")
    public ResponseEntity<String> requesterApproveRequest(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable(name = "requestId") Long requestId
    ) {
        refrigeratorFacade.requesterApproveAndCreateDm(memberAuthDto.getId(), requestId);
        return ResponseEntity.ok("요청을 수락하였습니다.");
    }

    @PostMapping("/chef/approve/{requestId}")
    public ResponseEntity<String> chefApproveRequest(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable(name = "requestId") Long requestId
    ) {
        refrigeratorFacade.chefApproveAndCreateDm(memberAuthDto.getId(), requestId);
        return ResponseEntity.ok("요청을 수락하였습니다.");
    }

    @PatchMapping("/reject/{requestId}")
    public ResponseEntity<String> rejectRequest(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable(name = "requestId") Long requestId
    ) {
        requestService.rejectRequest(memberAuthDto.getId(), requestId);
        return ResponseEntity.ok("요청을 거절하였습니다.");
    }
}
