package com.zerobase.foodlier.global.dm.controller;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.config.SecurityConfig;
import com.zerobase.foodlier.mockuser.WithCustomMockUser;
import com.zerobase.foodlier.module.dm.dm.dto.MessageResponseDto;
import com.zerobase.foodlier.module.dm.dm.dto.MessageSubDto;
import com.zerobase.foodlier.module.dm.dm.service.DmService;
import com.zerobase.foodlier.module.dm.room.dto.DmRoomDto;
import com.zerobase.foodlier.module.dm.room.service.DmRoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.zerobase.foodlier.module.dm.dm.type.MessageType.CHAT;
import static com.zerobase.foodlier.module.dm.dm.type.MessageType.SUGGESTION;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = DmController.class, excludeFilters =
@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class}))
public class DmControllerTest {

    @MockBean
    private DmRoomService dmRoomService;

    @MockBean
    private DmService dmService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("채팅방 가져오기 성공")
    @WithCustomMockUser
    void success_create_dm_room() throws Exception {
        //given
        int pageIdx = 0;
        int pageSize = 1;
        long id = 1L;
        ListResponse<DmRoomDto> listResponse = ListResponse.<DmRoomDto>builder()
                .content(new ArrayList<>(
                        List.of(DmRoomDto.builder()
                                .roomId(2L)
                                .requestId(3L)
                                .nickname("상대방 닉네임")
                                .profileUrl("http://s3.test.com/test")
                                .expectedPrice(30000L)
                                .isSuggested(false)
                                .isExit(false)
                                .role("CHEF")
                                .build())))
                .hasNextPage(false)
                .totalElements(1)
                .totalPages(1)
                .build();

        given(dmRoomService.getDmRoomList(id, pageIdx, pageSize))
                .willReturn(listResponse);

        //when
        ResultActions perform = mockMvc
                .perform(get("/dm/room/{pageIdx}/{pageSize}",
                        pageIdx, pageSize));

        //then
        DmRoomDto dmRoomDto = listResponse.getContent().get(0);
        perform.andExpect(jsonPath("$.content.[0].roomId")
                        .value(dmRoomDto.getRoomId()))
                .andExpect(jsonPath("$.content.[0].requestId")
                        .value(dmRoomDto.getRequestId()))
                .andExpect(jsonPath("$.content.[0].nickname")
                        .value(dmRoomDto.getNickname()))
                .andExpect(jsonPath("$.content.[0].profileUrl")
                        .value(dmRoomDto.getProfileUrl()))
                .andExpect(jsonPath("$.content.[0].expectedPrice")
                        .value(dmRoomDto.getExpectedPrice()))
                .andExpect(jsonPath("$.content.[0].isSuggested")
                        .value(dmRoomDto.getIsSuggested()))
                .andExpect(jsonPath("$.content.[0].isExit")
                        .value(dmRoomDto.getIsExit()))
                .andExpect(jsonPath("$.content.[0].role")
                        .value(dmRoomDto.getRole()))
                .andDo(print());
    }

    @Test
    @DisplayName("채팅방 나가기 성공")
    @WithCustomMockUser
    void success_exit_dm_room() throws Exception {
        //given
        long roomId = 1L;

        //when
        ResultActions perform = mockMvc.perform(put("/dm/room/exit/{roomId}",
                roomId).with(csrf()));

        //then
        perform.andExpect(status().isOk())
                .andExpect(content().string("성공적으로 채팅방을 나갔습니다."))
                .andDo(print());
        verify(dmRoomService, times(1)).exitDmRoom(anyLong(), anyLong());
    }

    @Test
    @DisplayName("채팅 가져오기 성공")
    @WithCustomMockUser
    void success_get_dm_list() throws Exception {
        //given
        long roomId = 3L;
        long dmId = 2L;

        MessageSubDto messageSubDto1 = MessageSubDto.builder()
                .roomId(roomId)
                .dmId(dmId)
                .message("안녕1")
                .writer("작성자1")
                .messageType(CHAT)
                .createdAt(LocalDateTime.of(2023, 10, 18, 2, 56, 30))
                .build();
        MessageSubDto messageSubDto2 = MessageSubDto.builder()
                .roomId(roomId)
                .dmId(dmId)
                .message("안녕2")
                .writer("작성자2")
                .messageType(SUGGESTION)
                .createdAt(LocalDateTime.of(2023, 10, 18, 2, 58, 30))
                .build();
        MessageResponseDto messageResponseDto = MessageResponseDto.builder()
                .hasNext(true)
                .lastMessageId(10L)
                .messageList(new ArrayList<>(List.of(messageSubDto1, messageSubDto2)))
                .build();

        given(dmService.getDmList(roomId, dmId))
                .willReturn(messageResponseDto);

        //when
        ResultActions perform = mockMvc
                .perform(get("/dm/message?roomId={roomId}&dmId={dmId}",
                        roomId, dmId));

        //then
        perform.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.hasNext")
                        .value(true))
                .andExpect(jsonPath("$.lastMessageId")
                        .value(10L))
                .andExpect(jsonPath("$.messageList.[0].roomId")
                        .value(messageSubDto1.getRoomId()))
                .andExpect(jsonPath("$.messageList.[0].dmId")
                        .value(messageSubDto1.getDmId()))
                .andExpect(jsonPath("$.messageList.[0].message")
                        .value(messageSubDto1.getMessage()))
                .andExpect(jsonPath("$.messageList.[0].writer")
                        .value(messageSubDto1.getWriter()))
                .andExpect(jsonPath("$.messageList.[0].messageType")
                        .value(messageSubDto1.getMessageType().toString()))
                .andExpect(jsonPath("$.messageList.[0].createdAt")
                        .value("2023-10-18 02:56:30"))
                .andExpect(jsonPath("$.messageList.[1].roomId")
                        .value(messageSubDto2.getRoomId()))
                .andExpect(jsonPath("$.messageList.[1].dmId")
                        .value(messageSubDto2.getDmId()))
                .andExpect(jsonPath("$.messageList.[1].message")
                        .value(messageSubDto2.getMessage()))
                .andExpect(jsonPath("$.messageList.[1].writer")
                        .value(messageSubDto2.getWriter()))
                .andExpect(jsonPath("$.messageList.[1].messageType")
                        .value(messageSubDto2.getMessageType().toString()))
                .andExpect(jsonPath("$.messageList.[1].createdAt")
                        .value("2023-10-18 02:58:30"));
    }
}
