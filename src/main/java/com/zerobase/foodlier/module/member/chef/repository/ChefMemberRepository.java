package com.zerobase.foodlier.module.member.chef.repository;

import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChefMemberRepository extends JpaRepository<ChefMember, Long>, ChefMemberRepositoryCustom {
    boolean existsByMember(Member member);

    List<ChefMember> findTop5ByOrderByExpDesc();
}
