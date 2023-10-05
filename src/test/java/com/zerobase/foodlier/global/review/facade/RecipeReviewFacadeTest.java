package com.zerobase.foodlier.global.review.facade;

import com.zerobase.foodlier.common.s3.service.S3Service;
import com.zerobase.foodlier.module.recipe.service.recipe.RecipeService;
import com.zerobase.foodlier.module.review.recipe.dto.ChangedRecipeReviewResponse;
import com.zerobase.foodlier.module.review.recipe.dto.RecipeReviewForm;
import com.zerobase.foodlier.module.review.recipe.dto.RecipeReviewRequestDto;
import com.zerobase.foodlier.module.review.recipe.service.RecipeReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RecipeReviewFacadeTest {

    @Mock
    private RecipeReviewService recipeReviewService;
    @Mock
    private RecipeService recipeService;
    @Mock
    private S3Service s3Service;
    @InjectMocks
    private RecipeReviewFacade recipeReviewFacade;

    private MultipartFile getMultiPartFile(){
        return new MockMultipartFile("image1.png", "김치찌개사진".getBytes(StandardCharsets.UTF_8));
    }

    @Nested
    @DisplayName("createRecipeReview() 테스트")
    class createRecipeReviewTest{

        @Test
        @DisplayName("꿀조합 후기 작성 성공 - 이미지가 존재하는 경우")
        void success_createRecipeReview_include_image_file(){
            //given
            MultipartFile imageFile = getMultiPartFile();
            given(s3Service.getImageUrl(any()))
                    .willReturn("https://s3.test.com/image1.png");

            RecipeReviewForm form = RecipeReviewForm.builder()
                    .content("후기 내용")
                    .star(2)
                    .cookImage(imageFile)
                    .build();
            //when
            recipeReviewFacade.createRecipeReview(1L, 1L, form);

            //then
            ArgumentCaptor<RecipeReviewRequestDto> captor = ArgumentCaptor.forClass(RecipeReviewRequestDto.class);

            verify(recipeReviewService, times(1)).createRecipeReview(
                    eq(1L), eq(1L), captor.capture()
            );

            verify(recipeService, times(1)).plusReviewStar(
                    eq(1L), eq(2)
            );

            RecipeReviewRequestDto recipeReviewRequestDto = captor.getValue();

            assertAll(
                    () -> assertEquals(form.getContent(), recipeReviewRequestDto.getContent()),
                    () -> assertEquals(form.getStar(), recipeReviewRequestDto.getStar()),
                    () -> assertEquals("https://s3.test.com/image1.png", recipeReviewRequestDto.getCookImageUrl())
            );
        }

        @Test
        @DisplayName("꿀조합 후기 작성 성공 - 이미지가 존재하지 않는 경우")
        void success_createRecipeReview_not_include_image_file(){
            //given
            RecipeReviewForm form = RecipeReviewForm.builder()
                    .content("후기 내용")
                    .star(2)
                    .cookImage(null)
                    .build();
            //when
            recipeReviewFacade.createRecipeReview(1L, 1L, form);

            //then
            ArgumentCaptor<RecipeReviewRequestDto> captor = ArgumentCaptor.forClass(RecipeReviewRequestDto.class);

            verify(s3Service, times(0)).getImageUrl(any());

            verify(recipeReviewService, times(1)).createRecipeReview(
                    eq(1L), eq(1L), captor.capture()
            );

            verify(recipeService, times(1)).plusReviewStar(
                    eq(1L), eq(2)
            );

            RecipeReviewRequestDto recipeReviewRequestDto = captor.getValue();

            assertAll(
                    () -> assertEquals(form.getContent(), recipeReviewRequestDto.getContent()),
                    () -> assertEquals(form.getStar(), recipeReviewRequestDto.getStar()),
                    () -> assertNull(recipeReviewRequestDto.getCookImageUrl())
            );
        }

    }

    @Nested
    @DisplayName("updateRecipeReview() 테스트")
    class updateRecipeReviewTest{

        @Test
        @DisplayName("꿀조합 후기 수정 성공 - 이미지가 존재하는 경우")
        void success_updateRecipeReview_include_image_file(){
            //given
            MultipartFile imageFile = getMultiPartFile();
            given(s3Service.getImageUrl(any()))
                    .willReturn("https://s3.test.com/image1.png");

            RecipeReviewForm form = RecipeReviewForm.builder()
                    .content("후기 내용")
                    .star(2)
                    .cookImage(imageFile)
                    .build();

            ChangedRecipeReviewResponse updatedResponse = ChangedRecipeReviewResponse
                    .builder()
                    .recipeId(1L)
                    .star(4)
                    .cookImageUrl("https://s3.test.com/imageOrigin.png")
                    .build();

            given(recipeReviewService.updateRecipeReview(anyLong(), anyLong(), any()))
                    .willReturn(updatedResponse);

            //when
            recipeReviewFacade.updateRecipeReview(1L, 1L,
                    form);

            //then
            ArgumentCaptor<RecipeReviewRequestDto> captor = ArgumentCaptor
                    .forClass(RecipeReviewRequestDto.class);

            verify(recipeReviewService, times(1)).updateRecipeReview(
                    eq(1L), eq(1L), captor.capture()
            );

            verify(s3Service, times(1))
                    .deleteImage(eq("https://s3.test.com/imageOrigin.png"));

            verify(recipeService, times(1))
                    .updateReviewStar(eq(1L), eq(4), eq(2));

            //서비스에서 updateRecipeReview 할 때, 들어가는 값 검증
            RecipeReviewRequestDto recipeReviewRequestDto = captor.getValue();
            assertAll(
                    () -> assertEquals(form.getContent(), recipeReviewRequestDto.getContent()),
                    () -> assertEquals(form.getStar(), recipeReviewRequestDto.getStar()),
                    () -> assertEquals("https://s3.test.com/image1.png",
                            recipeReviewRequestDto.getCookImageUrl())
            );
        }

        @Test
        @DisplayName("꿀조합 후기 수정 성공 - 이미지가 존재하지 않는 경우")
        void success_updateRecipeReview_not_include_image_file(){
            //given
            RecipeReviewForm form = RecipeReviewForm.builder()
                    .content("후기 내용")
                    .star(2)
                    .build();

            ChangedRecipeReviewResponse updatedResponse = ChangedRecipeReviewResponse
                    .builder()
                    .recipeId(1L)
                    .star(4)
                    .cookImageUrl("https://s3.test.com/imageOrigin.png")
                    .build();

            given(recipeReviewService.updateRecipeReview(anyLong(), anyLong(), any()))
                    .willReturn(updatedResponse);

            //when
            recipeReviewFacade.updateRecipeReview(1L, 1L,
                    form);

            //then
            ArgumentCaptor<RecipeReviewRequestDto> captor = ArgumentCaptor
                    .forClass(RecipeReviewRequestDto.class);

            verify(recipeReviewService, times(1)).updateRecipeReview(
                    eq(1L), eq(1L), captor.capture()
            );

            verify(s3Service, times(0))
                    .deleteImage(anyString());

            verify(recipeService, times(1))
                    .updateReviewStar(eq(1L), eq(4), eq(2));

            //서비스에서 updateRecipeReview 할 때, 들어가는 값 검증
            RecipeReviewRequestDto recipeReviewRequestDto = captor.getValue();
            assertAll(
                    () -> assertEquals(form.getContent(), recipeReviewRequestDto.getContent()),
                    () -> assertEquals(form.getStar(), recipeReviewRequestDto.getStar()),
                    () -> assertNull(recipeReviewRequestDto.getCookImageUrl())
            );
        }

    }

    @Nested
    @DisplayName("deleteRecipeReview() 테스트")
    class deleteRecipeReviewTest{

        @Test
        @DisplayName("꿀조합 후기 삭제 성공 - 이미지가 존재하는 경우")
        void success_deleteRecipeReview_include_image_file(){
            //given
            ChangedRecipeReviewResponse deletedResposne = ChangedRecipeReviewResponse
                    .builder()
                    .recipeId(1L)
                    .star(3)
                    .cookImageUrl("https://s3.test.com/originImage.png")
                    .build();

            given(recipeReviewService.deleteRecipeReview(anyLong(), anyLong()))
                    .willReturn(deletedResposne);
            //when
            recipeReviewFacade.deleteRecipeReview(1L, 1L);

            //then
            verify(recipeReviewService, times(1)).deleteRecipeReview(
                    eq(1L), eq(1L)
            );

            verify(s3Service, times(1)).deleteImage(
                    eq("https://s3.test.com/originImage.png")
            );

            verify(recipeService, times(1)).minusReviewStar(
                    eq(1L), eq(3)
            );
        }

        @Test
        @DisplayName("꿀조합 후기 삭제 성공 - 이미지가 존재하지 않는 경우")
        void success_deleteRecipeReview_not_include_image_file(){
            //given
            ChangedRecipeReviewResponse deletedResposne = ChangedRecipeReviewResponse
                    .builder()
                    .recipeId(1L)
                    .star(3)
                    .cookImageUrl(null)
                    .build();

            given(recipeReviewService.deleteRecipeReview(anyLong(), anyLong()))
                    .willReturn(deletedResposne);
            //when
            recipeReviewFacade.deleteRecipeReview(1L, 1L);

            //then
            verify(recipeReviewService, times(1)).deleteRecipeReview(
                    eq(1L), eq(1L)
            );

            verify(s3Service, times(0)).deleteImage(
                    anyString()
            );

            verify(recipeService, times(1)).minusReviewStar(
                    eq(1L), eq(3)
            );
        }

    }

}