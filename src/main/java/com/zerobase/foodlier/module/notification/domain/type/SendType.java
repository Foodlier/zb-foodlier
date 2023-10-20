package com.zerobase.foodlier.module.notification.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SendType {
    CHEF("요리사"),
    REQUESTER("요청자"),
    WRITER("작성자"),
    COMMENTER("댓글");
    private final String korean;
}