package com.zerobase.foodlier.module.member.member.local.service;

import com.zerobase.foodlier.module.member.member.local.dto.CoordinateResponseDto;
import com.zerobase.foodlier.module.member.member.local.dto.LocalApiResponse;
import com.zerobase.foodlier.module.member.member.local.exception.LocalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.zerobase.foodlier.module.member.member.local.exception.LocalErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LocalServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LocalServiceImpl localService;

    @Test
    @DisplayName("주소 -> 좌표 변환 성공")
    void success_getCoordinate(){

        //given

        String roadAddress = "경기도 성남시 분당구 판교역로 99";

        given(restTemplate.exchange(any(), any(), any(), any(Class.class)))
                .willReturn(ResponseEntity.ok(
                        new LocalApiResponse(
                                List.of(
                                        new LocalApiResponse.Documents(127.1, 37.1)
                                )
                        )
                ));

        //when
        CoordinateResponseDto coordinateResponseDto = localService.getCoordinate(roadAddress);

        //then
        assertAll(
                () -> assertEquals(37.1, coordinateResponseDto.getLat()),
                () -> assertEquals(127.1, coordinateResponseDto.getLnt())
        );

    }

    @Test
    @DisplayName("주소 -> 좌표 변환 실패 - API 호출 오류")
    void success_getCoordinate_cannot_get_api_response(){

        //given
        String roadAddress = "주소";

        given(restTemplate.exchange(any(), any(), any(), any(Class.class)))
                .willThrow(new LocalException(CANNOT_GET_API_RESPONSE));

        //when
        LocalException exception = assertThrows(LocalException.class,
                () -> localService.getCoordinate(roadAddress));

        //then
        assertEquals(CANNOT_GET_API_RESPONSE, exception.getErrorCode());

    }

    @Test
    @DisplayName("주소 -> 좌표 변환 실패 - 검색 결과 비어 있음.")
    void success_getCoordinate_empty_address_list(){

        //given
        String roadAddress = "경기도 성남시 분당구 판교역로 99999";

        given(restTemplate.exchange(any(), any(), any(), any(Class.class)))
                .willReturn(ResponseEntity.ok(
                        new LocalApiResponse(
                                new ArrayList<>()
                        )
                ));

        //when
        LocalException exception = assertThrows(LocalException.class,
                () -> localService.getCoordinate(roadAddress));

        //then
        assertEquals(EMPTY_ADDRESS_LIST, exception.getErrorCode());

    }

}