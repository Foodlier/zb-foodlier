package com.zerobase.foodlier.module.review.recipe.dto;

import com.zerobase.foodlier.module.review.recipe.domain.model.RecipeReview;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeReviewResponseDto {

    private Long recipeReviewId;
    private String nickname;
    private String profileUrl;
    private String content;
    private int star;
    private String cookUrl;
    private LocalDateTime createdAt;

    public static RecipeReviewResponseDto from(RecipeReview recipeReview){
        return RecipeReviewResponseDto.builder()
                .recipeReviewId(recipeReview.getId())
                .nickname(recipeReview.getMember().getNickname())
                .profileUrl(recipeReview.getMember().getProfileUrl())
                .content(recipeReview.getContent())
                .star(recipeReview.getStar())
                .cookUrl(recipeReview.getCookUrl())
                .createdAt(recipeReview.getCreatedAt())
                .build();
    }

}
