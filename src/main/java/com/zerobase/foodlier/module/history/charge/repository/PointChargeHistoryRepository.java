package com.zerobase.foodlier.module.history.charge.repository;

import com.zerobase.foodlier.module.history.charge.domain.model.PointChargeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointChargeHistoryRepository extends JpaRepository<PointChargeHistory, Long> {
}
