package com.zerobase.foodlier.global.dm.controller;

import com.zerobase.foodlier.module.dm.dm.dto.MessagePubDto;
import com.zerobase.foodlier.module.dm.dm.dto.MessageSubDto;
import com.zerobase.foodlier.module.dm.dm.service.DmService;
import com.zerobase.foodlier.module.dm.dm.type.MessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class StompChatControllerTest {

    @Mock
    private DmService dmService;

    @Mock
    private SimpMessagingTemplate template;

    @InjectMocks
    private StompChatController stompChatController;

    @Test
    @DisplayName("메세지 전송 성공")
    void success_create_message() throws InterruptedException, ExecutionException {
        // given
        MessagePubDto messagePubDto = MessagePubDto.builder()
                .roomId(1L)
                .message("hi")
                .writer("user")
                .messageType(MessageType.CHAT)
                .build();

        MessageSubDto messageSubDto = MessageSubDto.builder()
                .roomId(1L)
                .dmId(2L)
                .message("hi")
                .writer("user")
                .messageType(MessageType.CHAT)
                .createdAt(LocalDateTime.of(2023, 10, 19, 19, 25, 30))
                .build();

        CompletableFuture<MessageSubDto> future = CompletableFuture
                .completedFuture(messageSubDto);

        given(dmService.createDm(messagePubDto)).willReturn(future);

        // when
        stompChatController.createMessage(messagePubDto);

        // then
        ArgumentCaptor<String> destinationCaptor =
                ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MessageSubDto> messageSubDtoCaptor =
                ArgumentCaptor.forClass(MessageSubDto.class);

        verify(dmService, times(1)).createDm(messagePubDto);
        verify(template, times(1))
                .convertAndSend(destinationCaptor.capture(), messageSubDtoCaptor.capture());

        MessageSubDto messageSubDtoCaptorValue = messageSubDtoCaptor.getValue();

        assertAll(
                () -> assertEquals("/sub/message/" + messagePubDto.getRoomId(),
                        destinationCaptor.getValue()),
                () -> assertEquals(messagePubDto.getRoomId(),
                        messageSubDtoCaptorValue.getRoomId()),
                () -> assertEquals(messagePubDto.getMessage(),
                        messageSubDtoCaptorValue.getMessage()),
                () -> assertEquals(messagePubDto.getWriter(),
                        messageSubDtoCaptorValue.getWriter()),
                () -> assertEquals(messagePubDto.getMessageType(),
                        messageSubDtoCaptorValue.getMessageType()),
                () -> assertEquals(2L,
                        messageSubDtoCaptorValue.getDmId()),
                () -> assertEquals("2023-10-19T19:25:30",
                        messageSubDtoCaptorValue.getCreatedAt().toString())
        );
    }
}