package com.zerobase.foodlier.module.request.repository;

import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.request.domain.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {


    /**
     *  Member가 ChefMember에게 요청한 이력이 있는지 확인함.
     */
    boolean existsByMemberAndChefMemberAndIsFinishedFalse(
            Member member, ChefMember chefMember
    );

    boolean existsByRecipe(Recipe recipe);

    Optional<Request> findByMemberAndChefMember(Member member,ChefMember chefMember);

    Optional<Request> findByIdAndMember(Long requestId, Member member);
}
