package com.zerobase.foodlier.global.dm.controller;

import com.zerobase.foodlier.module.dm.dm.dto.MessagePubDto;
import com.zerobase.foodlier.module.dm.dm.dto.MessageSubDto;
import com.zerobase.foodlier.module.dm.dm.service.DmService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StompChatControllerTest {

    static final String CHATTING_URI = "http://localhost:8080/ws";
    static final String CHATTING_SUB = "/sub/message";

    @Mock
    private DmService dmService;

    @Mock
    private SimpMessagingTemplate template;

    @InjectMocks
    private StompChatController stompChatController;

    private MockMvc mockMvc;

//    public StompChatControllerTest() {
//        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(stompChatController).build();
//    }

//    @Test
//    public void testMessage() throws Exception {
//        // Arrange
//        MessagePubDto message = new MessagePubDto(/* Initialize with required data */);
//        MessageSubDto messageSubDto = new MessageSubDto(/* Initialize with expected data */);
//
//        given(dmService.createDm(any(MessagePubDto.class)))
//                .willReturn(CompletableFuture.completedFuture(messageSubDto));
//
//        // Create a simulated WebSocket message
//        Map<String, Object> headers = new HashMap<>();
//        headers.put(SimpMessageHeaderAccessor.MESSAGE_TYPE_HEADER, SimpMessageType.MESSAGE);
//        MessageHeaders messageHeaders = new MessageHeaders(headers);
//
//        // Act
//        stompChatController.message(message);
//
//        // Assert
//        verify(dmService, times(1)).createDm(any(MessagePubDto.class));
//        verify(template, times(1)).convertAndSend(anyString(), any(MessageSubDto.class));
//    }
}

