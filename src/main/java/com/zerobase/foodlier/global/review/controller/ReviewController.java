package com.zerobase.foodlier.global.review.controller;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.global.review.facade.ChefReviewFacade;
import com.zerobase.foodlier.global.review.facade.RecipeReviewFacade;
import com.zerobase.foodlier.module.review.chef.dto.ChefReviewForm;
import com.zerobase.foodlier.module.review.recipe.dto.RecipeReviewForm;
import com.zerobase.foodlier.module.review.recipe.dto.RecipeReviewResponseDto;
import com.zerobase.foodlier.module.review.recipe.service.RecipeReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final RecipeReviewFacade recipeReviewFacade;
    private final RecipeReviewService recipeReviewService;
    private final ChefReviewFacade chefReviewFacade;

    @PostMapping("/chef/{requestId}")
    public ResponseEntity<String> createChefReview(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable Long requestId,
            @RequestBody @Valid ChefReviewForm form
            ){
        chefReviewFacade.createChefReview(memberAuthDto.getId(), requestId, form);
        return ResponseEntity.ok("요리사 후기를 작성하였습니다.");
    }

    @PostMapping("/recipe/{recipeId}")
    public ResponseEntity<String> createRecipeReview(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable Long recipeId,
            @ModelAttribute @Valid RecipeReviewForm request
            ){
        recipeReviewFacade.createRecipeReview(memberAuthDto.getId(), recipeId, request);
        return ResponseEntity.ok("꿀조합 후기를 작성하였습니다.");
    }

    @GetMapping("/recipe/me/{recipeId}")
    public ResponseEntity<RecipeReviewResponseDto> getMyRecipeReview(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable Long recipeId
    ){
        return ResponseEntity.ok(
                recipeReviewService.getMyRecipeReview(memberAuthDto.getId(), recipeId)
        );
    }

    @GetMapping("/recipe/{pageIdx}/{pageSize}/{recipeId}")
    public ResponseEntity<List<RecipeReviewResponseDto>> getRecipeReviewList(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable int pageIdx,
            @PathVariable int pageSize,
            @PathVariable Long recipeId
    ){
        return ResponseEntity.ok(
                recipeReviewService.getRecipeReviewList(memberAuthDto.getId(), recipeId, PageRequest.of(pageIdx, pageSize))
        );
    }

    @GetMapping("/recipe/{recipeReviewId}")
    public ResponseEntity<RecipeReviewResponseDto> getRecipeReviewDetail(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable Long recipeReviewId
    ){
        return ResponseEntity.ok(
                recipeReviewService.getReviewDetail(recipeReviewId)
        );
    }

    @PutMapping("/recipe/{recipeReviewId}")
    public ResponseEntity<String> updateRecipeReview(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable Long recipeReviewId,
            @ModelAttribute @Valid RecipeReviewForm request
    ){
        recipeReviewFacade.updateRecipeReview(memberAuthDto.getId(),
                recipeReviewId, request);
        return ResponseEntity.ok(
                "꿀조합 후기가 수정되었습니다."
        );
    }

    @DeleteMapping("/recipe/{recipeReviewId}")
    public ResponseEntity<String> deleteRecipeReview(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable Long recipeReviewId
    ){
        recipeReviewFacade.deleteRecipeReview(memberAuthDto.getId(), recipeReviewId);
        return ResponseEntity.ok(
                "꿀조합 후기가 삭제되었습니다."
        );
    }

}
