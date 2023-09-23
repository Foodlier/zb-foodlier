package com.zerobase.foodlier.module.review.chef.repository;

import com.zerobase.foodlier.module.review.chef.domain.model.ChefReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChefReviewRepository extends JpaRepository<ChefReview, Long> {
}
