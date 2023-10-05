package com.zerobase.foodlier.module.dm.dm.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageSubDto {
    private Long roomId;
    private Long dmId;
    private String message;
    private String writer;
    private LocalDateTime createdAt = LocalDateTime.now();
}
