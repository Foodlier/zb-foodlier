package com.zerobase.foodlier.module.history.charge.repository;

import com.zerobase.foodlier.module.history.charge.model.PointChargeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PointChargeHistoryRepository extends JpaRepository<PointChargeHistory, Long> {
}
