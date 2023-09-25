package com.zerobase.foodlier.module.member.member.local.service;

import com.zerobase.foodlier.module.member.member.local.dto.CoordinateResponseDto;

public interface LocalService {

    CoordinateResponseDto getCoordinate(String roadAddress);

}
