package com.zerobase.foodlier.module.heart.service;

import com.zerobase.foodlier.common.aop.RedissonLock;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.heart.domain.model.Heart;
import com.zerobase.foodlier.module.heart.exception.HeartException;
import com.zerobase.foodlier.module.heart.reposiotry.HeartRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.exception.RecipeException;
import com.zerobase.foodlier.module.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.zerobase.foodlier.module.heart.exception.HeartErrorCode.*;
import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.recipe.exception.RecipeErrorCode.NO_SUCH_RECIPE;

@Service
@RequiredArgsConstructor
public class HeartServiceImpl implements HeartService {
    private final HeartRepository heartRepository;
    private final RecipeRepository recipeRepository;
    private final MemberRepository memberRepository;

    @RedissonLock(group = "heart", key = "#recipeId")
    @Override
    public void createHeart(MemberAuthDto memberAuthDto, Long recipeId) {
        heartRepository.findByRecipeIdAndMemberId(recipeId,memberAuthDto.getId())
                .ifPresentOrElse(
                        h -> {
                            if (!h.isHeartOrNot()) {
                                h.setHeartOrNot(true);
                                h.getRecipe().plusHeart();
                                heartRepository.save(h);
                            } else {
                                throw new HeartException(ALREADY_HEART);
                            }
                        },
                        () -> {
                            Recipe recipe = recipeRepository.findById(recipeId)
                                    .orElseThrow(()-> new RecipeException(NO_SUCH_RECIPE));
                            recipe.plusHeart();
                            Member member = memberRepository.findById(memberAuthDto.getId())
                                    .orElseThrow(()->new MemberException(MEMBER_NOT_FOUND));
                            heartRepository.save(Heart.builder()
                                    .recipe(recipe)
                                    .member(member)
                                    .heartOrNot(true)
                                    .build());
                        }
                );
    }

    @RedissonLock(group = "heart", key = "#recipeId")
    @Override
    public void heartCancel(MemberAuthDto memberAuthDto, Long recipeId) {
        heartRepository.findByRecipeIdAndMemberId(recipeId,memberAuthDto.getId())
                .ifPresentOrElse(
                        h -> {
                            if (h.isHeartOrNot()) {
                                h.setHeartOrNot(false);
                                h.getRecipe().minusHeart();
                                heartRepository.save(h);
                            } else {
                                throw new HeartException(ALREADY_HEART_CANCEL);
                            }
                        },
                        () -> {
                            throw new HeartException(HEART_NOT_FOUND);
                        }
                );
    }
}
