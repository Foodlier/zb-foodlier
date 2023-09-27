package com.zerobase.foodlier.module.member.chef.service;

import com.zerobase.foodlier.module.member.chef.dto.ChefIntroduceForm;

public interface ChefMemberService {
    void registerChef(Long memberId, ChefIntroduceForm chefIntroduceForm);
    void updateChefIntroduce(Long memberId, ChefIntroduceForm chefIntroduceForm);
}
