package com.zerobase.foodlier.global.dm.controller;

import com.zerobase.foodlier.module.dm.dm.dto.MessagePubDto;
import com.zerobase.foodlier.module.dm.dm.dto.MessageSubDto;
import com.zerobase.foodlier.module.dm.dm.service.DmService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final DmService dmService;
    private final SimpMessagingTemplate template;

    @MessageMapping(value = "/message/{roomId}")
    public void message(MessagePubDto message) throws InterruptedException, ExecutionException {
        CompletableFuture<MessageSubDto> messageSubDto = dmService.createDm(message);
        template.convertAndSend("/sub/message/" + messageSubDto.get().getRoomId(),
                messageSubDto.get());
    }
}
