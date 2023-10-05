package com.zerobase.foodlier.module.dm.dm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessagePubDto {
    private Long roomId;
    private String message;
    private String writer;
}
