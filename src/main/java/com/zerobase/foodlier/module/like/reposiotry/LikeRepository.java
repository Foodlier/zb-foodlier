package com.zerobase.foodlier.module.like.reposiotry;

import com.zerobase.foodlier.module.like.domain.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
