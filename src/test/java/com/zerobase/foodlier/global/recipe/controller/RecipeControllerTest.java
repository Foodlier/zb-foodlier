package com.zerobase.foodlier.global.recipe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.config.SecurityConfig;
import com.zerobase.foodlier.global.notification.facade.NotificationFacade;
import com.zerobase.foodlier.global.recipe.facade.RecipeFacade;
import com.zerobase.foodlier.mockuser.WithCustomMockUser;
import com.zerobase.foodlier.module.heart.service.HeartService;
import com.zerobase.foodlier.module.recipe.domain.type.Difficulty;
import com.zerobase.foodlier.module.recipe.dto.recipe.*;
import com.zerobase.foodlier.module.recipe.service.recipe.RecipeService;
import com.zerobase.foodlier.module.recipe.type.SearchType;
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
import org.springframework.test.web.servlet.ResultActions;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = RecipeController.class, excludeFilters =
@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
class RecipeControllerTest {

    @MockBean
    private RecipeFacade recipeFacade;
    @MockBean
    private RecipeService recipeService;
    @MockBean
    private HeartService heartService;
    @MockBean
    private NotificationFacade notificationFacade;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithCustomMockUser
    @DisplayName("레시피 이미지 업로드 성공")
    void success_upload_recipe_image() throws Exception {
        //given
        String mainImageName = "foodlier_logo.png";
        File file = new File("src/test/resources/foodlier_logo.png");
        String cookingOrderImageName = "foodlier_logo.png";
        MockMultipartFile mainImage = new MockMultipartFile(mainImageName,
                mainImageName,
                MediaType.IMAGE_PNG_VALUE, new FileInputStream(file));
        MockMultipartFile cookingOrderImage1 = new MockMultipartFile(cookingOrderImageName,
                cookingOrderImageName,
                MediaType.IMAGE_PNG_VALUE, new FileInputStream(file));
        MockMultipartFile cookingOrderImage2 = new MockMultipartFile(cookingOrderImageName,
                cookingOrderImageName,
                MediaType.IMAGE_PNG_VALUE, new FileInputStream(file));
        List<MockMultipartFile> cookingOrderImageList = new ArrayList<>(
                List.of(cookingOrderImage1, cookingOrderImage2)
        );

        given(recipeFacade.uploadRecipeImage(any(), any()))
                .willReturn(RecipeImageResponse.builder()
                        .mainImage("http://s3.test/mainimage")
                        .cookingOrderImageList(new ArrayList<>(
                                List.of("http://s3.test/cookingorderimage1",
                                        "http://s3.test/cookingorderimage2")))
                        .build());

        //when
        ResultActions perform = mockMvc.perform(multipart("/recipe/image")
                .file("mainImage", mainImage.getBytes())
                .file("cookingOrderImageList", cookingOrderImageList.get(0).getBytes())
                .file("cookingOrderImageList", cookingOrderImageList.get(1).getBytes())
                .with(request -> {
                    request.setMethod("POST");
                    return request;
                }).with(csrf())
                .contentType(MediaType.MULTIPART_FORM_DATA));

        //then
        verify(recipeFacade, times(1)).uploadRecipeImage(any(), any());
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mainImage")
                        .value("http://s3.test/mainimage"))
                .andExpect(jsonPath("$.cookingOrderImageList.[0]")
                        .value("http://s3.test/cookingorderimage1"))
                .andExpect(jsonPath("$.cookingOrderImageList.[1]")
                        .value("http://s3.test/cookingorderimage2"));

    }

    @Test
    @WithCustomMockUser
    @DisplayName("레시피 이미지 수정 성공")
    void success_update_recipe_image() throws Exception {
        //given
        String mainImageName = "foodlier_logo.png";
        File file = new File("src/test/resources/foodlier_logo.png");
        String cookingOrderImageName = "foodlier_logo.png";
        MockMultipartFile mainImage = new MockMultipartFile(mainImageName,
                mainImageName,
                MediaType.IMAGE_PNG_VALUE, new FileInputStream(file));
        MockMultipartFile cookingOrderImage1 = new MockMultipartFile(cookingOrderImageName,
                cookingOrderImageName,
                MediaType.IMAGE_PNG_VALUE, new FileInputStream(file));
        MockMultipartFile cookingOrderImage2 = new MockMultipartFile(cookingOrderImageName,
                cookingOrderImageName,
                MediaType.IMAGE_PNG_VALUE, new FileInputStream(file));
        List<MockMultipartFile> cookingOrderImageList = new ArrayList<>(
                List.of(cookingOrderImage1, cookingOrderImage2)
        );

        given(recipeFacade.updateRecipeImage(any(), any(), any(), any()))
                .willReturn(RecipeImageResponse.builder()
                        .mainImage("http://s3.test/mainimage")
                        .cookingOrderImageList(new ArrayList<>(
                                List.of("http://s3.test/cookingorderimage1",
                                        "http://s3.test/cookingorderimage2")))
                        .build());

        long recipeId = 2L;

        //when
        ResultActions perform = mockMvc.perform(multipart("/recipe/image/{recipeId}", recipeId)
                .file("mainImage", mainImage.getBytes())
                .file("cookingOrderImageList", cookingOrderImageList.get(0).getBytes())
                .file("cookingOrderImageList", cookingOrderImageList.get(1).getBytes())
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                }).with(csrf())
                .contentType(MediaType.MULTIPART_FORM_DATA));

        //then
        verify(recipeFacade, times(1))
                .updateRecipeImage(any(), any(), any(), any());
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mainImage")
                        .value("http://s3.test/mainimage"))
                .andExpect(jsonPath("$.cookingOrderImageList.[0]")
                        .value("http://s3.test/cookingorderimage1"))
                .andExpect(jsonPath("$.cookingOrderImageList.[1]")
                        .value("http://s3.test/cookingorderimage2"));

    }

    @Test
    @WithCustomMockUser
    @DisplayName("게시글 작성 성공")
    void success_create_recipe() throws Exception {
        //given
        RecipeDtoRequest recipeDtoRequest = RecipeDtoRequest.builder()
                .title("title")
                .mainImageUrl("http://s3.test.com/mainimage")
                .content("content")
                .difficulty(Difficulty.EASY)
                .expectedTime(30)
                .recipeDetailDtoList(new ArrayList<>(List.of(RecipeDetailDto.builder()
                        .cookingOrder("order1")
                        .cookingOrderImageUrl("http://s3.test.com/cookingorderimage")
                        .build())))
                .recipeIngredientDtoList(new ArrayList<>(List.of(RecipeIngredientDto.builder()
                        .count(1)
                        .name("name")
                        .unit("ea")
                        .build())))
                .build();

        //when
        ResultActions perform = mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeDtoRequest))
                .with(csrf()));

        //then
        ArgumentCaptor<RecipeDtoRequest> captor = ArgumentCaptor.forClass(RecipeDtoRequest.class);
        verify(recipeFacade, times(1))
                .createRecipe(any(), captor.capture());
        RecipeDtoRequest captorValue = captor.getValue();

        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("게시글 작성을 성공했습니다."));

        assertAll(
                () -> assertEquals(recipeDtoRequest.getTitle(), captorValue.getTitle()),
                () -> assertEquals(recipeDtoRequest.getMainImageUrl(),
                        captorValue.getMainImageUrl()),
                () -> assertEquals(recipeDtoRequest.getContent(),
                        captorValue.getContent()),
                () -> assertEquals(recipeDtoRequest.getDifficulty(),
                        captorValue.getDifficulty()),
                () -> assertEquals(recipeDtoRequest.getExpectedTime(),
                        captorValue.getExpectedTime()),
                () -> assertEquals(recipeDtoRequest.getRecipeDetailDtoList()
                                .get(0).getCookingOrder(),
                        captorValue.getRecipeDetailDtoList()
                                .get(0).getCookingOrder()),
                () -> assertEquals(recipeDtoRequest.getRecipeDetailDtoList()
                                .get(0).getCookingOrderImageUrl(),
                        captorValue.getRecipeDetailDtoList()
                                .get(0).getCookingOrderImageUrl()),
                () -> assertEquals(recipeDtoRequest.getRecipeIngredientDtoList()
                                .get(0).getCount(),
                        captorValue.getRecipeIngredientDtoList()
                                .get(0).getCount()),
                () -> assertEquals(recipeDtoRequest.getRecipeIngredientDtoList()
                                .get(0).getUnit(),
                        captorValue.getRecipeIngredientDtoList()
                                .get(0).getUnit()),
                () -> assertEquals(recipeDtoRequest.getRecipeIngredientDtoList()
                                .get(0).getName(),
                        captorValue.getRecipeIngredientDtoList()
                                .get(0).getName()));
    }

    @Test
    @WithCustomMockUser
    @DisplayName("게시글 수정 성공")
    void success_update_recipe() throws Exception {
        //given
        RecipeDtoRequest recipeDtoRequest = RecipeDtoRequest.builder()
                .title("title")
                .mainImageUrl("http://s3.test.com/mainimage")
                .content("content")
                .difficulty(Difficulty.EASY)
                .expectedTime(30)
                .recipeDetailDtoList(new ArrayList<>(List.of(RecipeDetailDto.builder()
                        .cookingOrder("order1")
                        .cookingOrderImageUrl("http://s3.test.com/cookingorderimage")
                        .build())))
                .recipeIngredientDtoList(new ArrayList<>(List.of(RecipeIngredientDto.builder()
                        .count(1)
                        .name("name")
                        .unit("ea")
                        .build())))
                .build();
        Long recipeId = 1L;

        //when
        ResultActions perform = mockMvc.perform(put("/recipe/{recipeId}",
                recipeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipeDtoRequest))
                .with(csrf()));

        //then
        ArgumentCaptor<RecipeDtoRequest> captor = ArgumentCaptor.forClass(RecipeDtoRequest.class);
        verify(recipeFacade, times(1))
                .updateRecipe(any(), captor.capture(), any());
        RecipeDtoRequest captorValue = captor.getValue();

        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("게시글 수정을 성공했습니다."));

        assertAll(
                () -> assertEquals(recipeDtoRequest.getTitle(), captorValue.getTitle()),
                () -> assertEquals(recipeDtoRequest.getMainImageUrl(),
                        captorValue.getMainImageUrl()),
                () -> assertEquals(recipeDtoRequest.getContent(),
                        captorValue.getContent()),
                () -> assertEquals(recipeDtoRequest.getDifficulty(),
                        captorValue.getDifficulty()),
                () -> assertEquals(recipeDtoRequest.getExpectedTime(),
                        captorValue.getExpectedTime()),
                () -> assertEquals(recipeDtoRequest.getRecipeDetailDtoList()
                                .get(0).getCookingOrder(),
                        captorValue.getRecipeDetailDtoList()
                                .get(0).getCookingOrder()),
                () -> assertEquals(recipeDtoRequest.getRecipeDetailDtoList()
                                .get(0).getCookingOrderImageUrl(),
                        captorValue.getRecipeDetailDtoList()
                                .get(0).getCookingOrderImageUrl()),
                () -> assertEquals(recipeDtoRequest.getRecipeIngredientDtoList()
                                .get(0).getCount(),
                        captorValue.getRecipeIngredientDtoList()
                                .get(0).getCount()),
                () -> assertEquals(recipeDtoRequest.getRecipeIngredientDtoList()
                                .get(0).getUnit(),
                        captorValue.getRecipeIngredientDtoList()
                                .get(0).getUnit()),
                () -> assertEquals(recipeDtoRequest.getRecipeIngredientDtoList()
                                .get(0).getName(),
                        captorValue.getRecipeIngredientDtoList()
                                .get(0).getName()));
    }

    @Test
    @WithCustomMockUser
    @DisplayName("레시피 가져오기 성공")
    void success_get_recipe() throws Exception {
        //given
        Long recipeId = 1L;
        Long memberId = 1L;
        RecipeDtoResponse recipeDtoResponse = RecipeDtoResponse.builder()
                .recipeId(recipeId)
                .memberId(memberId)
                .nickname("nickname")
                .profileUrl("http://s3.test.com/profile")
                .title("title")
                .content("content")
                .mainImageUrl("http://s3.test.com/mainimage")
                .expectedTime(30)
                .heartCount(100)
                .difficulty(Difficulty.HARD)
                .recipeDetailDtoList(new ArrayList<>(List.of(
                        RecipeDetailDto.builder()
                                .cookingOrderImageUrl("http://s3.test.com/cookingorderimage")
                                .cookingOrder("cookingorder")
                                .build())))
                .recipeIngredientDtoList(new ArrayList<>(List.of(
                        RecipeIngredientDto.builder()
                                .unit("ea")
                                .count(3)
                                .name("ingredient")
                                .build()
                )))
                .isHeart(false)
                .build();

        given(recipeService.getRecipeDetail(any(), any()))
                .willReturn(recipeDtoResponse);

        //when
        ResultActions perform = mockMvc.perform(get("/recipe/{recipeId}",
                recipeId));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeId")
                        .value(recipeDtoResponse.getRecipeId()))
                .andExpect(jsonPath("$.memberId")
                        .value(recipeDtoResponse.getMemberId()))
                .andExpect(jsonPath("$.nickname")
                        .value(recipeDtoResponse.getNickname()))
                .andExpect(jsonPath("$.profileUrl")
                        .value(recipeDtoResponse.getProfileUrl()))
                .andExpect(jsonPath("$.title")
                        .value(recipeDtoResponse.getTitle()))
                .andExpect(jsonPath("$.content")
                        .value(recipeDtoResponse.getContent()))
                .andExpect(jsonPath("$.mainImageUrl")
                        .value(recipeDtoResponse.getMainImageUrl()))
                .andExpect(jsonPath("$.expectedTime")
                        .value(recipeDtoResponse.getExpectedTime()))
                .andExpect(jsonPath("$.heartCount")
                        .value(recipeDtoResponse.getHeartCount()))
                .andExpect(jsonPath("$.difficulty")
                        .value(recipeDtoResponse.getDifficulty().toString()))
                .andExpect(jsonPath("$.recipeDetailDtoList.[0]" +
                        ".cookingOrderImageUrl")
                        .value(recipeDtoResponse.getRecipeDetailDtoList()
                                .get(0).getCookingOrderImageUrl()))
                .andExpect(jsonPath("$.recipeDetailDtoList.[0]" +
                        ".cookingOrder")
                        .value(recipeDtoResponse.getRecipeDetailDtoList()
                                .get(0).getCookingOrder()))
                .andExpect(jsonPath("$.recipeIngredientDtoList.[0]" +
                        ".unit")
                        .value(recipeDtoResponse.getRecipeIngredientDtoList()
                                .get(0).getUnit()))
                .andExpect(jsonPath("$.recipeIngredientDtoList.[0]" +
                        ".count")
                        .value(recipeDtoResponse.getRecipeIngredientDtoList()
                                .get(0).getCount()))
                .andExpect(jsonPath("$.recipeIngredientDtoList.[0]" +
                        ".name")
                        .value(recipeDtoResponse.getRecipeIngredientDtoList()
                                .get(0).getName()))
                .andExpect(jsonPath("$.heart")
                        .value(recipeDtoResponse.isHeart()));

    }

    @Test
    @WithCustomMockUser
    @DisplayName("레시피 삭제 성공")
    void success_delete_recipe() throws Exception {
        //given
        Long recipeId = 1L;
        Long id = 1L;

        //when
        ResultActions perform = mockMvc.perform(delete("/recipe/{recipeId}",
                recipeId).with(csrf()));

        //then
        verify(recipeFacade, times(1)).deleteRecipe(id, recipeId);
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("레시피 삭제를 성공했습니다."));
    }

    @Test
    @WithCustomMockUser
    @DisplayName("레시피 검색하기")
    void success_get_recipe_list() throws Exception {
        //given
        SearchType searchType = SearchType.TITLE;
        int pageIdx = 0;
        int pageSize = 2;
        String searchText = "찜닭";
        RecipeSearchRequest recipeSearchRequest = RecipeSearchRequest.builder().build();
        RecipeCardDto recipeCardDto = RecipeCardDto.builder()
                .recipeId(1L)
                .nickName("요리왕비룡")
                .title("간장찜닭")
                .mainImageUrl("http://s3.test.com/mainimage")
                .content("맛있는 간장찜닭")
                .heartCount(1000)
                .isHeart(true)
                .build();

        given(recipeService.getRecipeList(any()))
                .willReturn(ListResponse.from(
                        new PageImpl<>(
                                List.of(
                                        recipeCardDto
                                )
                        )
                ));

        //when
        ResultActions perform = mockMvc.perform(get("/recipe/search/" +
                        "{searchType}/{pageIdx}/{pageSize}?searchText={searchText}",
                searchType, pageIdx, pageSize, searchText));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].recipeId")
                        .value(recipeCardDto.getRecipeId()));


    }
}