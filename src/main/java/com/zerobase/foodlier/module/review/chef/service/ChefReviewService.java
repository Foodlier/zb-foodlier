package com.zerobase.foodlier.module.review.chef.service;

import com.zerobase.foodlier.module.review.chef.dto.ChefReviewForm;
import com.zerobase.foodlier.module.review.chef.dto.ChefReviewResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChefReviewService {

    Long createChefReview(Long memberId, Long requestId, ChefReviewForm form);
    List<ChefReviewResponseDto> getChefReviewList(Long chefMemberId, Pageable pageable);

}
