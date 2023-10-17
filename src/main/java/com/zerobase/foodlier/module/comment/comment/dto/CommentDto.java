package com.zerobase.foodlier.module.comment.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentDto {
    private Long id;
    private String message;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss", timezone="Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss", timezone="Asia/Seoul")
    private LocalDateTime modifiedAt;

    public CommentDto() {

    }

    public CommentDto(Long id,
                      String message,
                      LocalDateTime createdAt,
                      LocalDateTime modifiedAt) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}