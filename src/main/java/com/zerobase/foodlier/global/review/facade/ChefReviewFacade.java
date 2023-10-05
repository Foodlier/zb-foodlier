package com.zerobase.foodlier.global.review.facade;

import com.zerobase.foodlier.module.member.chef.service.ChefMemberService;
import com.zerobase.foodlier.module.review.chef.dto.ChefReviewForm;
import com.zerobase.foodlier.module.review.chef.service.ChefReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ChefReviewFacade {

    private final ChefReviewService chefReviewService;
    private final ChefMemberService chefMemberService;

    public void createChefReview(Long memberId, Long requestId, ChefReviewForm form){

        Long chefMemberId = chefReviewService.createChefReview(memberId, requestId, form);
        chefMemberService.plusExp(chefMemberId, form.getStar());
        chefMemberService.plusStar(chefMemberId, form.getStar());

    }

}
