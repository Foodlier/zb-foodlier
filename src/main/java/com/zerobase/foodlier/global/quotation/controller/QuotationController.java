package com.zerobase.foodlier.global.quotation.controller;

import com.zerobase.foodlier.module.notification.domain.type.ActionType;
import com.zerobase.foodlier.module.notification.domain.type.PerformerType;
import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.global.notification.facade.NotificationFacade;
import com.zerobase.foodlier.global.quotation.facade.QuotationFacade;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;
import com.zerobase.foodlier.module.notification.dto.notify.NotifyInfoDto;
import com.zerobase.foodlier.module.notification.dto.notify.Impl.RequesterNotify;
import com.zerobase.foodlier.module.recipe.dto.quotation.QuotationDetailResponse;
import com.zerobase.foodlier.module.recipe.dto.quotation.QuotationDtoRequest;
import com.zerobase.foodlier.module.recipe.dto.quotation.QuotationTopResponse;
import com.zerobase.foodlier.module.recipe.dto.recipe.RecipeDtoRequest;
import com.zerobase.foodlier.module.recipe.service.quotation.QuotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/quotation")
@RequiredArgsConstructor
public class QuotationController {

    private final QuotationService quotationService;
    private final QuotationFacade quotationFacade;
    private final NotificationFacade notificationFacade;

    @PostMapping
    public ResponseEntity<String> createQuotation(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @RequestBody @Valid QuotationDtoRequest request
    ) {
        quotationService.createQuotation(memberAuthDto.getId(), request);
        return ResponseEntity.ok("견적서가 작성되었습니다.");
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendQuotation(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @RequestParam Long quotationId,
            @RequestParam Long requestId
    ) {
        RequesterNotify requesterNotify = RequesterNotify.from(
                quotationFacade.sendQuotation(memberAuthDto.getId(), quotationId, requestId),
                NotifyInfoDto.builder()
                        .performerType(PerformerType.CHEF)
                        .actionType(ActionType.SEND_QUOTATION)
                        .notificationType(NotificationType.REQUEST)
                        .build()
        );
        notificationFacade.send(requesterNotify);
        return ResponseEntity.ok(
                "견적서를 보냈습니다."
        );
    }

    @GetMapping("/{pageIdx}/{pageSize}")
    public ResponseEntity<ListResponse<QuotationTopResponse>> getQuotationListForRefrigerator(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable int pageIdx,
            @PathVariable int pageSize
    ) {
        return ResponseEntity.ok(quotationService
                .getQuotationListForRefrigerator(memberAuthDto.getId(),
                        PageRequest.of(pageIdx, pageSize)));
    }

    @GetMapping("/recipe/{pageIdx}/{pageSize}")
    public ResponseEntity<ListResponse<QuotationTopResponse>> getQuotationListForRecipe(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable int pageIdx,
            @PathVariable int pageSize
    ) {
        return ResponseEntity.ok(quotationService
                .getQuotationListForRecipe(memberAuthDto.getId(),
                        PageRequest.of(pageIdx, pageSize)));
    }

    @GetMapping("/{quotationId}")
    public ResponseEntity<QuotationDetailResponse> getQuotationDetail(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable Long quotationId
    ) {
        return ResponseEntity.ok(
                quotationService.getQuotationDetail(memberAuthDto.getId(), quotationId)
        );
    }

    @PutMapping("/{quotationId}")
    public ResponseEntity<String> updateQuotation(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable Long quotationId,
            @RequestBody @Valid QuotationDtoRequest request
    ) {
        quotationService.updateQuotation(memberAuthDto.getId(), quotationId, request);
        return ResponseEntity.ok(
                "견적서가 수정되었습니다."
        );
    }

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-10-02
     * 결제가 성사된 견적서를 Recipe로 변환함
     */
    @PutMapping("/recipify/{quotationId}")
    public ResponseEntity<String> recipifyQuotation(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable Long quotationId,
            @RequestBody @Valid RecipeDtoRequest request
    ) {
        quotationService.convertToRecipe(memberAuthDto.getId(), quotationId, request);
        return ResponseEntity.ok(
                "견적서가 꿀조합으로 변환되었습니다."
        );
    }

    @DeleteMapping("/{quotationId}")
    public ResponseEntity<String> deleteQuotation(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable Long quotationId
    ) {
        quotationService.deleteQuotation(memberAuthDto.getId(), quotationId);
        return ResponseEntity.ok(
                "견적서가 삭제되었습니다."
        );
    }


}
