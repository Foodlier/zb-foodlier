package com.zerobase.foodlier.module.comment.reply.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReplyDto {
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ReplyDto(){

    }

    public ReplyDto(String message,
                    LocalDateTime createdAt,
                    LocalDateTime modifiedAt)
    {
        this.message = message;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

}
