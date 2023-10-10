package com.zerobase.foodlier.module.comment.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentDto {
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentDto() {

    }

    public CommentDto(String message,
                      LocalDateTime createdAt,
                      LocalDateTime modifiedAt){
        this.message = message;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
