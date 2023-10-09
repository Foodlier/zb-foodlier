package com.zerobase.foodlier.module.comment.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public interface MyPageCommentDto {

    Long getRecipeId();
    String getMessage();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime getCreatedAt();

}
