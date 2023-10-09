package com.zerobase.foodlier.module.dm.dm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponseDto {
    private boolean hasNext;
    private Long lastMessageId;
    private List<MessageSubDto> messageList;
}
