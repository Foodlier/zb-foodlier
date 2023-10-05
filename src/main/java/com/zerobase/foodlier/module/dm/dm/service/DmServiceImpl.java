package com.zerobase.foodlier.module.dm.dm.service;

import com.zerobase.foodlier.module.dm.dm.domain.model.Dm;
import com.zerobase.foodlier.module.dm.dm.dto.MessagePubDto;
import com.zerobase.foodlier.module.dm.dm.dto.MessageSubDto;
import com.zerobase.foodlier.module.dm.dm.repository.DmRepository;
import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.dm.room.repository.DmRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class DmServiceImpl implements DmService {

    private final DmRepository dmRepository;
    private final DmRoomRepository dmRoomRepository;

    @Async("dmExecutor")
    @Override
    public CompletableFuture<MessageSubDto> createDm(MessagePubDto message) {
        DmRoom dmRoom = dmRoomRepository.findById(message.getRoomId())
                .orElseThrow();

        Dm dm = dmRepository.save(Dm.builder()
                .text(message.getMessage())
                .flag(message.getWriter())
                .dmroom(dmRoom)
                .build());

        return CompletableFuture.completedFuture(MessageSubDto.builder()
                .roomId(message.getRoomId())
                .dmId(dm.getId())
                .message(dm.getText())
                .writer(dm.getFlag())
                .createdAt(LocalDateTime.now())
                .build());
    }
}
