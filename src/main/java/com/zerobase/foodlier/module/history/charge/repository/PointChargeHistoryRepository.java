package com.zerobase.foodlier.module.history.charge.repository;

import com.zerobase.foodlier.module.history.charge.domain.model.PointChargeHistory;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointChargeHistoryRepository extends JpaRepository<PointChargeHistory, Long> {
    Page<PointChargeHistory> findByMemberOrderByCreatedAtDesc(Member member,
                                                                Pageable pageable);
}
