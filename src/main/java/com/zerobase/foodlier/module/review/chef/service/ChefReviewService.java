package com.zerobase.foodlier.module.review.chef.service;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.module.review.chef.dto.ChefReviewForm;
import com.zerobase.foodlier.module.review.chef.dto.ChefReviewResponseDto;
import org.springframework.data.domain.Pageable;

public interface ChefReviewService {

    Long createChefReview(Long memberId, Long requestId, ChefReviewForm form);
    ListResponse<ChefReviewResponseDto> getChefReviewList(Long chefMemberId, Pageable pageable);

}
