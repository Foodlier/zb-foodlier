package com.zerobase.foodlier.module.member.member.local.service;

import com.zerobase.foodlier.module.member.member.dto.LocalApiResponse;
import com.zerobase.foodlier.module.member.member.local.constants.LocalApiConstants;
import com.zerobase.foodlier.module.member.member.local.dto.CoordinateResponseDto;
import com.zerobase.foodlier.module.member.member.local.exception.LocalErrorCode;
import com.zerobase.foodlier.module.member.member.local.exception.LocalException;
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

@Service
public class LocalServiceImpl implements LocalService{

    @Value("${spring.kakao.admin-key}")
    private String ADMIN_KEY;

    public CoordinateResponseDto getCoordinate(String roadAddress){
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> httpEntity = new HttpEntity<>(getHeaders()); //엔티티로 만들기
        URI targetUrl = UriComponentsBuilder
                .fromUriString(LocalApiConstants.LOCAL_API_URL)
                .queryParam("query", roadAddress)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        CoordinateResponseDto coordinateResponseDto = null;

        try {
            ResponseEntity<LocalApiResponse> result = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, LocalApiResponse.class);
            coordinateResponseDto = new CoordinateResponseDto(
                    Objects.requireNonNull(result.getBody()).getDocuments().get(0).getLat(),
                    result.getBody().getDocuments().get(0).getLnt());
        }catch (Exception e){
            throw new LocalException(LocalErrorCode.CANNOT_ADDRESS_PARSING);
        }
        return coordinateResponseDto;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = "KakaoAK " + ADMIN_KEY;

        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return httpHeaders;
    }
}
