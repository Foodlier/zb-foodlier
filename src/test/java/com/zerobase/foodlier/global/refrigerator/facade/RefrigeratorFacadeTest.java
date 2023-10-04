package com.zerobase.foodlier.global.refrigerator.facade;

import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.dm.room.service.DmRoomService;
import com.zerobase.foodlier.module.request.domain.model.Request;
import com.zerobase.foodlier.module.request.service.RequestService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RefrigeratorFacadeTest {

    @Mock
    private RequestService requestService;
    @Mock
    private DmRoomService dmRoomService;
    @InjectMocks
    private RefrigeratorFacade refrigeratorFacade;

    @Test
    @DisplayName("요청자 요청 수락 및 DM 방 생성 성공")
    void success_requester_approve_and_create_dm(){
        //given
        Request request = Request.builder()
                .id(1L)
                .build();

        DmRoom dmRoom = DmRoom.builder()
                .id(1L)
                .build();

        given(requestService.requesterApproveRequest(anyLong(), anyLong()))
                .willReturn(request);
        given(dmRoomService.createDmRoom(any()))
                .willReturn(dmRoom);

        //when
        refrigeratorFacade.requesterApproveAndCreateDm(1L, 1L);

        //then
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        ArgumentCaptor<DmRoom> dmRoomCaptor = ArgumentCaptor.forClass(DmRoom.class);

        verify(requestService, times(1)).setDmRoom(
                requestCaptor.capture(), dmRoomCaptor.capture()
        );

        assertAll(
                () -> assertEquals(request, requestCaptor.getValue()),
                () -> assertEquals(dmRoom, dmRoomCaptor.getValue())
        );
    }

    @Test
    @DisplayName("요리사 요청 수락 및 DM 방 생성")
    void success_chef_approve_and_create_dm(){
        //given
        Request request = Request.builder()
                .id(1L)
                .build();

        DmRoom dmRoom = DmRoom.builder()
                .id(1L)
                .build();

        given(requestService.chefApproveRequest(anyLong(), anyLong()))
                .willReturn(request);
        given(dmRoomService.createDmRoom(any()))
                .willReturn(dmRoom);

        //when
        refrigeratorFacade.chefApproveAndCreateDm(1L, 1L);

        //then
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        ArgumentCaptor<DmRoom> dmRoomCaptor = ArgumentCaptor.forClass(DmRoom.class);

        verify(requestService, times(1)).setDmRoom(
                requestCaptor.capture(), dmRoomCaptor.capture()
        );

        assertAll(
                () -> assertEquals(request, requestCaptor.getValue()),
                () -> assertEquals(dmRoom, dmRoomCaptor.getValue())
        );
    }

}