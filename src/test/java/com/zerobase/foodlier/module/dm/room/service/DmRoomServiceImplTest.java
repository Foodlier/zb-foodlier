package com.zerobase.foodlier.module.dm.room.service;

import com.zerobase.foodlier.module.dm.dm.domain.model.Dm;
import com.zerobase.foodlier.module.dm.dm.repository.DmRepository;
import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.dm.room.dto.DmRoomDto;
import com.zerobase.foodlier.module.dm.room.exception.DmRoomException;
import com.zerobase.foodlier.module.dm.room.repository.DmRoomRepository;
import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.request.domain.model.Request;
import com.zerobase.foodlier.module.request.repository.RequestRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.zerobase.foodlier.module.dm.room.exception.DmRoomErrorCode.DM_ROOM_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DmRoomServiceImplTest {

    @Mock
    private DmRoomRepository dmRoomRepository;
    @Mock
    private DmRepository dmRepository;
    @Mock
    private RequestRepository requestRepository;
    @InjectMocks
    private DmRoomServiceImpl dmRoomService;

    @Test
    @DisplayName("채팅방 목록 가져오기")
    void success_get_dm_room_list() {
        //given
        Long id = 1L;
        int pageIdx = 0;
        int pageSize = 2;
        List<DmRoomDto> expectDmRoomDtoList = new ArrayList<>(
                List.of(DmRoomDto.builder()
                                .id(1L)
                                .nickname("nickname1")
                                .build(),
                        DmRoomDto.builder()
                                .id(2L)
                                .nickname("nickname2")
                                .build())
        );

        given(dmRoomRepository.getDmRoomPage(any(), any()))
                .willReturn(expectDmRoomDtoList);

        //when
        List<DmRoomDto> dmRoomDtoList =
                dmRoomService.getDmRoomList(id, pageIdx, pageSize);

        //then
        assertAll(
                () -> assertEquals(expectDmRoomDtoList.get(0).getNickname(),
                        dmRoomDtoList.get(0).getNickname()),
                () -> assertEquals(expectDmRoomDtoList.get(0).getId(),
                        dmRoomDtoList.get(0).getId()),
                () -> assertEquals(expectDmRoomDtoList.get(1).getNickname(),
                        dmRoomDtoList.get(1).getNickname()),
                () -> assertEquals(expectDmRoomDtoList.get(1).getId(),
                        dmRoomDtoList.get(1).getId())
        );
    }

    @Test
    @DisplayName("채팅방 나가기")
    void success_exit_dm_room() {
        //given
        Long id = 1L;
        Long chefId = 2L;
        Long memberId = 1L;
        Long roomId = 10L;
        DmRoom dmRoom = DmRoom.builder()
                .request(Request.builder()
                        .member(Member.builder()
                                .id(memberId)
                                .build())
                        .chefMember(ChefMember.builder()
                                .id(chefId)
                                .build())
                        .build())
                .isMemberExit(false)
                .isChefExit(false)
                .build();

        given(dmRoomRepository.findById(roomId))
                .willReturn(Optional.ofNullable(dmRoom));

        //when
        dmRoomService.exitDmRoom(id, roomId);

        //then
        verify(dmRoomRepository, times(1)).save(dmRoom);
    }

    @Test
    @DisplayName("채팅방 나가기 삭제")
    void success_exit_dm_room_delete() {
        //given
        Long id = 1L;
        Long chefId = 2L;
        Long memberId = 1L;
        Long roomId = 10L;
        Long requestId = 3L;
        DmRoom dmRoom = DmRoom.builder()
                .request(Request.builder()
                        .id(requestId)
                        .member(Member.builder()
                                .id(memberId)
                                .build())
                        .chefMember(ChefMember.builder()
                                .id(chefId)
                                .build())
                        .build())
                .isMemberExit(false)
                .isChefExit(true)
                .build();
        List<Dm> dmList = new ArrayList<>(List.of(Dm.builder()
                .text("채팅")
                .build()));
        Request request = Request.builder()
                .id(requestId)
                .build();

        given(dmRoomRepository.findById(roomId))
                .willReturn(Optional.ofNullable(dmRoom));
        given(dmRepository.findByDmroom(dmRoom))
                .willReturn(dmList);

        //when
        dmRoomService.exitDmRoom(id, roomId);

        //then
        verify(dmRepository, times(1)).deleteAll(dmList);
        verify(dmRoomRepository, times(1)).delete(dmRoom);
        verify(dmRoomRepository, times(1)).delete(dmRoom);
    }

    @Test
    @DisplayName("채팅방 나가기 실패 - 채팅방 없음")
    void fail_exit_dm_room_dm_room_not_found() {
        //given
        Long id = 1L;
        Long roomId = 10L;

        given(dmRoomRepository.findById(roomId))
                .willThrow(new DmRoomException(DM_ROOM_NOT_FOUND));

        //when
        DmRoomException dmRoomException = assertThrows(DmRoomException.class,
                () -> dmRoomService.exitDmRoom(id, roomId));

        //then
        assertEquals(DM_ROOM_NOT_FOUND, dmRoomException.getErrorCode());
    }
}
