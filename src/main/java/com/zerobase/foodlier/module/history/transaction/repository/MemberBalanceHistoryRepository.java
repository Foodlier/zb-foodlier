package com.zerobase.foodlier.module.history.transaction.repository;

import com.zerobase.foodlier.module.history.transaction.domain.model.MemberBalanceHistory;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberBalanceHistoryRepository extends JpaRepository<MemberBalanceHistory, Long> {
    Page<MemberBalanceHistory> findByMemberOrderByCreatedAtDesc(Member member,
                                                                Pageable pageable);
}
