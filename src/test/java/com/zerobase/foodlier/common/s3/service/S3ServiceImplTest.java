package com.zerobase.foodlier.common.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.zerobase.foodlier.common.s3.exception.S3Exception;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.zerobase.foodlier.common.s3.exception.S3ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceImplTest {

    @Mock
    private AmazonS3 amazonS3;

    @InjectMocks
    private S3ServiceImpl s3Service;

    @Test
    @DisplayName("s3 이미지 업로드 성공")
    void success_get_image_url() throws IOException {
        //given
        String imageName = "img1.jpg";
        String content = "content";
        String expectedImageUrl = "https://zb-foodlier.s3.ap-northeast-2.amazonaws.com/img1.jpg";
        MultipartFile imageFile = new MockMultipartFile(imageName,
                imageName, "image/jpg", content.getBytes());
        given(amazonS3.getUrl(any(), any())).willReturn(new URL(expectedImageUrl));

        //when
        String imageUrl = s3Service.getImageUrl(imageFile);

        //then
        verify(amazonS3, times(1)).putObject(any(), any(), any(), any());
        verify(amazonS3, times(1)).getUrl(any(), any());
        assertEquals(expectedImageUrl, imageUrl);
    }

    @Test
    @DisplayName("s3 이미지 업로드 실패 - s3업로드 실패")
    void fail_get_image_url_image_upload_failed() {
        //given
        String imageName = "img1.jpg";
        String content = "content";
        MultipartFile imageFile = new MockMultipartFile(imageName,
                imageName, "image/jpg", content.getBytes());
        given(amazonS3.putObject(any(), any(), any(), any()))
                .willThrow(new S3Exception(IMAGE_UPLOAD_FAILED));

        //when
        S3Exception s3Exception = assertThrows(S3Exception.class,
                () -> s3Service.getImageUrl(imageFile));

        //then
        verify(amazonS3, times(1)).putObject(any(), any(), any(), any());
        verify(amazonS3, times(0)).getUrl(any(), any());
        assertEquals(IMAGE_UPLOAD_FAILED, s3Exception.getErrorCode());
    }

    @Test
    @DisplayName("s3 이미지 리스트 업로드 성공")
    void success_get_image_url_list() throws IOException {
        //given
        String imageName1 = "img1.jpg";
        String content1 = "content";
        MultipartFile imageFile1 = new MockMultipartFile(imageName1,
                imageName1, "image/jpg", content1.getBytes());

        String imageName2 = "img1.jpg";
        String content2 = "content";
        MultipartFile imageFile2 = new MockMultipartFile(imageName2,
                imageName2, "image/jpg", content2.getBytes());

        String expectedImageUrl = "https://zb-foodlier.s3.ap-northeast-2.amazonaws.com/img1.jpg";

        List<MultipartFile> imageFileList = new ArrayList<>(List.of(imageFile1, imageFile2));
        given(amazonS3.getUrl(any(), any())).willReturn(new URL(expectedImageUrl));

        //when
        List<String> imageUrlList = s3Service.getImageUrlList(imageFileList);

        //then
        verify(amazonS3, times(2)).putObject(any(), any(), any(), any());
        verify(amazonS3, times(2)).getUrl(any(), any());
        assertEquals(expectedImageUrl, imageUrlList.get(0));
        assertEquals(expectedImageUrl, imageUrlList.get(1));
    }

    @Test
    @DisplayName("s3 이미지 리스트 업로드 실패 - s3업로드 실패")
    void fail_get_image_url_list_image_upload_failed() {
        //given
        String imageName1 = "img1.jpg";
        String content1 = "content";
        MultipartFile imageFile1 = new MockMultipartFile(imageName1,
                imageName1, "image/jpg", content1.getBytes());

        String imageName2 = "img1.jpg";
        String content2 = "content";
        MultipartFile imageFile2 = new MockMultipartFile(imageName2,
                imageName2, "image/jpg", content2.getBytes());

        List<MultipartFile> imageFileList = new ArrayList<>(List.of(imageFile1, imageFile2));
        given(amazonS3.putObject(any(), any(), any(), any()))
                .willThrow(new S3Exception(IMAGE_UPLOAD_FAILED));

        //when
        S3Exception s3Exception = assertThrows(S3Exception.class,
                () -> s3Service.getImageUrlList(imageFileList));

        //then
        verify(amazonS3, times(1)).putObject(any(), any(), any(), any());
        verify(amazonS3, times(0)).getUrl(any(), any());
        assertEquals(IMAGE_UPLOAD_FAILED, s3Exception.getErrorCode());
    }

    @Test
    @DisplayName("UUID 생성 성공")
    void success_create_UUID_file_name() {
        //given
        String imageName = "image.jpg";
        int UUIDSize = 32;
        int suffixSize = 4;

        //when
        String UUIDImageName = s3Service.createUUIDFileName(imageName);

        //then
        assertEquals(UUIDSize + suffixSize, UUIDImageName.length());
    }

    @Test
    @DisplayName("s3 이미지 삭제 성공")
    void success_delete_image() {
        //given
        String imageUrl = "https://s3test.com/image.jpg";
        given(amazonS3.doesObjectExist(any(), any())).willReturn(true);

        //when
        s3Service.deleteImage(imageUrl);

        //then
        verify(amazonS3, times(1)).deleteObject(any(), any());
    }

    @Test
    @DisplayName("s3 이미지 삭제 실패 - 이미지 없음")
    void fail_delete_image_image_does_not_exist() {
        //given
        String imageUrl = "https://s3test.com/image.jpg";
        given(amazonS3.doesObjectExist(any(), any())).willReturn(false);

        //when
        S3Exception s3Exception = assertThrows(S3Exception.class,
                () -> s3Service.deleteImage(imageUrl));

        //then
        verify(amazonS3, times(1)).doesObjectExist(any(), any());
        verify(amazonS3, times(0)).deleteObject(any(), any());
        assertEquals(IMAGE_DOES_NOT_EXIST, s3Exception.getErrorCode());
    }

    @Test
    @DisplayName("s3 이미지 삭제 실패 - s3 삭제 실패")
    void fail_delete_image_image_delete_failed() {
        //given
        String imageUrl = "https://s3test.com/image.jpg";
        given(amazonS3.doesObjectExist(any(), any())).willReturn(true);
        doThrow(new S3Exception(IMAGE_DELETE_FAILED)).when(amazonS3)
                .deleteObject(any(), any());

        //when
        S3Exception s3Exception = assertThrows(S3Exception.class,
                () -> s3Service.deleteImage(imageUrl));

        //then
        verify(amazonS3, times(1)).doesObjectExist(any(), any());
        verify(amazonS3, times(1)).deleteObject(any(), any());
        assertEquals(IMAGE_DELETE_FAILED, s3Exception.getErrorCode());
    }

    @Test
    @DisplayName("s3 이미지 교체 성공")
    void success_get_update_image_url() throws IOException {
        //given
        String imageName = "img1.jpg";
        String content = "content";
        String beforeImageUrl = "https://zb-foodlier.s3.ap-northeast-2.amazonaws.com/img1.jpg";
        String expectedImageUrl = "https://zb-foodlier.s3.ap-northeast-2.amazonaws.com/img1.jpg";

        MultipartFile imageFile = new MockMultipartFile(imageName,
                imageName, "image/jpg", content.getBytes());
        given(amazonS3.doesObjectExist(any(), any())).willReturn(true);
        given(amazonS3.getUrl(any(), any())).willReturn(new URL(expectedImageUrl));

        //when
        String afterImageUrl = s3Service.getUpdateImageURL(imageFile, beforeImageUrl);

        //then
        verify(amazonS3, times(1)).deleteObject(any(), any());
        verify(amazonS3, times(1)).putObject(any(), any(), any(), any());
        verify(amazonS3, times(1)).getUrl(any(), any());
        assertEquals(expectedImageUrl, afterImageUrl);
    }

    @Test
    @DisplayName("s3 이미지 교체 실패 - 기존의 이미지 반환")
    void fail_get_update_image_url_return_imageUrl() {
        //given
        String beforeImageUrl = "https://zb-foodlier.s3.ap-northeast-2.amazonaws.com/img1.jpg";
        MultipartFile imageFile = new MockMultipartFile("emptyFile", new byte[0]);

        //when
        String afterImageUrl = s3Service.getUpdateImageURL(imageFile, beforeImageUrl);

        //then
        assertEquals(beforeImageUrl, afterImageUrl);
    }

    @Test
    @DisplayName("s3 이미지 교체 실패 - 이미지 없음")
    void fail_get_update_image_image_does_not_exist() {
        //given
        String imageUrl = "https://s3test.com/image.jpg";
        String imageName = "img1.jpg";
        String content = "content";
        MultipartFile imageFile = new MockMultipartFile(imageName,
                imageName, "image/jpg", content.getBytes());
        given(amazonS3.doesObjectExist(any(), any())).willReturn(false);

        //when
        S3Exception s3Exception = assertThrows(S3Exception.class,
                () -> s3Service.getUpdateImageURL(imageFile, imageUrl));

        //then
        verify(amazonS3, times(1)).doesObjectExist(any(), any());
        verify(amazonS3, times(0)).deleteObject(any(), any());
        assertEquals(IMAGE_DOES_NOT_EXIST, s3Exception.getErrorCode());
    }

    @Test
    @DisplayName("s3 이미지 교체 실패 - s3 삭제 실패")
    void fail_get_update_image_image_delete_failed() {
        //given
        String imageUrl = "https://s3test.com/image.jpg";
        String imageName = "img1.jpg";
        String content = "content";
        MultipartFile imageFile = new MockMultipartFile(imageName,
                imageName, "image/jpg", content.getBytes());
        given(amazonS3.doesObjectExist(any(), any())).willReturn(true);
        doThrow(new S3Exception(IMAGE_DELETE_FAILED)).when(amazonS3)
                .deleteObject(any(), any());

        //when
        S3Exception s3Exception = assertThrows(S3Exception.class,
                () -> s3Service.getUpdateImageURL(imageFile, imageUrl));

        //then
        verify(amazonS3, times(1)).doesObjectExist(any(), any());
        verify(amazonS3, times(1)).deleteObject(any(), any());
        assertEquals(IMAGE_DELETE_FAILED, s3Exception.getErrorCode());
    }

    @Test
    @DisplayName("s3 이미지 교체 실패 - s3업로드 실패")
    void fail_get_update_image_image_upload_failed() {
        //given
        String imageUrl = "https://s3test.com/image.jpg";
        String imageName = "img1.jpg";
        String content = "content";
        MultipartFile imageFile = new MockMultipartFile(imageName,
                imageName, "image/jpg", content.getBytes());
        given(amazonS3.doesObjectExist(any(), any())).willReturn(true);
        given(amazonS3.putObject(any(), any(), any(), any()))
                .willThrow(new S3Exception(IMAGE_UPLOAD_FAILED));

        //when
        S3Exception s3Exception = assertThrows(S3Exception.class,
                () -> s3Service.getUpdateImageURL(imageFile, imageUrl));

        //then
        verify(amazonS3, times(1)).putObject(any(), any(), any(), any());
        verify(amazonS3, times(0)).getUrl(any(), any());
        assertEquals(IMAGE_UPLOAD_FAILED, s3Exception.getErrorCode());
    }

    @Test
    @DisplayName("이미지 이름 추출 성공")
    void success_get_image_name() {
        //given
        String imageUrl = "https://s3test.com/image.jpg";
        String expectedImageName = "image.jpg";

        //when
        String imageName = s3Service.getImageName(imageUrl);

        //then
        assertEquals(expectedImageName, imageName);
    }

    @Test
    @DisplayName("이미지 주소 유효성 검사 성공")
    void success_valid_image_url() {
        //given
        String imageUrl = "https://s3test.com/image.jpg";
        given(amazonS3.doesObjectExist(any(), any())).willReturn(true);

        //when
        boolean isImageExist = s3Service.validImageUrl(imageUrl);

        //then
        assertTrue(isImageExist);
    }

    @Test
    @DisplayName("이미지 주소 유효성 검사 실패")
    void fail_valid_image_url() {
        //given
        String imageUrl = "https://s3test.com/image.jpg";
        given(amazonS3.doesObjectExist(any(), any())).willReturn(false);

        //when
        boolean isImageExist = s3Service.validImageUrl(imageUrl);

        //then
        assertFalse(isImageExist);
    }
}