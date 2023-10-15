package com.zerobase.foodlier.module.member.chef.repository;

import com.zerobase.foodlier.module.member.chef.dto.AroundChefDto;
import com.zerobase.foodlier.module.member.chef.dto.RequestedChefDto;
import com.zerobase.foodlier.module.member.chef.type.ChefSearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChefMemberRepositoryCustom {

    Page<AroundChefDto> findAroundChefOrderByType(
            Long memberId, double lat, double lnt, double distance,
            Pageable pageable, ChefSearchType type);

    Page<RequestedChefDto> findRequestedChef(
            Long memberId, Pageable pageable
    );
}
