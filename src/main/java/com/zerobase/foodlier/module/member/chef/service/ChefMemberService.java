package com.zerobase.foodlier.module.member.chef.service;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.module.member.chef.dto.AroundChefDto;
import com.zerobase.foodlier.module.member.chef.dto.ChefIntroduceForm;
import com.zerobase.foodlier.module.member.chef.dto.RequestedChefDto;
import com.zerobase.foodlier.module.member.chef.dto.*;
import com.zerobase.foodlier.module.member.chef.type.ChefSearchType;
import org.springframework.data.domain.Pageable;
import java.util.List;
public interface ChefMemberService {
    void registerChef(Long memberId, ChefIntroduceForm chefIntroduceForm);
    void updateChefIntroduce(Long memberId, ChefIntroduceForm chefIntroduceForm);
    List<TopChefDto> getTopChefList();
    ChefProfileDto getChefProfile(Long chefMemberId);
    ListResponse<RequestedChefDto> getRequestedChefList(Long memberId,
                                                        Pageable pageable);
    ListResponse<AroundChefDto> getAroundChefList(Long memberId,
                                          Pageable pageable, ChefSearchType type);
    void plusExp(Long chefMemberId, int star);

    void plusStar(Long chefMemberId, int star);

}
