package com.zerobase.foodlier.global.request.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3Service {

    public String getImageUrl(MultipartFile multipartFile) throws IOException;

    public List<String> getImageUrlList(List<MultipartFile> multipartFileList);
    public String changeFileToUrl(MultipartFile multipartFile) throws IOException;
    public String createUUIDFileName(String fileName);
}
