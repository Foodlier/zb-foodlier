package com.zerobase.foodlier.module.recipe.service.recipe.data;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.module.recipe.domain.document.RecipeDocument;
import com.zerobase.foodlier.module.recipe.dto.recipe.RecipeCardDto;
import com.zerobase.foodlier.module.recipe.dto.recipe.RecipeSearchRequest;
import com.zerobase.foodlier.module.recipe.type.OrderType;
import com.zerobase.foodlier.module.recipe.type.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DummyDataGenerator {

    public static PageImpl<RecipeDocument> getRecipeDocumentByTitleAndCreateAt() {
        return new PageImpl<>(new ArrayList<>(Arrays.asList(
                RecipeDocument.builder()
                        .id(3L)
                        .ingredients("양파 당근 돼지고기 김치")
                        .title("김치제육볶음")
                        .content("제육볶음과 김치를 함께")
                        .createAt(LocalDateTime.now())
                        .numberOfHeart(0)
                        .numberOfComment(0)
                        .writer("세체요가 되고 싶은 요리사")
                        .mainImageUrl("http://mainImageUrl3.com")
                        .build(),
                RecipeDocument.builder()
                        .id(2L)
                        .ingredients("양파 당근 돼지고기 치즈")
                        .title("치즈제육볶음")
                        .content("치즈를 곁들인 제육볶음")
                        .createAt(LocalDateTime.now().minusDays(1))
                        .numberOfHeart(0)
                        .numberOfComment(0)
                        .writer("세체요가 되고 싶은 요리사")
                        .mainImageUrl("http://mainImageUrl3.com")
                        .build(),
                RecipeDocument.builder()
                        .id(1L)
                        .ingredients("양파 당근 돼지고기")
                        .title("제육볶음")
                        .content("초간단 제육볶음")
                        .createAt(LocalDateTime.now().minusDays(2))
                        .numberOfHeart(0)
                        .numberOfComment(0)
                        .writer("세체요")
                        .mainImageUrl("http://mainImageUrl3.com")
                        .build()
        )));
    }

    public static PageImpl<RecipeDocument> getRecipeDocumentByTitleAndCommentCount() {
        return new PageImpl<>(new ArrayList<>(Arrays.asList(
                RecipeDocument.builder()
                        .id(3L)
                        .ingredients("양파 당근 돼지고기 김치")
                        .title("김치제육볶음")
                        .content("제육볶음과 김치를 함께")
                        .createAt(LocalDateTime.now())
                        .numberOfHeart(0)
                        .numberOfComment(6)
                        .writer("세체요가 되고 싶은 요리사")
                        .mainImageUrl("http://mainImageUrl3.com")
                        .build(),
                RecipeDocument.builder()
                        .id(2L)
                        .ingredients("양파 당근 돼지고기 치즈")
                        .title("치즈제육볶음")
                        .content("치즈를 곁들인 제육볶음")
                        .createAt(LocalDateTime.now().minusDays(1))
                        .numberOfHeart(0)
                        .numberOfComment(2)
                        .writer("세체요가 되고 싶은 요리사")
                        .mainImageUrl("http://mainImageUrl3.com")
                        .build(),
                RecipeDocument.builder()
                        .id(1L)
                        .ingredients("양파 당근 돼지고기")
                        .title("제육볶음")
                        .content("초간단 제육볶음")
                        .createAt(LocalDateTime.now().minusDays(2))
                        .numberOfHeart(0)
                        .numberOfComment(0)
                        .writer("세체요")
                        .mainImageUrl("http://mainImageUrl3.com")
                        .build()
        )));
    }

    public static PageImpl<RecipeDocument> getRecipeDocumentByTitleAndHeartCount() {
        return new PageImpl<>(new ArrayList<>(Arrays.asList(
                RecipeDocument.builder()
                        .id(3L)
                        .ingredients("양파 당근 돼지고기 김치")
                        .title("김치제육볶음")
                        .content("제육볶음과 김치를 함께")
                        .createAt(LocalDateTime.now())
                        .numberOfHeart(6)
                        .numberOfComment(0)
                        .writer("세체요가 되고 싶은 요리사")
                        .mainImageUrl("http://mainImageUrl3.com")
                        .build(),
                RecipeDocument.builder()
                        .id(2L)
                        .ingredients("양파 당근 돼지고기 치즈")
                        .title("치즈제육볶음")
                        .content("치즈를 곁들인 제육볶음")
                        .createAt(LocalDateTime.now().minusDays(1))
                        .numberOfHeart(2)
                        .numberOfComment(0)
                        .writer("세체요가 되고 싶은 요리사")
                        .mainImageUrl("http://mainImageUrl3.com")
                        .build(),
                RecipeDocument.builder()
                        .id(1L)
                        .ingredients("양파 당근 돼지고기")
                        .title("제육볶음")
                        .content("초간단 제육볶음")
                        .mainImageUrl("http://mainImageUrl3.com")
                        .createAt(LocalDateTime.now().minusDays(2))
                        .numberOfHeart(0)
                        .numberOfComment(0)
                        .writer("세체요")

                        .build()
        )));
    }

    public static PageImpl<RecipeDocument> getRecipeDocumentByWriterAndHeartCount() {
        return new PageImpl<>(new ArrayList<>(Arrays.asList(
                RecipeDocument.builder()
                        .id(3L)
                        .ingredients("양파 당근 돼지고기 김치")
                        .title("김치제육볶음")
                        .content("제육볶음과 김치를 함께")
                        .mainImageUrl("http://mainImageUrl3.com")
                        .createAt(LocalDateTime.now())
                        .numberOfHeart(6)
                        .numberOfComment(0)
                        .writer("세체요가 되고 싶은 요리사")
                        .build(),
                RecipeDocument.builder()
                        .id(2L)
                        .ingredients("양파 당근 돼지고기 치즈")
                        .title("치즈제육볶음")
                        .content("치즈를 곁들인 제육볶음")
                        .mainImageUrl("http://mainImageUrl2.com")
                        .createAt(LocalDateTime.now())
                        .numberOfHeart(2)
                        .numberOfComment(0)
                        .writer("세체요가 되고 싶은 요리사")
                        .build()
        )));
    }

    public static PageImpl<RecipeDocument> getRecipeDocumentByWriterAndCommentCount() {
        return new PageImpl<>(new ArrayList<>(Arrays.asList(
                RecipeDocument.builder()
                        .id(3L)
                        .ingredients("양파 당근 돼지고기 김치")
                        .title("김치제육볶음")
                        .content("제육볶음과 김치를 함께")
                        .mainImageUrl("http://mainImageUrl3.com")
                        .createAt(LocalDateTime.now())
                        .numberOfHeart(0)
                        .numberOfComment(6)
                        .writer("세체요가 되고 싶은 요리사")
                        .build(),
                RecipeDocument.builder()
                        .id(2L)
                        .ingredients("양파 당근 돼지고기 치즈")
                        .title("치즈제육볶음")
                        .content("치즈를 곁들인 제육볶음")
                        .mainImageUrl("http://mainImageUrl2.com")
                        .createAt(LocalDateTime.now())
                        .numberOfHeart(0)
                        .numberOfComment(2)
                        .writer("세체요가 되고 싶은 요리사")
                        .build()
        )));
    }

    public static PageImpl<RecipeDocument> getRecipeDocumentByWriterAndCreatedAt() {
        return new PageImpl<>(new ArrayList<>(Arrays.asList(
                RecipeDocument.builder()
                        .id(3L)
                        .ingredients("양파 당근 돼지고기 김치")
                        .title("김치제육볶음")
                        .content("제육볶음과 김치를 함께")
                        .mainImageUrl("http://mainImageUrl3.com")
                        .createAt(LocalDateTime.now())
                        .numberOfHeart(0)
                        .numberOfComment(0)
                        .writer("세체요가 되고 싶은 요리사")
                        .build(),
                RecipeDocument.builder()
                        .id(2L)
                        .ingredients("양파 당근 돼지고기 치즈")
                        .title("치즈제육볶음")
                        .content("치즈를 곁들인 제육볶음")
                        .mainImageUrl("http://mainImageUrl2.com")
                        .createAt(LocalDateTime.now())
                        .numberOfHeart(0)
                        .numberOfComment(0)
                        .writer("세체요가 되고 싶은 요리사")
                        .build()
        )));
    }

    public static PageImpl<RecipeDocument> getRecipeDocumentByIngredientsAndCreateAt() {
        return new PageImpl<>(new ArrayList<>(Arrays.asList(
                RecipeDocument.builder()
                        .id(3L)
                        .ingredients("양파 당근 돼지고기 김치")
                        .title("김치제육볶음")
                        .content("제육볶음과 김치를 함께")
                        .createAt(LocalDateTime.now())
                        .numberOfHeart(0)
                        .numberOfComment(0)
                        .writer("세체요가 되고 싶은 요리사")
                        .mainImageUrl("http://mainImageUrl3.com")
                        .build(),
                RecipeDocument.builder()
                        .id(2L)
                        .ingredients("양파 당근 돼지고기 치즈")
                        .title("치즈제육볶음")
                        .content("치즈를 곁들인 제육볶음")
                        .createAt(LocalDateTime.now().minusDays(1))
                        .numberOfHeart(0)
                        .numberOfComment(0)
                        .writer("세체요가 되고 싶은 요리사")
                        .mainImageUrl("http://mainImageUrl3.com")
                        .build(),
                RecipeDocument.builder()
                        .id(1L)
                        .ingredients("양파 당근 돼지고기")
                        .title("제육볶음")
                        .content("초간단 제육볶음")
                        .createAt(LocalDateTime.now().minusDays(2))
                        .numberOfHeart(0)
                        .numberOfComment(0)
                        .writer("세체요")
                        .mainImageUrl("http://mainImageUrl3.com")
                        .build()
        )));
    }

    public static PageImpl<RecipeDocument> getRecipeDocumentByIngredientsAndCommentCount() {
        return new PageImpl<>(new ArrayList<>(Arrays.asList(
                RecipeDocument.builder()
                        .id(3L)
                        .ingredients("양파 당근 돼지고기 김치")
                        .title("김치제육볶음")
                        .content("제육볶음과 김치를 함께")
                        .createAt(LocalDateTime.now())
                        .numberOfHeart(0)
                        .numberOfComment(6)
                        .writer("세체요가 되고 싶은 요리사")
                        .mainImageUrl("http://mainImageUrl3.com")
                        .build(),
                RecipeDocument.builder()
                        .id(2L)
                        .ingredients("양파 당근 돼지고기 치즈")
                        .title("치즈제육볶음")
                        .content("치즈를 곁들인 제육볶음")
                        .createAt(LocalDateTime.now().minusDays(1))
                        .numberOfHeart(0)
                        .numberOfComment(2)
                        .writer("세체요가 되고 싶은 요리사")
                        .mainImageUrl("http://mainImageUrl3.com")
                        .build(),
                RecipeDocument.builder()
                        .id(1L)
                        .ingredients("양파 당근 돼지고기")
                        .title("제육볶음")
                        .content("초간단 제육볶음")
                        .createAt(LocalDateTime.now().minusDays(2))
                        .numberOfHeart(0)
                        .numberOfComment(0)
                        .writer("세체요")
                        .mainImageUrl("http://mainImageUrl3.com")
                        .build()
        )));
    }

    public static PageImpl<RecipeDocument> getRecipeDocumentByIngredientsAndHeartCount() {
        return new PageImpl<>(new ArrayList<>(Arrays.asList(
                RecipeDocument.builder()
                        .id(3L)
                        .ingredients("양파 당근 돼지고기 김치")
                        .title("김치제육볶음")
                        .content("제육볶음과 김치를 함께")
                        .createAt(LocalDateTime.now())
                        .numberOfHeart(6)
                        .numberOfComment(0)
                        .writer("세체요가 되고 싶은 요리사")
                        .mainImageUrl("http://mainImageUrl3.com")
                        .build(),
                RecipeDocument.builder()
                        .id(2L)
                        .ingredients("양파 당근 돼지고기 치즈")
                        .title("치즈제육볶음")
                        .content("치즈를 곁들인 제육볶음")
                        .createAt(LocalDateTime.now().minusDays(1))
                        .numberOfHeart(2)
                        .numberOfComment(0)
                        .writer("세체요가 되고 싶은 요리사")
                        .mainImageUrl("http://mainImageUrl3.com")
                        .build(),
                RecipeDocument.builder()
                        .id(1L)
                        .ingredients("양파 당근 돼지고기")
                        .title("제육볶음")
                        .content("초간단 제육볶음")
                        .mainImageUrl("http://mainImageUrl3.com")
                        .createAt(LocalDateTime.now().minusDays(2))
                        .numberOfHeart(0)
                        .numberOfComment(0)
                        .writer("세체요")

                        .build()
        )));
    }




    public static ListResponse<RecipeCardDto> getListResponse(Page<RecipeDocument> searchResult) {
        return ListResponse.<RecipeCardDto>builder()
                .hasNextPage(searchResult.hasNext())
                .totalElements(searchResult.getTotalElements())
                .totalPages(searchResult.getTotalPages())
                .content(searchResult.stream()
                        .map(recipeDocument -> RecipeCardDto.builder()
                                .id(recipeDocument.getId())
                                .title(recipeDocument.getTitle())
                                .content(recipeDocument.getContent())
                                .nickName(recipeDocument.getWriter())
                                .mainImageUrl(recipeDocument.getMainImageUrl())
                                .heartCount((int) recipeDocument.getNumberOfHeart())
                                .isHeart(false)
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public static RecipeSearchRequest getRecipeSearchRequest(SearchType searchType,
                                                              OrderType orderType,
                                                              String searchText) {

        return RecipeSearchRequest.builder()
                .memberId(1L)
                .searchText(searchText)
                .searchType(searchType)
                .orderType(orderType)
                .pageable(PageRequest.of(0, 10))
                .build();

    }


}
