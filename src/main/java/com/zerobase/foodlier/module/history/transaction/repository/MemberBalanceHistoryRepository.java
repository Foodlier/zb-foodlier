package com.zerobase.foodlier.module.history.transaction.repository;

import com.zerobase.foodlier.module.history.transaction.model.MemberBalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface MemberBalanceHistoryRepository extends JpaRepository<MemberBalanceHistory, Long> {
}
