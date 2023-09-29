package com.zerobase.foodlier.global.refrigerator.controller;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.global.refrigerator.facade.RefrigeratorFacade;
import com.zerobase.foodlier.module.request.service.RequestService;
import com.zerobase.foodlier.module.requestform.domain.model.RequestForm;
import com.zerobase.foodlier.module.requestform.dto.RequestFormDetailDto;
import com.zerobase.foodlier.module.requestform.dto.RequestFormDto;
import com.zerobase.foodlier.module.requestform.dto.RequestFormResponseDto;
import com.zerobase.foodlier.module.requestform.service.RequestFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/refrigerator")
public class RefrigeratorController {
    private final RequestService requestService;
    private final RefrigeratorFacade refrigeratorFacade;
    private final RequestFormService requestFormService;

    @PatchMapping("/send?requestId={requestId}&chefMemberId={chefMemberId}")
    public ResponseEntity<String> sendRequest(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @RequestParam(name = "requestId") Long requestId,
            @RequestParam(name = "chefMemberId") Long chefMemberId
    ) {
        requestService.sendRequest(memberAuthDto, requestId, chefMemberId);
        return ResponseEntity.ok("요청서가 전송되었습니다.");
    }

    @PatchMapping("/cancel/{requestId}")
    public ResponseEntity<String> cancelRequest(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable(name = "requestId") Long requestId
    ) {
        requestService.cancelRequest(memberAuthDto, requestId);
        return ResponseEntity.ok("요청이 취소되었습니다.");
    }

    @PatchMapping("/approve/{requestId}")
    public ResponseEntity<String> approveRequest(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable(name = "requestId") Long requestId
    ) {
        refrigeratorFacade.validRequestAndCreateDmRoom(memberAuthDto, requestId);
        return ResponseEntity.ok("요청을 수락하였습니다.");
    }

    @PatchMapping("/reject/{requestId}")
    public ResponseEntity<String> rejectRequest(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable(name = "requestId") Long requestId
    ) {
        requestService.rejectRequest(memberAuthDto, requestId);
        return ResponseEntity.ok("요청을 거절하였습니다.");
    }

    @PostMapping
    public ResponseEntity<String> createRequestForm(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @RequestBody RequestFormDto requestFormDto
    ) {
        requestFormService.createRequestForm(memberAuthDto.getId(), requestFormDto);
        return ResponseEntity.ok("요청이 완료되었습니다.");
    }

    @GetMapping("/{pageIdx}/{pageSize}")
    public ResponseEntity<Page<RequestFormResponseDto>> getMyRequestForm(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable int pageIdx,
            @PathVariable int pageSize
    ) {
        return ResponseEntity.ok(requestFormService.getMyRequestForm(
                memberAuthDto.getId(), pageIdx, pageSize));
    }

    @GetMapping("/{requestFormId}")
    public ResponseEntity<RequestFormDetailDto> getRequestFormDetail(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable Long requestFormId
    ) {
        return ResponseEntity.ok(requestFormService.getRequestFormDetail(
                memberAuthDto.getId(), requestFormId));
    }

    @PutMapping("{requestFormId}")
    public ResponseEntity<String> updateRequestForm(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @RequestBody RequestFormDto requestFormDto,
            @PathVariable Long requestFormId
    ) {
        requestFormService.updateRequestForm(
                memberAuthDto.getId(), requestFormDto, requestFormId);
        return ResponseEntity.ok("요청서 변경을 완료했습니다.");
    }

    @DeleteMapping("{requestFormId}")
    public ResponseEntity<String> deleteRequestForm(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable Long requestFormId
    ) {
        requestFormService.deleteRequestForm(
                memberAuthDto.getId(), requestFormId
        );
        return ResponseEntity.ok("요청서 삭제를 완료하였습니다.");
    }
}
