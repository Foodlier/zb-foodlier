package com.zerobase.foodlier.global.refrigerator.facade;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.dm.room.service.DmRoomService;
import com.zerobase.foodlier.module.request.domain.model.Request;
import com.zerobase.foodlier.module.request.service.RequestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefrigeratorFacadeTest {
    @Mock
    private RequestService requestService;

    @Mock
    private DmRoomService dmRoomService;

    @InjectMocks
    private RefrigeratorFacade refrigeratorFacade;

    @Test
    void success_validRequestAndCreateDmRoom() {
        //given
        given(requestService.approveRequest(any(), anyLong()))
                .willReturn(new Request());
        doNothing().when(dmRoomService).createDmRoom(any());

        //when
        refrigeratorFacade.validRequestAndCreateDmRoom(new MemberAuthDto(),
                1L);

        //then
        verify(dmRoomService, times(1))
                .createDmRoom(any());
    }
}