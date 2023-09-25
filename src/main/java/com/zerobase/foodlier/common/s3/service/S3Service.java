package com.zerobase.foodlier.common.s3.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {

    public String getImageUrl(MultipartFile multipartFile);
    public List<String> getImageUrlList(List<MultipartFile> multipartFileList);
    public String uploadImage(MultipartFile multipartFile);
    public String createUUIDFileName(String fileName);
    public void deleteImage(String fileName);
    public String getUpdateImageURL(MultipartFile image, String imageUrl);
    public String getImageName(String imageUrl);
    public boolean validImageUrl(String imageUrl);
}
