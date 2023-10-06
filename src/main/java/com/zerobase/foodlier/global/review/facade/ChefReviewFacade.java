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

    /**
     *  작성자 : 전현서
     *  작성일 : 2023-10-06
     *  요리사에 대한 후기를 작성하고, ChefMember의 exp와 star를 올려줌.
     */
    public void createChefReview(Long memberId, Long requestId, ChefReviewForm form){

        Long chefMemberId = chefReviewService.createChefReview(memberId, requestId, form);
        chefMemberService.plusExp(chefMemberId, form.getStar());
        chefMemberService.plusStar(chefMemberId, form.getStar());

    }

}
