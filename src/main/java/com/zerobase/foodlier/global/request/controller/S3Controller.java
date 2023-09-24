package com.zerobase.foodlier.global.request.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.zerobase.foodlier.global.request.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final AmazonS3 amazonS3;
    private final S3Service s3Service;

    @Value("${cloud.aws.bucket}")
    private String bucket;

    @PostMapping("/aws")
    public ResponseEntity<?> saveFile(@RequestPart MultipartFile multipartFile) throws IOException {
        return ResponseEntity.
                ok(
                        s3Service.getImageUrl(multipartFile)
                );

    }

}
