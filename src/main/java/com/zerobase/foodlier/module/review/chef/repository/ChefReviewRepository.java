package com.zerobase.foodlier.module.review.chef.repository;

import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.request.domain.model.Request;
import com.zerobase.foodlier.module.review.chef.domain.model.ChefReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChefReviewRepository extends JpaRepository<ChefReview, Long> {

    boolean existsByRequest(Request request);
    Page<ChefReview> findByChefMember(ChefMember chefMember, Pageable pageable);

}
