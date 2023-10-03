package com.zerobase.foodlier.module.requestform.service;

import com.zerobase.foodlier.module.requestform.dto.RequestFormDetailDto;
import com.zerobase.foodlier.module.requestform.dto.RequestFormDto;
import com.zerobase.foodlier.module.requestform.dto.RequestFormResponseDto;
import org.springframework.data.domain.Page;

public interface RequestFormService {
    void createRequestForm(Long id, RequestFormDto requestFormDto);

    Page<RequestFormResponseDto> getMyRequestForm(Long id, int pageIdx, int pageSize);

    RequestFormDetailDto getRequestFormDetail(Long id, Long requestId);

    void updateRequestForm(Long id, RequestFormDto requestFormDto, Long requestFormId);

    void deleteRequestForm(Long id, Long requestFormId);
}
