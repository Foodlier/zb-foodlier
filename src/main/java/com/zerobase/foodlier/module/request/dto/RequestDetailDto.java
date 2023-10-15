package com.zerobase.foodlier.module.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDetailDto {
    private Long requestId;
    private String requesterNickname;
    private String title;
    private String content;
    private List<String> ingredientList;
    private Long expectedPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime expectedAt;
    private String address;
    private String addressDetail;
    private String mainImageUrl;
    private String recipeTitle;
    private String recipeContent;
    private int heartCount;
}
