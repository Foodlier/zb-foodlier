package com.zerobase.foodlier.module.dm.dm.service;

import com.zerobase.foodlier.module.dm.dm.dto.MessagePubDto;
import com.zerobase.foodlier.module.dm.dm.dto.MessageResponseDto;
import com.zerobase.foodlier.module.dm.dm.dto.MessageSubDto;

import java.util.concurrent.CompletableFuture;

public interface DmService {
    CompletableFuture<MessageSubDto> createDm(MessagePubDto message);

    MessageResponseDto getDmList(Long id, Long roomId, Long dmId);
}
