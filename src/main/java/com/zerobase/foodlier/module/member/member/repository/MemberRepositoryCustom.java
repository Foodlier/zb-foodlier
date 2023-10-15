package com.zerobase.foodlier.module.member.member.repository;

import com.zerobase.foodlier.module.member.member.dto.DefaultProfileDtoResponse;
import com.zerobase.foodlier.module.member.member.dto.RequestedMemberDto;
import com.zerobase.foodlier.module.member.member.type.RequestedOrderingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {

    Page<RequestedMemberDto> getRequestedMemberListOrderByType(Long chefMemberId, double lat, double lnt, Pageable pageable, RequestedOrderingType orderingType);

    DefaultProfileDtoResponse getDefaultProfile(Long memberId);
}
