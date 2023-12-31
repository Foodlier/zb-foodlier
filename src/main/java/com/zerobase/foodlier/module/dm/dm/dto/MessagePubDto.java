package com.zerobase.foodlier.module.dm.dm.dto;

import com.zerobase.foodlier.module.dm.dm.type.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessagePubDto {
    private Long roomId;
    private String message;
    private String writer;
    private MessageType messageType;
}
