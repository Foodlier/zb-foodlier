package com.zerobase.foodlier.global.quotation.facade;

import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.service.quotation.QuotationService;
import com.zerobase.foodlier.module.request.service.RequestService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class QuotationFacadeTest {

    @Mock
    private RequestService requestService;
    @Mock
    private QuotationService quotationService;
    @InjectMocks
    private QuotationFacade quotationFacade;

    @Test
    @DisplayName("견적서 보내기 성공")
    void success_sendQuotation(){
        //given
        Recipe quotation = Recipe.builder()
                .id(1L)
                .isQuotation(true)
                .build();

        given(quotationService.getQuotationForSend(anyLong(), anyLong()))
                .willReturn(quotation);

        //when
        quotationFacade.sendQuotation(1L, 1L, 1L);

        //then
        ArgumentCaptor<Long> recipeIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Recipe> quotationCaptor = ArgumentCaptor.forClass(Recipe.class);
        verify(requestService, times(1))
                .setQuotation(recipeIdCaptor.capture(), quotationCaptor.capture());

        assertAll(
                () -> assertEquals(1L, recipeIdCaptor.getValue()),
                () -> assertEquals(quotation, quotationCaptor.getValue())
        );
    }

}