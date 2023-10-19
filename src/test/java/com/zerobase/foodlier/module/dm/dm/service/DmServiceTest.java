package com.zerobase.foodlier.module.dm.dm.service;

import com.zerobase.foodlier.module.dm.dm.domain.model.Dm;
import com.zerobase.foodlier.module.dm.dm.dto.MessagePubDto;
import com.zerobase.foodlier.module.dm.dm.dto.MessageResponseDto;
import com.zerobase.foodlier.module.dm.dm.dto.MessageSubDto;
import com.zerobase.foodlier.module.dm.dm.exception.DmException;
import com.zerobase.foodlier.module.dm.dm.repository.DmRepository;
import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.dm.room.exception.DmRoomException;
import com.zerobase.foodlier.module.dm.room.repository.DmRoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static com.zerobase.foodlier.module.dm.dm.exception.DmErrorCode.NO_SUCH_DM;
import static com.zerobase.foodlier.module.dm.dm.type.MessageType.CHAT;
import static com.zerobase.foodlier.module.dm.dm.type.MessageType.SUGGESTION;
import static com.zerobase.foodlier.module.dm.room.exception.DmRoomErrorCode.DM_ROOM_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DmServiceTest {
    @Mock
    private DmRepository dmRepository;
    @Mock
    private DmRoomRepository dmRoomRepository;
    @InjectMocks
    private DmService dmService;

    @Test
    @DisplayName("채팅작성 - 채팅")
    void success_create_dm() throws ExecutionException, InterruptedException {
        //given
        Long roomId = 1L;
        Long dmId = 2L;
        MessagePubDto messagePubDto = MessagePubDto.builder()
                .roomId(roomId)
                .message("채팅")
                .messageType(CHAT)
                .writer("피곤한 작성자1")
                .build();
        DmRoom dmRoom = DmRoom.builder()
                .id(roomId)
                .build();
        Dm dm = Dm.builder()
                .id(dmId)
                .text(messagePubDto.getMessage())
                .flag(messagePubDto.getWriter())
                .dmroom(dmRoom)
                .messageType(messagePubDto.getMessageType())
                .build();

        given(dmRoomRepository.findById(roomId))
                .willReturn(Optional.ofNullable(dmRoom));
        given(dmRepository.save(any())).willReturn(dm);

        //when
        MessageSubDto messageSubDto =
                dmService.createDm(messagePubDto).get();

        //then
        verify(dmRepository, times(1)).save(any());
        assertAll(
                () -> assertEquals(messagePubDto.getRoomId(), messageSubDto.getRoomId()),
                () -> assertEquals(dm.getId(), messageSubDto.getDmId()),
                () -> assertEquals(dm.getText(), messageSubDto.getMessage()),
                () -> assertEquals(dm.getFlag(), messageSubDto.getWriter()),
                () -> assertEquals(dm.getMessageType(), messageSubDto.getMessageType())
        );
    }

    @Test
    @DisplayName("채팅작성 - 제안")
    void success_create_dm_suggestion() throws ExecutionException, InterruptedException {
        //given
        Long roomId = 1L;
        Long dmId = 2L;
        MessagePubDto messagePubDto = MessagePubDto.builder()
                .roomId(roomId)
                .message("20000")
                .messageType(SUGGESTION)
                .writer("피곤한 작성자1")
                .build();
        DmRoom dmRoom = DmRoom.builder()
                .id(roomId)
                .build();
        Dm dm = Dm.builder()
                .id(dmId)
                .text(messagePubDto.getMessage())
                .flag(messagePubDto.getWriter())
                .dmroom(dmRoom)
                .messageType(messagePubDto.getMessageType())
                .build();

        given(dmRoomRepository.findById(roomId))
                .willReturn(Optional.ofNullable(dmRoom));
        given(dmRepository.save(any())).willReturn(dm);

        //when
        MessageSubDto messageSubDto =
                dmService.createDm(messagePubDto).get();

        //then
        verify(dmRepository, times(1)).save(any());
        verify(dmRoomRepository, times(1)).save(any());
        assertAll(
                () -> assertEquals(messagePubDto.getRoomId(), messageSubDto.getRoomId()),
                () -> assertEquals(dm.getId(), messageSubDto.getDmId()),
                () -> assertEquals(dm.getText(), messageSubDto.getMessage()),
                () -> assertEquals(dm.getFlag(), messageSubDto.getWriter()),
                () -> assertEquals(dm.getMessageType(), messageSubDto.getMessageType())
        );
    }

    @Test
    @DisplayName("채팅작성 실패 - 채팅방 없음")
    void fail_create_dm_dm_room_not_found() {
        //given
        Long roomId = 1L;
        MessagePubDto messagePubDto = MessagePubDto.builder()
                .roomId(roomId)
                .message("20000")
                .messageType(SUGGESTION)
                .writer("피곤한 작성자1")
                .build();

        given(dmRoomRepository.findById(roomId))
                .willThrow(new DmRoomException(DM_ROOM_NOT_FOUND));

        //when
        DmRoomException dmRoomException = assertThrows(DmRoomException.class,
                () -> dmService.createDm(messagePubDto));

        //then
        assertEquals(DM_ROOM_NOT_FOUND, dmRoomException.getErrorCode());
    }

    @Test
    @DisplayName("채팅내역 가져오기")
    void success_get_dm_list() {
        // given
        Long id = 1L;
        Long roomId = 100L;
        Long dmId = 6L;

        DmRoom dmRoom = DmRoom.builder()
                .id(roomId)
                .build();

        Dm dm1 = Dm.builder()
                .id(5L)
                .dmroom(dmRoom)
                .flag("user1")
                .text("hello")
                .messageType(CHAT)
                .build();

        Dm dm2 = Dm.builder()
                .id(4L)
                .dmroom(dmRoom)
                .flag("user2")
                .text("hi")
                .messageType(CHAT)
                .build();

        int pageIdx = 0;
        int pageSize = 30;
        Pageable pageable = PageRequest.of(pageIdx, pageSize,
                Sort.by("createdAt").descending());

        List<Dm> dmList = Arrays.asList(dm1, dm2);
        Page<Dm> dmPage = new PageImpl<>(dmList);

        given(dmRoomRepository.findById(roomId)).willReturn(Optional.of(dmRoom));
        given(dmRepository.findByDmroomAndIdLessThan(dmRoom, dmId, pageable))
                .willReturn(dmPage);

        // when
        MessageResponseDto messageResponseDto = dmService.getDmList(roomId, dmId);

        // then
        assertNotNull(messageResponseDto);
        assertEquals(dmList.size(), messageResponseDto.getMessageList().size());
        assertEquals(dm2.getId(), messageResponseDto.getLastMessageId());
    }

    @Test
    @DisplayName("채팅내역 가져오기 실패 - 채팅방 없음")
    void fail_get_dm_list_dm_room_not_found() {
        // given
        Long id = 1L;
        Long roomId = 100L;
        Long dmId = 50L;

        given(dmRoomRepository.findById(roomId))
                .willThrow(new DmRoomException(DM_ROOM_NOT_FOUND));

        // when
        DmRoomException dmRoomException = assertThrows(DmRoomException.class,
                () -> dmService.getDmList(roomId, dmId));

        // then
        assertEquals(DM_ROOM_NOT_FOUND, dmRoomException.getErrorCode());
    }

    @Test
    @DisplayName("채팅내역 가져오기 실패 - 채팅기록 없음")
    void fail_get_dm_list_valid_dm_page() {
        // given
        Long id = 1L;
        Long roomId = 100L;
        Long dmId = 50L;

        DmRoom dmRoom = DmRoom.builder()
                .id(roomId)
                .build();

        int pageIdx = 0;
        int pageSize = 30;
        Pageable pageable = PageRequest.of(pageIdx, pageSize,
                Sort.by("createdAt").descending());

        given(dmRoomRepository.findById(roomId))
                .willReturn(Optional.ofNullable(dmRoom));
        given(dmRepository.findByDmroomAndIdLessThan(dmRoom, dmId, pageable))
                .willThrow(new DmException(NO_SUCH_DM));

        // when
        DmException dmException = assertThrows(DmException.class,
                () -> dmService.getDmList(roomId, dmId));

        // then
        assertEquals(NO_SUCH_DM, dmException.getErrorCode());
    }
}
