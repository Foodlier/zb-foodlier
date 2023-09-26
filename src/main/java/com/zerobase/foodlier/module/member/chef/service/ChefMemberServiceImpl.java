package com.zerobase.foodlier.module.member.chef.service;

import com.zerobase.foodlier.module.member.chef.repository.ChefMemberRepository;
import com.zerobase.foodlier.module.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChefMemberServiceImpl {

    private final ChefMemberRepository chefMemberRepository;
    private final RecipeRepository recipeRepository;

    /**
     *  작성자 : 전현서
     *  작성일 : 2023-09-26
     *  Recipe가 3개 이상인 회원에 대해 요리사를 등록
     */
    public void registerChef(Long memberId){



    }

}
