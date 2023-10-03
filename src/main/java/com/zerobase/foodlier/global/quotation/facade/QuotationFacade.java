package com.zerobase.foodlier.global.quotation.facade;

import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.service.quotation.QuotationService;
import com.zerobase.foodlier.module.request.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class QuotationFacade {

    private final RequestService requestService;
    private final QuotationService quotationService;

    public void sendQuotation(Long memberId, Long quotationId, Long requestId){
        Recipe quotation = quotationService.getQuotationForSend(memberId, quotationId);
        requestService.setQuotation(requestId, quotation);
    }

}
