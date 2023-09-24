package com.zerobase.foodlier.common.s3.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3ErrorCode {

    IMAGE_DELETE_FAILED("이미지 업로드를 실패하였습니다."),
    IMAGE_UPLOAD_FAILED("이미지 삭제를 실패하였습니다."),
    IMAGE_DOES_NOT_EXIST("이미지가 존재하지 않습니다."),
    ;

    private final String description;

}
