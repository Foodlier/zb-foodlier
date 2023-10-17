package com.zerobase.foodlier.module.recipe.dto.quotation;

import com.zerobase.foodlier.module.recipe.domain.type.Difficulty;
import com.zerobase.foodlier.module.recipe.dto.recipe.RecipeIngredientDto;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuotationDtoRequest {

    @NotBlank(message = "견적서 제목을 입력해주세요.")
    @Size(min = 2, max = 20, message = "견적서 제목을 2~20자 범위로 입력해주세요.")
    private String title;

    @NotBlank(message = "견적서 내용을 입력해주세요.")
    @Size(min = 2, max = 1000, message = "견적서 내용을 2~1000자 범위로 입력해주세요.")
    private String content;

    @Valid
    @Size(min = 1, max = 5, message = "최소 1개 ~ 최대 5개의 재료를 입력해주세요.")
    private List<RecipeIngredientDto> recipeIngredientDtoList;

    @NotNull(message = "난이도를 설정해주세요.")
    private Difficulty difficulty;

    @Size(min = 1, message = "최소 1개의 조리순서를 입력해주세요.")
    private List<@NotBlank(message = "조리순서에 내용을 입력해주세요.")String> recipeDetailDtoList;

    @Min(value = 1, message = "예상조리시간은 최소 1분이상으로 입력해주세요.")
    private int expectedTime;

}
