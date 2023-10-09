package com.zerobase.foodlier.global.review.facade;

import com.zerobase.foodlier.module.member.chef.service.ChefMemberService;
import com.zerobase.foodlier.module.review.chef.dto.ChefReviewForm;
import com.zerobase.foodlier.module.review.chef.service.ChefReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChefReviewFacadeTest {

    @Mock
    private ChefReviewService chefReviewService;
    @Mock
    private ChefMemberService chefMemberService;
    @InjectMocks
    private ChefReviewFacade chefReviewFacade;

    @Test
    @DisplayName("요리사 후기 작성 성공")
    void success_createChefReview(){
        //given
        given(chefReviewService.createChefReview(anyLong(), anyLong(), any()))
                .willReturn(1L);

        ChefReviewForm form = ChefReviewForm.builder()
                .content("nice")
                .star(4)
                .build();

        //when
        chefReviewFacade.createChefReview(1L, 1L, form);

        //then
        verify(chefMemberService, times(1)).plusExp(1L, 4);
        verify(chefMemberService, times(1)).plusStar(1L, 4);

    }

}