package com.zerobase.foodlier.module.request.repository;

import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.request.domain.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {


    /**
     *  Member가 ChefMember에게 요청한 이력이 있는지 확인함.
     */
    boolean existsByMemberAndChefMemberAndIsPaidFalse(
            Member member, ChefMember chefMember
    );
}