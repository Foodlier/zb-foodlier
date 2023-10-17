package com.zerobase.foodlier.global.quotation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.config.SecurityConfig;
import com.zerobase.foodlier.global.quotation.facade.QuotationFacade;
import com.zerobase.foodlier.module.recipe.domain.type.Difficulty;
import com.zerobase.foodlier.module.recipe.dto.quotation.QuotationDtoRequest;
import com.zerobase.foodlier.module.recipe.dto.quotation.QuotationTopResponse;
import com.zerobase.foodlier.module.recipe.dto.recipe.RecipeDetailDto;
import com.zerobase.foodlier.module.recipe.dto.recipe.RecipeDtoRequest;
import com.zerobase.foodlier.module.recipe.dto.recipe.RecipeIngredientDto;
import com.zerobase.foodlier.module.recipe.service.quotation.QuotationService;
import com.zerobase.foodlier.mockuser.WithCustomMockUser;
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
import org.springframework.test.web.servlet.MockMvc;

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

@WebMvcTest(value = QuotationController.class, excludeFilters =
@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class}))
class QuotationControllerTest {

    @MockBean
    private QuotationService quotationService;

    @MockBean
    private QuotationFacade quotationFacade;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    @DisplayName("견적서 작성 성공")
    @WithCustomMockUser
    void success_createQuotation() throws Exception {
        //when & then
        QuotationDtoRequest request = QuotationDtoRequest.builder()
                .title("제육볶음")
                .content("아주 맛있는 제육볶음 견적서")
                .recipeIngredientDtoList(
                        List.of(
                                RecipeIngredientDto.builder()
                                        .name("앞다리살")
                                        .count(1)
                                        .unit("근")
                                        .build()
                        )
                )
                .difficulty(Difficulty.MEDIUM)
                .recipeDetailDtoList(
                        List.of(
                                "프라이팬에 넣고 볶아줍니다."
                        )
                )
                .expectedTime(10)
                .build();

        mockMvc.perform(post("/quotation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("견적서가 작성되었습니다."));

        ArgumentCaptor<QuotationDtoRequest> captor = ArgumentCaptor.forClass(QuotationDtoRequest.class);
        verify(quotationService, times(1)).createQuotation(eq(1L), captor.capture());

        QuotationDtoRequest expectedRequest = captor.getValue();

        assertAll(
                () -> assertEquals(request.getTitle(), expectedRequest.getTitle()),
                () -> assertEquals(request.getContent(), expectedRequest.getContent()),
                () -> assertEquals(request.getRecipeIngredientDtoList().get(0).getName(), expectedRequest.getRecipeIngredientDtoList().get(0).getName()),
                () -> assertEquals(request.getRecipeIngredientDtoList().get(0).getCount(), expectedRequest.getRecipeIngredientDtoList().get(0).getCount()),
                () -> assertEquals(request.getRecipeIngredientDtoList().get(0).getUnit(), expectedRequest.getRecipeIngredientDtoList().get(0).getUnit()),
                () -> assertEquals(request.getDifficulty(), expectedRequest.getDifficulty()),
                () -> assertEquals(request.getRecipeDetailDtoList().get(0), expectedRequest.getRecipeDetailDtoList().get(0)),
                () -> assertEquals(request.getExpectedTime(), expectedRequest.getExpectedTime())
        );

    }

    @Test
    @WithCustomMockUser
    @DisplayName("견적서 전송 성공")
    void success_sendQuotation() throws Exception {
        //when & then
        mockMvc.perform(post("/quotation/send?quotationId=2&requestId=3").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("견적서를 보냈습니다."));

        verify(quotationFacade, times(1)).sendQuotation(1L, 2L, 3L);
    }

    @Test
    @WithCustomMockUser
    @DisplayName("견적서 목록 조회 For 냉장고를 부탁해")
    void success_getQuotationListForRefrigerator() throws Exception {
        //given
        QuotationTopResponse quotation = QuotationTopResponse.builder()
                .quotationId(1L)
                .title("견적서")
                .content("내용")
                .difficulty(Difficulty.EASY)
                .expectedTime(25)
                .build();

        given(quotationService.getQuotationListForRefrigerator(anyLong(), any()))
                .willReturn(ListResponse.from(
                        new PageImpl<>(
                                List.of(
                                    quotation
                                )
                        )
                ));

        //when & then
        mockMvc.perform(get("/quotation/0/10"))
                .andDo(print())
                .andExpect(jsonPath("$.content.[0].quotationId").value(quotation.getQuotationId()))
                .andExpect(jsonPath("$.content.[0].title").value(quotation.getTitle()))
                .andExpect(jsonPath("$.content.[0].content").value(quotation.getContent()))
                .andExpect(jsonPath("$.content.[0].difficulty").value(quotation.getDifficulty().name()))
                .andExpect(jsonPath("$.content.[0].expectedTime").value(quotation.getExpectedTime()));
    }

    @Test
    @WithCustomMockUser
    @DisplayName("견적서 목록 조회 For 꿀조합")
    void success_getQuotationListForRecipe() throws Exception {
        //given
        QuotationTopResponse quotation = QuotationTopResponse.builder()
                .quotationId(1L)
                .title("견적서")
                .content("내용")
                .difficulty(Difficulty.EASY)
                .expectedTime(25)
                .build();

        given(quotationService.getQuotationListForRecipe(anyLong(), any()))
                .willReturn(ListResponse.from(
                        new PageImpl<>(
                                List.of(
                                        quotation
                                )
                        )
                ));

        //when & then
        mockMvc.perform(get("/quotation/recipe/0/10"))
                .andDo(print())
                .andExpect(jsonPath("$.content.[0].quotationId").value(quotation.getQuotationId()))
                .andExpect(jsonPath("$.content.[0].title").value(quotation.getTitle()))
                .andExpect(jsonPath("$.content.[0].content").value(quotation.getContent()))
                .andExpect(jsonPath("$.content.[0].difficulty").value(quotation.getDifficulty().name()))
                .andExpect(jsonPath("$.content.[0].expectedTime").value(quotation.getExpectedTime()));
    }


    @Test
    @WithCustomMockUser
    @DisplayName("견적서 수정 성공")
    void success_updateQuotation() throws Exception {
        //when & then
        QuotationDtoRequest request = QuotationDtoRequest.builder()
                .title("제육볶음")
                .content("아주 맛있는 제육볶음 견적서")
                .recipeIngredientDtoList(
                        List.of(
                                RecipeIngredientDto.builder()
                                        .name("앞다리살")
                                        .count(1)
                                        .unit("근")
                                        .build()
                        )
                )
                .difficulty(Difficulty.MEDIUM)
                .recipeDetailDtoList(
                        List.of(
                                "프라이팬에 넣고 볶아줍니다."
                        )
                )
                .expectedTime(10)
                .build();

        mockMvc.perform(put("/quotation/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("견적서가 수정되었습니다."));

        ArgumentCaptor<QuotationDtoRequest> captor = ArgumentCaptor.forClass(QuotationDtoRequest.class);
        verify(quotationService, times(1)).updateQuotation(eq(1L), eq(1L), captor.capture());

        QuotationDtoRequest expectedRequest = captor.getValue();

        assertAll(
                () -> assertEquals(request.getTitle(), expectedRequest.getTitle()),
                () -> assertEquals(request.getContent(), expectedRequest.getContent()),
                () -> assertEquals(request.getRecipeIngredientDtoList().get(0).getName(), expectedRequest.getRecipeIngredientDtoList().get(0).getName()),
                () -> assertEquals(request.getRecipeIngredientDtoList().get(0).getCount(), expectedRequest.getRecipeIngredientDtoList().get(0).getCount()),
                () -> assertEquals(request.getRecipeIngredientDtoList().get(0).getUnit(), expectedRequest.getRecipeIngredientDtoList().get(0).getUnit()),
                () -> assertEquals(request.getDifficulty(), expectedRequest.getDifficulty()),
                () -> assertEquals(request.getRecipeDetailDtoList().get(0), expectedRequest.getRecipeDetailDtoList().get(0)),
                () -> assertEquals(request.getExpectedTime(), expectedRequest.getExpectedTime())
        );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("견적서를 꿀조합으로 변경")
    void success_recipifyQuotation() throws Exception {
        //when & then
        RecipeDtoRequest request = RecipeDtoRequest.builder()
                .title("제육볶음")
                .content("아주 맛있는 제육볶음")
                .mainImageUrl("https://s3.test.com/image.png")
                .recipeIngredientDtoList(
                        List.of(
                                RecipeIngredientDto.builder()
                                        .name("앞다리살")
                                        .count(1)
                                        .unit("근")
                                        .build()
                        )
                )
                .difficulty(Difficulty.MEDIUM)
                .recipeDetailDtoList(
                        List.of(
                                RecipeDetailDto.builder()
                                        .cookingOrderImageUrl("https://s3.test.com/cook.png")
                                        .cookingOrder("이리 저리 볶아 주면 끗!")
                                        .build()
                        )
                )
                .expectedTime(10)
                .build();

        mockMvc.perform(put("/quotation/recipify/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(csrf()))
                .andDo(print())
                .andExpect(content().string("견적서가 꿀조합으로 변환되었습니다."));

        ArgumentCaptor<RecipeDtoRequest> captor = ArgumentCaptor.forClass(RecipeDtoRequest.class);

        verify(quotationService, times(1)).convertToRecipe(eq(1L), eq(1L), captor.capture());

        RecipeDtoRequest expectedRequest = captor.getValue();

        assertAll(
                () -> assertEquals(request.getTitle(), expectedRequest.getTitle()),
                () -> assertEquals(request.getContent(), expectedRequest.getContent()),
                () -> assertEquals(request.getRecipeIngredientDtoList().get(0).getName(), expectedRequest.getRecipeIngredientDtoList().get(0).getName()),
                () -> assertEquals(request.getRecipeIngredientDtoList().get(0).getCount(), expectedRequest.getRecipeIngredientDtoList().get(0).getCount()),
                () -> assertEquals(request.getRecipeIngredientDtoList().get(0).getUnit(), expectedRequest.getRecipeIngredientDtoList().get(0).getUnit()),
                () -> assertEquals(request.getDifficulty(), expectedRequest.getDifficulty()),
                () -> assertEquals(request.getRecipeDetailDtoList().get(0).getCookingOrderImageUrl(), expectedRequest.getRecipeDetailDtoList().get(0).getCookingOrderImageUrl()),
                () -> assertEquals(request.getRecipeDetailDtoList().get(0).getCookingOrder(), expectedRequest.getRecipeDetailDtoList().get(0).getCookingOrder()),
                () -> assertEquals(request.getExpectedTime(), expectedRequest.getExpectedTime()),
                () -> assertEquals(request.getMainImageUrl(), expectedRequest.getMainImageUrl())
        );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("견적서 삭제 성공")
    void success_deleteQuotation() throws Exception {
        //when & then
        mockMvc.perform(delete("/quotation/1").with(csrf()))
                .andDo(print())
                .andExpect(content().string("견적서가 삭제되었습니다."));

        verify(quotationService, times(1)).deleteQuotation(eq(1L), eq(1L));

    }

}