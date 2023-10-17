package com.zerobase.foodlier.module.requestform.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestFormDto {

    private Long recipeId;

    @NotBlank(message = "요청서 제목을 입력해주세요.")
    @Size(min = 2, max = 20, message = "요청서 제목을 2~20자 범위로 입력해주세요.")
    private String title;

    @NotBlank(message = "요청서 내용을 입력해주세요.")
    @Size(min = 2, max = 1000, message = "요청서 내용을 2~1000자 범위로 입력해주세요.")
    private String content;

    @NotNull(message = "희망 가격을 입력해주세요.")
    @Min(value = 100, message = "100원 부터 입력해주세요.")
    private Long expectedPrice;

    @NotNull(message = "희망 일시를 입력해주세요.")
    private LocalDateTime expectedAt;

    @Size(min = 1, message = "최소 1개 이상의 재료를 입력해주세요.")
    private List<@NotBlank(message = "재료명을 작성해주세요.") String> ingredientList;
}
