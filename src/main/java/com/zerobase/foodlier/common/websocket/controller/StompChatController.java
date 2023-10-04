package com.zerobase.foodlier.common.websocket.controller;

import com.zerobase.foodlier.module.dm.dm.dto.MessageDto;
import com.zerobase.foodlier.module.dm.dm.service.DmService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final DmService dmService;
    private final SimpMessagingTemplate template;

    @MessageMapping(value = "/message/{roomId}")
    public void message(MessageDto message) {
        dmService.createDm(message);
        template.convertAndSend("/sub/message/" + message.getRoomId(), message);
    }
}
