package com.zerobase.foodlier.module.comment.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentPagingDto {

    private Long totalElements;
    private int totalPages;
    private boolean hasNextPage;
    private List<CommentDto> commentDtoList;

}
