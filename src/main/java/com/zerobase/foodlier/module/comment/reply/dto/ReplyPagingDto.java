package com.zerobase.foodlier.module.comment.reply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyPagingDto {

    private Long totalElements;
    private int totalPages;
    private boolean hasNextPage;
    private List<ReplyDto> replyDtoList;

}
