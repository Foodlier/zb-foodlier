package com.zerobase.foodlier.module.history.transaction.repository;

import com.zerobase.foodlier.module.history.transaction.domain.model.MemberBalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberBalanceHistoryRepository extends JpaRepository<MemberBalanceHistory, Long> {
}
