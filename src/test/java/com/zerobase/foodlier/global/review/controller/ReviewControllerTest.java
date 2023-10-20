package com.zerobase.foodlier.global.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.config.SecurityConfig;
import com.zerobase.foodlier.global.review.facade.ChefReviewFacade;
import com.zerobase.foodlier.global.review.facade.RecipeReviewFacade;
import com.zerobase.foodlier.mockuser.WithCustomMockUser;
import com.zerobase.foodlier.module.review.chef.dto.ChefReviewForm;
import com.zerobase.foodlier.module.review.recipe.dto.RecipeReviewForm;
import com.zerobase.foodlier.module.review.recipe.dto.RecipeReviewResponseDto;
import com.zerobase.foodlier.module.review.recipe.service.RecipeReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value = ReviewController.class, excludeFilters =
@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class}))
class ReviewControllerTest {

    @MockBean
    private RecipeReviewFacade recipeReviewFacade;
    @MockBean
    private RecipeReviewService recipeReviewService;
    @MockBean
    private ChefReviewFacade chefReviewFacade;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithCustomMockUser
    @DisplayName("요리사 후기 작성 성공")
    void success_createChefReview() throws Exception {
        //when & then
        ChefReviewForm form = ChefReviewForm.builder()
                .content("최고의 극찬")
                .star(5)
                .build();

        mockMvc.perform(post("/review/chef/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("요리사 후기를 작성하였습니다."));

        ArgumentCaptor<ChefReviewForm> captor = ArgumentCaptor.forClass(ChefReviewForm.class);
        verify(chefReviewFacade, times(1))
                .createChefReview(eq(1L), eq(1L), captor.capture());
        ChefReviewForm expectedForm = captor.getValue();

        assertAll(
                () -> assertEquals(form.getContent(), expectedForm.getContent()),
                () -> assertEquals(form.getStar(), expectedForm.getStar())
        );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("꿀조합 후기 작성 성공")
    void success_createRecipeReview() throws Exception {
        //when & then
        String cookImageName = "foodlier_logo.png";
        File file = new File("src/test/resources/foodlier_logo.png");

        RecipeReviewForm form = RecipeReviewForm.builder()
                .content("최고의 극찬")
                .star(5)
                .cookImage(new MockMultipartFile(cookImageName,
                        cookImageName, MediaType.IMAGE_PNG_VALUE, new FileInputStream(file)))
                .build();

        mockMvc.perform(multipart("/review/recipe/1")
                        .file("cookImage", form.getCookImage().getBytes())
                        .param("content", form.getContent())
                        .param("star", String.valueOf(form.getStar()))
                        .with(request -> {
                            request.setMethod("POST");
                            return request;
                        }).with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("꿀조합 후기를 작성하였습니다."));

        ArgumentCaptor<RecipeReviewForm> captor = ArgumentCaptor.forClass(RecipeReviewForm.class);
        verify(recipeReviewFacade, times(1))
                .createRecipeReview(eq(1L), eq(1L), captor.capture());
        RecipeReviewForm expectedForm = captor.getValue();

        assertAll(
                () -> assertEquals(form.getContent(), expectedForm.getContent()),
                () -> assertEquals(form.getStar(), expectedForm.getStar()),
                () -> assertEquals(form.getCookImage().getSize(), expectedForm.getCookImage().getSize())
        );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("꿀조합에 대한 내 꿀조합 후기 조회 성공")
    void success_getMyRecipeReview() throws Exception {
        //given
        RecipeReviewResponseDto recipeReviewResponseDto = RecipeReviewResponseDto
                .builder()
                .recipeId(1L)
                .recipeReviewId(1L)
                .nickname("user")
                .profileUrl("https://s3.test.com/profile.png")
                .content("좋아용!")
                .createdAt(LocalDateTime.of(2023, 10, 10, 10, 30, 50))
                .build();
        given(recipeReviewService.getMyRecipeReview(anyLong(), anyLong()))
                .willReturn(recipeReviewResponseDto);

        //when & then
        mockMvc.perform(get("/review/recipe/me/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeId").value(recipeReviewResponseDto.getRecipeId()))
                .andExpect(jsonPath("$.recipeReviewId").value(recipeReviewResponseDto.getRecipeReviewId()))
                .andExpect(jsonPath("$.nickname").value(recipeReviewResponseDto.getNickname()))
                .andExpect(jsonPath("$.profileUrl").value(recipeReviewResponseDto.getProfileUrl()))
                .andExpect(jsonPath("$.content").value(recipeReviewResponseDto.getContent()))
                .andExpect(jsonPath("$.createdAt").value("2023-10-10 10:30:50"));
    }

    @Test
    @WithCustomMockUser
    @DisplayName("꿀조합 후기 목록 조회 성공")
    void success_getRecipeReviewList() throws Exception {
        //given
        RecipeReviewResponseDto recipeReviewResponseDto = RecipeReviewResponseDto
                .builder()
                .recipeId(1L)
                .recipeReviewId(1L)
                .nickname("user")
                .profileUrl("https://s3.test.com/profile.png")
                .content("좋아용!")
                .createdAt(LocalDateTime.of(2023, 10, 10, 10, 30, 50))
                .build();
        given(recipeReviewService.getRecipeReviewList(anyLong(), anyLong(), any()))
                .willReturn(ListResponse.from(new PageImpl<>(List.of(recipeReviewResponseDto))));

        //when & then
        mockMvc.perform(get("/review/recipe/0/10/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].recipeId").value(recipeReviewResponseDto.getRecipeId()))
                .andExpect(jsonPath("$.content.[0].recipeReviewId").value(recipeReviewResponseDto.getRecipeReviewId()))
                .andExpect(jsonPath("$.content.[0].nickname").value(recipeReviewResponseDto.getNickname()))
                .andExpect(jsonPath("$.content.[0].profileUrl").value(recipeReviewResponseDto.getProfileUrl()))
                .andExpect(jsonPath("$.content.[0].content").value(recipeReviewResponseDto.getContent()))
                .andExpect(jsonPath("$.content.[0].createdAt").value("2023-10-10 10:30:50"));
    }

    @Test
    @WithCustomMockUser
    @DisplayName("꿀조합 후기 상세 조회 성공")
    void success_getRecipeReviewDetail() throws Exception {
        //given
        RecipeReviewResponseDto recipeReviewResponseDto = RecipeReviewResponseDto
                .builder()
                .recipeId(1L)
                .recipeReviewId(1L)
                .nickname("user")
                .profileUrl("https://s3.test.com/profile.png")
                .content("좋아용!")
                .createdAt(LocalDateTime.of(2023, 10, 10, 10, 30, 50))
                .build();
        given(recipeReviewService.getReviewDetail(anyLong()))
                .willReturn(recipeReviewResponseDto);

        //when & then
        mockMvc.perform(get("/review/recipe/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeId").value(recipeReviewResponseDto.getRecipeId()))
                .andExpect(jsonPath("$.recipeReviewId").value(recipeReviewResponseDto.getRecipeReviewId()))
                .andExpect(jsonPath("$.nickname").value(recipeReviewResponseDto.getNickname()))
                .andExpect(jsonPath("$.profileUrl").value(recipeReviewResponseDto.getProfileUrl()))
                .andExpect(jsonPath("$.content").value(recipeReviewResponseDto.getContent()))
                .andExpect(jsonPath("$.createdAt").value("2023-10-10 10:30:50"));
    }

    @Test
    @WithCustomMockUser
    @DisplayName("꿀조합 후기 수정 성공")
    void success_updateRecipeReview() throws Exception {
        //when & then
        String cookImageName = "foodlier_logo.png";
        File file = new File("src/test/resources/foodlier_logo.png");

        RecipeReviewForm form = RecipeReviewForm.builder()
                .content("최고의 극찬")
                .star(5)
                .cookImage(new MockMultipartFile(cookImageName,
                        cookImageName, MediaType.IMAGE_PNG_VALUE, new FileInputStream(file)))
                .build();

        mockMvc.perform(multipart("/review/recipe/1")
                        .file("cookImage", form.getCookImage().getBytes())
                        .param("content", form.getContent())
                        .param("star", String.valueOf(form.getStar()))
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }).with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("꿀조합 후기가 수정되었습니다."));

        ArgumentCaptor<RecipeReviewForm> captor = ArgumentCaptor.forClass(RecipeReviewForm.class);
        verify(recipeReviewFacade, times(1))
                .updateRecipeReview(eq(1L), eq(1L), captor.capture());
        RecipeReviewForm expectedForm = captor.getValue();

        assertAll(
                () -> assertEquals(form.getContent(), expectedForm.getContent()),
                () -> assertEquals(form.getStar(), expectedForm.getStar()),
                () -> assertEquals(form.getCookImage().getSize(), expectedForm.getCookImage().getSize())
        );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("꿀조합 후기 삭제 성공")
    void success_deleteRecipeReview() throws Exception {
        //when & then
        mockMvc.perform(delete("/review/recipe/1").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("꿀조합 후기가 삭제되었습니다."));

        verify(recipeReviewFacade, times(1)).deleteRecipeReview(eq(1L), eq(1L));
    }

}