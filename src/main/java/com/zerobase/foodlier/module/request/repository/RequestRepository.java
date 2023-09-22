package com.zerobase.foodlier.module.request.repository;

import com.zerobase.foodlier.module.request.domain.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
