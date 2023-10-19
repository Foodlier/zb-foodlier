package com.zerobase.foodlier.module.heart.service;

import com.zerobase.foodlier.common.aop.RedissonLock;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.heart.domain.model.Heart;
import com.zerobase.foodlier.module.heart.exception.HeartErrorCode;
import com.zerobase.foodlier.module.heart.exception.HeartException;
import com.zerobase.foodlier.module.heart.reposiotry.HeartRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.recipe.domain.document.RecipeDocument;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.exception.recipe.RecipeException;
import com.zerobase.foodlier.module.recipe.repository.RecipeRepository;
import com.zerobase.foodlier.module.recipe.repository.RecipeSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.foodlier.module.heart.exception.HeartErrorCode.HEART_NOT_FOUND;
import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.recipe.exception.recipe.RecipeErrorCode.NO_SUCH_RECIPE;
import static com.zerobase.foodlier.module.recipe.exception.recipe.RecipeErrorCode.NO_SUCH_RECIPE_DOCUMENT;

@Service
@RequiredArgsConstructor
@Transactional
public class HeartServiceImpl implements HeartService {
    private final HeartRepository heartRepository;
    private final RecipeRepository recipeRepository;
    private final MemberRepository memberRepository;
    private final RecipeSearchRepository recipeSearchRepository;
    /**
     * 작성자 : 이승현
     * 작성일 : 2023-10-03
     * 좋아요를 눌렀을 때 좋아요 생성
     */
    @RedissonLock(group = "heart", key = "#recipeId")
    @Override
    public Heart createHeart(MemberAuthDto memberAuthDto, Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));
        Member member = memberRepository.findById(memberAuthDto.getId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        RecipeDocument recipeDocument = recipeSearchRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE_DOCUMENT));
        if (heartRepository.existsByRecipeAndMember(recipe, member)) {
           throw new HeartException(HeartErrorCode.ALREADY_PUSH_HEART);
        }

        recipe.plusHeart();
        recipeDocument.plusNumberOfHeart();
        recipeSearchRepository.save(recipeDocument);
        return heartRepository.save(Heart.builder()
                .recipe(recipe)
                .member(member)
                .build());
    }

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-10-03
     * 좋아요 취소했을 때 좋아요 삭제
     */
    @RedissonLock(group = "heart", key = "#recipeId")
    @Override
    public void deleteHeart(MemberAuthDto memberAuthDto, Long recipeId) {
        Heart heart = heartRepository.findHeart(recipeId, memberAuthDto.getId())
                .orElseThrow(() -> new HeartException(HEART_NOT_FOUND));
        heart.getRecipe().minusHeart();
        RecipeDocument recipeDocument = recipeSearchRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE_DOCUMENT));
        recipeDocument.minusNumberOfHeart();
        recipeSearchRepository.save(recipeDocument);
        heartRepository.delete(heart);
    }
}
