package com.zerobase.foodlier.common.s3.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {

    String getImageUrl(MultipartFile multipartFile);

    List<String> getImageUrlList(List<MultipartFile> multipartFileList);

    String uploadImage(MultipartFile multipartFile);

    String createUUIDFileName(String fileName);

    void deleteImage(String fileName);

    String getUpdateImageURL(MultipartFile image, String imageUrl);

    String getImageName(String imageUrl);

    boolean validImageUrl(String imageUrl);
}
