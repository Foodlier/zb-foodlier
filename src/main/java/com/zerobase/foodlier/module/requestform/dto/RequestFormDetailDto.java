package com.zerobase.foodlier.module.requestform.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestFormDetailDto {
    private Long requestFormId;
    private String requesterNickname;
    private String title;
    private String content;
    private List<String> ingredientList;
    private Long expectedPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedAt;
    private String address;
    private String addressDetail;
    private String mainImageUrl;
    private Long recipeId;
    private String recipeTitle;
    private String recipeContent;
    private int heartCount;
}
