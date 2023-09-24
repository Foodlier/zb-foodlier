package com.zerobase.foodlier.global.request.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.bucket}")
    private String bucket;

    /**
     * 작성자 : 황태원
     * 작성일 : 2023-09-24
     * 한 개의 multipartFile을 s3에 업로드 후 url을 return
     */
    @Override
    public String getImageUrl(MultipartFile multipartFile) {
        return changeFileToUrl(multipartFile);
    }

    /**
     * 작성자 : 황태원
     * 작성일 : 2023-09-24
     * multipartFileList를 s3에 업로드 후 urlList를 return
     */
    @Override
    public List<String> getImageUrlList(List<MultipartFile> multipartFileList) {
        return multipartFileList.stream()
                .map(this::changeFileToUrl)
                .collect(Collectors.toList());
    }

    /**
     * 작성자 : 황태원
     * 작성일 : 2023-09-24
     * multipartFile을 s3에 업로드 후 url를 return
     */
    @Override
    public String changeFileToUrl(MultipartFile multipartFile) {
        String fileName = createUUIDFileName(Objects.requireNonNull(multipartFile
                .getOriginalFilename()));
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    /**
     * 작성자 : 황태원
     * 작성일 : 2023-09-24
     * multipartFile의 이름을 UUID로 랜덤하게 변경후 return
     */
    @Override
    public String createUUIDFileName(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        return uuid + extension;
    }

}
