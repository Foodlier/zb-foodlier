package com.zerobase.foodlier.module.requestform.dto;

import com.zerobase.foodlier.module.requestform.domain.model.RequestForm;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestFormResponseDto {
    private Long requestFormId;
    private String title;
    private String content;

    public static RequestFormResponseDto fromEntity(RequestForm requestForm) {
        return RequestFormResponseDto.builder()
                .requestFormId(requestForm.getId())
                .title(requestForm.getTitle())
                .content(requestForm.getContent())
                .build();
    }
}
