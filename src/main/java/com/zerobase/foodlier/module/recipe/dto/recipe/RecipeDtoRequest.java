package com.zerobase.foodlier.module.recipe.dto.recipe;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.domain.type.Difficulty;
import com.zerobase.foodlier.module.recipe.domain.vo.RecipeStatistics;
import com.zerobase.foodlier.module.recipe.domain.vo.Summary;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDtoRequest {

    @NotBlank(message = "꿀조합 제목을 입력해주세요.")
    @Size(min = 2, max = 20, message = "꿀조합 제목을 2~20자 범위로 입력해주세요.")
    private String title;

    @NotBlank(message = "꿀조합 내용을 입력해주세요.")
    @Size(min = 2, max = 1000, message = "꿀조합 내용을 2~1000자 범위로 입력해주세요.")
    private String content;

    @NotBlank(message = "메인 요리 이미지 주소를 입력해주세요.")
    @URL(message = "메인 요리 이미지 주소 형식이 올바르지 않습니다.")
    private String mainImageUrl;

    @Valid
    @Size(min = 1, max = 5, message = "최소 1개 ~ 최대 5개의 재료를 입력해주세요.")
    private List<RecipeIngredientDto> recipeIngredientDtoList;

    @NotNull(message = "난이도를 설정해주세요.")
    private Difficulty difficulty;

    @Valid
    @Size(min = 1, message = "최소 1개의 조리순서를 입력해주세요.")
    private List<RecipeDetailDto> recipeDetailDtoList;

    @Min(value = 1, message = "예상조리시간은 최소 1분이상으로 입력해주세요.")
    private int expectedTime;

    public Recipe toEntity(Member member) {
        return Recipe.builder()
                .summary(Summary.builder()
                        .title(title)
                        .content(content)
                        .build())
                .mainImageUrl(mainImageUrl)
                .expectedTime(expectedTime)
                .recipeStatistics(new RecipeStatistics())
                .difficulty(difficulty)
                .isPublic(true)
                .member(member)
                .recipeIngredientList(recipeIngredientDtoList
                        .stream()
                        .map(RecipeIngredientDto::toEntity)
                        .collect(Collectors.toList()))
                .recipeDetailList(recipeDetailDtoList
                        .stream()
                        .map(RecipeDetailDto::toEntity)
                        .collect(Collectors.toList()))
                .build();
    }

}
