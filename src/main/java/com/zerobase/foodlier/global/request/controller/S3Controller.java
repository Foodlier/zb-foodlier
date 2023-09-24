package com.zerobase.foodlier.global.request.controller;

import com.zerobase.foodlier.common.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @Value("${cloud.aws.bucket}")
    private String bucket;

    @PostMapping("/aws")
    public ResponseEntity<?> saveFile(@RequestPart MultipartFile multipartFile) throws IOException {
        return ResponseEntity.ok(s3Service.getImageUrl(multipartFile));
    }

    @PostMapping("/awsList")
    public ResponseEntity<?> saveFile(@RequestPart List<MultipartFile> multipartFileList) throws IOException {
        return ResponseEntity.ok(s3Service.getImageUrlList(multipartFileList));
    }

    @DeleteMapping("/aws")
    public ResponseEntity<?> deleteFile(@RequestParam String fileName) throws IOException {
        s3Service.deleteImage(fileName);
        return ResponseEntity.ok("delete complete");
    }

}
