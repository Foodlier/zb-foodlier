package com.zerobase.foodlier.module.member.member.local.service;

import com.zerobase.foodlier.module.member.member.local.dto.CoordinateResponseDto;
import com.zerobase.foodlier.module.member.member.local.dto.LocalApiResponse;
import com.zerobase.foodlier.module.member.member.local.exception.LocalErrorCode;
import com.zerobase.foodlier.module.member.member.local.exception.LocalException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.zerobase.foodlier.module.member.member.local.constants.LocalApiConstants.*;

@Service
@RequiredArgsConstructor
public class LocalServiceImpl implements LocalService{

    @Value("${spring.kakao.admin-key}")
    private String ADMIN_KEY;

    private final RestTemplate restTemplate;

    /**
     *  작성자 : 전현서
     *  작성일 : 2023-9-25 (2023-9-26)
     *  주소를 입력하여, 좌표(위도, 경도)를 가져오는 메서드
     */
    public CoordinateResponseDto getCoordinate(String roadAddress){
//        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> httpEntity = new HttpEntity<>(getHeaders()); //엔티티로 만들기
        URI targetUrl = UriComponentsBuilder
                .fromUriString(LOCAL_API_URL)
                .queryParam(PARAMETER, roadAddress)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        //API 호출
        ResponseEntity<LocalApiResponse> apiResponse = null;
        try{
            apiResponse = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, LocalApiResponse.class);
        }catch (Exception e){
            throw new LocalException(LocalErrorCode.CANNOT_GET_API_RESPONSE);
        }

        //데이터 추출
        CoordinateResponseDto coordinateResponseDto = null;
        try {
            coordinateResponseDto = new CoordinateResponseDto(
                    Objects.requireNonNull(apiResponse.getBody()).getDocuments().get(0).getLat(),
                    apiResponse.getBody().getDocuments().get(0).getLnt());
        }catch (Exception e){
            throw new LocalException(LocalErrorCode.EMPTY_ADDRESS_LIST);
        }

        return coordinateResponseDto;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = KAKAO_PREFIX + ADMIN_KEY;

        httpHeaders.set(HEADER, auth);
        httpHeaders.set(CONTENT_TYPE_TITLE, CONTENT_TYPE);

        return httpHeaders;
    }
}
