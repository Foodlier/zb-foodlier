package com.zerobase.foodlier.module.member.member.repository;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
