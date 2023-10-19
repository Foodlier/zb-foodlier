package com.zerobase.foodlier.common.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.zerobase.foodlier.common.s3.exception.S3Exception;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.zerobase.foodlier.common.s3.exception.S3ErrorCode.*;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private static final int IMAGE_IDX = 3;
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
        return uploadImage(multipartFile);
    }

    /**
     * 작성자 : 황태원
     * 작성일 : 2023-09-24
     * multipartFileList를 s3에 업로드 후 urlList를 return
     */
    @Override
    public List<String> getImageUrlList(List<MultipartFile> multipartFileList) {
        return multipartFileList.stream()
                .map(this::uploadImage)
                .collect(Collectors.toList());
    }

    /**
     * 작성자 : 황태원
     * 작성일 : 2023-09-24
     * multipartFile을 s3에 업로드 후 url를 return
     */
    @Override
    public String uploadImage(MultipartFile multipartFile) {
        String imageName = createUUIDFileName(Objects.requireNonNull(multipartFile
                .getOriginalFilename()));
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(bucket, imageName, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            throw new S3Exception(IMAGE_UPLOAD_FAILED);
        }

        return amazonS3.getUrl(bucket, imageName).toString();
    }

    /**
     * 작성자 : 황태원
     * 작성일 : 2023-09-24
     * multipartFile의 이름을 UUID로 랜덤하게 변경후 return
     */
    @Override
    public String createUUIDFileName(String imageName) {
        String extension = imageName.substring(imageName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        return uuid + extension;
    }

    /**
     * 작성자 : 황태원
     * 작성일 : 2023-09-24
     * 해당 이미지를 S3에서 삭제
     */
    @Override
    public void deleteImage(String imageUrl) {
        if (!validImageUrl(imageUrl)) {
            throw new S3Exception(IMAGE_DOES_NOT_EXIST);
        }

        try {
            amazonS3.deleteObject(bucket, getImageName(imageUrl));
        } catch (Exception e) {
            throw new S3Exception(IMAGE_DELETE_FAILED);
        }
    }

    /**
     * 작성자 : 황태원
     * 작성일 : 2023-09-24(2023-09-25)
     * 기존의 이미지를 삭제 후 새로운 이미지를 업로드
     * 새로운 이미지가 null 이라면 기존의 이미지 링크를 return
     */
    @Override
    public String getUpdateImageURL(MultipartFile multipartFile, String imageUrl) {
        if (multipartFile.isEmpty()) {
            return imageUrl;
        }
        deleteImage(imageUrl);
        return getImageUrl(multipartFile);
    }

    /**
     * 작성자 : 황태원
     * 작성일 : 2023-09-24
     * 이미지 주소에서 이름만 추출하여 return
     */
    @Override
    public String getImageName(String imageUrl) {
        return imageUrl.split("/")[IMAGE_IDX];
    }

    /**
     * 작성자 : 황태원
     * 작성일 : 2023-09-24
     * 이미지주소가 유효한지 검증하여 return
     */
    @Override
    public boolean validImageUrl(String imageUrl) {
        String imageName = getImageName(imageUrl);
        return amazonS3.doesObjectExist(bucket, imageName);
    }
}
