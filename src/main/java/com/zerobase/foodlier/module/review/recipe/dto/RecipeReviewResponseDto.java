package com.zerobase.foodlier.module.review.recipe.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerobase.foodlier.module.review.recipe.domain.model.RecipeReview;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeReviewResponseDto {

    private Long recipeId;
    private Long recipeReviewId;
    private String nickname;
    private String profileUrl;
    private String content;
    private int star;
    private String cookUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static RecipeReviewResponseDto from(RecipeReview recipeReview){
        return RecipeReviewResponseDto.builder()
                .recipeId(recipeReview.getRecipe().getId())
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
