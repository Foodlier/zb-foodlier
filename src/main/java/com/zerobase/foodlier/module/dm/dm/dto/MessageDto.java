package com.zerobase.foodlier.module.dm.dm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Long roomId;
    private String message;
    private String writer;
    private LocalDateTime createdAt = LocalDateTime.now();
}
