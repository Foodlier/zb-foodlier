package com.zerobase.foodlier.module.recipe.service.recipe;

import com.zerobase.foodlier.common.aop.RedissonLock;
import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.heart.reposiotry.HeartRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.recipe.domain.document.RecipeDocument;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.domain.vo.RecipeDetail;
import com.zerobase.foodlier.module.recipe.domain.vo.RecipeIngredient;
import com.zerobase.foodlier.module.recipe.domain.vo.RecipeStatistics;
import com.zerobase.foodlier.module.recipe.domain.vo.Summary;
import com.zerobase.foodlier.module.recipe.dto.recipe.*;
import com.zerobase.foodlier.module.recipe.exception.recipe.RecipeErrorCode;
import com.zerobase.foodlier.module.recipe.exception.recipe.RecipeException;
import com.zerobase.foodlier.module.recipe.repository.RecipeRepository;
import com.zerobase.foodlier.module.recipe.repository.RecipeSearchRepository;
import com.zerobase.foodlier.module.recipe.type.OrderType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.recipe.exception.recipe.RecipeErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeService {
    private static final String DELIMITER = " ";
    private final RecipeRepository recipeRepository;
    private final RecipeSearchRepository recipeSearchRepository;
    private final MemberRepository memberRepository;
    private final HeartRepository heartRepository;

    /**
     * 작성자: 황태원(이종욱)
     * 레시피 정보를 받아 레시피를 등록
     * 레시피 등록 시 레시피 검색을 위한 객체도 레시피 정보를 기반으로 저장
     * 작성일자: 2023-09-27
     */
    public void createRecipe(Member member, RecipeDtoRequest recipeDtoRequest) {
        Recipe recipe = Recipe.builder()
                .summary(Summary.builder()
                        .title(recipeDtoRequest.getTitle())
                        .content(recipeDtoRequest.getContent())
                        .build())
                .mainImageUrl(recipeDtoRequest.getMainImageUrl())
                .expectedTime(recipeDtoRequest.getExpectedTime())
                .recipeStatistics(new RecipeStatistics())
                .difficulty(recipeDtoRequest.getDifficulty())
                .isPublic(true)
                .member(member)
                .recipeIngredientList(recipeDtoRequest.getRecipeIngredientDtoList()
                        .stream()
                        .map(RecipeIngredientDto::toEntity)
                        .collect(Collectors.toList()))
                .recipeDetailList(recipeDtoRequest.getRecipeDetailDtoList()
                        .stream()
                        .map(RecipeDetailDto::toEntity)
                        .collect(Collectors.toList()))
                .build();
        recipeRepository.save(recipe);
        recipeSearchRepository.save(RecipeDocument.builder()
                .id(recipe.getId())
                .memberId(recipe.getMember().getId())
                .title(recipe.getSummary().getTitle())
                .content(recipe.getSummary().getContent())
                .writer(recipe.getMember().getNickname())
                .ingredients(recipe.getRecipeIngredientList().stream()
                        .map(RecipeIngredient::getName)
                        .collect(Collectors.joining(DELIMITER)))
                .numberOfHeart(recipe.getHeartList().size())
                .numberOfComment(recipe.getCommentList().size())
                .createAt(recipe.getCreatedAt())
                .mainImageUrl(recipe.getMainImageUrl())
                .build());
    }

    /**
     * 작성자: 황태원(이종욱)
     * 레시피 수정 정보를 받아 레시피를 수정
     * 레시피 수정 시 레시피 검색을 위한 객체도 레시피 정보를 기반으로 수정
     * 작성일자: 2023-09-27
     */
    @Transactional
    public void updateRecipe(RecipeDtoRequest recipeDtoRequest, Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));

        recipe.updateRecipe(recipeDtoRequest);
        recipeRepository.save(recipe);
        RecipeDocument recipeDocument = recipeSearchRepository.findById(recipe.getId())
                .orElseThrow(() -> new RecipeException(RecipeErrorCode.NO_SUCH_RECIPE_DOCUMENT));

        recipeDocument.updateTitle(recipeDtoRequest.getTitle());
        recipeDocument.updateContent(recipeDtoRequest.getContent());
        recipeDocument.updateMainImageUrl(recipeDtoRequest.getMainImageUrl());
        recipeDocument.updateIngredients(recipe.getRecipeIngredientList().stream()
                .map(RecipeIngredient::getName)
                .collect(Collectors.joining(DELIMITER)));

        recipeSearchRepository.save(recipeDocument);
    }

    public Recipe getRecipe(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));
    }

    /**
     * 2023-09-26
     * 황태원
     * 레시피 상세보기, 레시피 수정 시 정보 로드
     */
    public RecipeDtoResponse getRecipeDetail(MemberAuthDto memberAuthDto, Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));

        Member member = memberRepository.findById(memberAuthDto.getId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        RecipeDtoResponse recipeDtoResponse =
                RecipeDtoResponse.fromEntity(recipe);

        recipeDtoResponse.updateHeart(heartRepository
                .existsByRecipeAndMember(recipe, member));

        return recipeDtoResponse;
    }

    /**
     * 작성자: 황태원(이종욱)
     * 레시피 id를 이용하여 등록된 레시피 삭제
     * 레시피 삭제로 인해 해당 레시피에 대한 검색 객체 삭제
     * 작성일자: 2023-09-27
     */
    public void deleteRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));
        recipeRepository.deleteById(recipe.getId());

        if (!recipeSearchRepository.existsById(id)) {
            throw new RecipeException(RecipeErrorCode.NO_SUCH_RECIPE_DOCUMENT);
        }
        recipeSearchRepository.deleteById(id);
    }

    public ListResponse<RecipeCardDto> getRecipeList(RecipeSearchRequest recipeSearchRequest) {

        Member member = memberRepository.findById(recipeSearchRequest.getMemberId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        Page<RecipeDocument> result = recipeSearchRepository.searchBy(recipeSearchRequest.getSearchType(),
                recipeSearchRequest.getSearchText(), recipeSearchRequest.getPageable());

        return ListResponse.<RecipeCardDto>builder()
                .totalPages(result.getTotalPages())
                .totalElements(result.getTotalElements())
                .hasNextPage(result.hasNext())
                .content(result.stream()
                        .map(recipeDocument -> RecipeCardDto.builder()
                                .recipeId(recipeDocument.getId())
                                .title(recipeDocument.getTitle())
                                .content(recipeDocument.getContent())
                                .mainImageUrl(recipeDocument.getMainImageUrl())
                                .nickName(recipeDocument.getWriter())
                                .heartCount((int) recipeDocument.getNumberOfHeart())
                                .isHeart(heartRepository.existsHeart(recipeDocument.getId(), member))
                                .build()).collect(Collectors.toList()))
                .build();

    }


    /**
     * 업데이트 시 기존의 이미지 반환
     */
    public ImageUrlDto getBeforeImageUrl(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));
        return ImageUrlDto.builder()
                .mainImageUrl(recipe.getMainImageUrl())
                .cookingOrderImageUrlList(recipe.getRecipeDetailList()
                        .stream().map(RecipeDetail::getCookingOrderImageUrl)
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-10-06
     * 좋아요를 누른 레시피 목록을 반환함.
     */
    public ListResponse<RecipeDtoTopResponse> getRecipeForHeart(Long memberId,
                                                                Pageable pageable) {
        return ListResponse.from(
                recipeRepository.findHeart(memberId, pageable),
                recipe -> RecipeDtoTopResponse.from(recipe, true));

    }

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-10-06
     * 해당 회원이 작성한 꿀조합 목록을 반환함.
     */
    public ListResponse<RecipeDtoTopResponse> getRecipeListByMemberId(Long memberId,
                                                                      Long targetMemberId,
                                                                      Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        Member targetMember = memberRepository.findById(targetMemberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        return ListResponse.from(
                recipeRepository
                        .findByMemberAndIsPublicTrueAndIsQuotationFalse(targetMember,
                                pageable),
                recipe -> RecipeDtoTopResponse.from(recipe, getIsHeart(member, recipe)));
    }

    private boolean getIsHeart(Member member, Recipe recipe) {
        return heartRepository.existsByRecipeAndMember(recipe, member);
    }

    @RedissonLock(group = "recipeReview", key = "#recipeId")
    public void plusReviewStar(Long recipeId, int star) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));

        recipe.getRecipeStatistics().plusStar(star);
        recipeRepository.save(recipe);
    }

    @RedissonLock(group = "recipeReview", key = "#recipeId")
    public void updateReviewStar(Long recipeId, int originStar, int newStar) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));

        recipe.getRecipeStatistics().updateStar(originStar, newStar);
        recipeRepository.save(recipe);
    }

    @RedissonLock(group = "recipeReview", key = "#recipeId")
    public void minusReviewStar(Long recipeId, int star) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));

        recipe.getRecipeStatistics().minusStar(star);
        recipeRepository.save(recipe);
    }

    public Recipe plusCommentCount(Long recipeId) {
        // 레시피의 댓글 수 증가
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));
        recipe.plusCommentCount();

        // 레시피 검색 객체의 댓글 수 증가
        RecipeDocument recipeDocument = recipeSearchRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE_DOCUMENT));
        recipeDocument.plusNumberOfComment();
        recipeSearchRepository.save(recipeDocument);

        return recipeRepository.save(recipe);
    }

    public void minusCommentCount(Long recipeId) {

        // 레시피 댓글 수 감소
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));
        recipe.minusCommentCount();

        // 레시피 검색 객체의 댓글 수 감소
        RecipeDocument recipeDocument = recipeSearchRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE_DOCUMENT));
        recipeDocument.minusNumberOfComment();
        recipeSearchRepository.save(recipeDocument);

        recipeRepository.save(recipe);
    }

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-10-10
     * 메인 페이지에서 생성된 순으로 3개를 조회해 옵니다.
     */
    public List<RecipeCardDto> getMainPageRecipeList(MemberAuthDto memberAuthDto) {
        Member member = memberRepository.findById(memberAuthDto.getId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        return recipeRepository.findTop3ByIsPublicIsTrueOrderByCreatedAtDesc()
                .stream()
                .map(r -> RecipeCardDto.builder()
                        .recipeId(r.getId())
                        .nickName(r.getMember().getNickname())
                        .title(r.getSummary().getTitle())
                        .mainImageUrl(r.getMainImageUrl())
                        .content(r.getSummary().getContent())
                        .heartCount(r.getHeartCount())
                        .isHeart(heartRepository.existsByRecipeAndMember(r, member))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-10-10
     * 레시피 페이지에서 레시피를 orderTpye에 따라서 조회해 옵니다.
     */
    public ListResponse<RecipeCardDto> getRecipePageRecipeList(MemberAuthDto memberAuthDto,
                                                               Pageable pageable,
                                                               OrderType orderType) {
        Member member = memberRepository.findById(memberAuthDto.getId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        Page<Recipe> recipePage;
        int totalPages;
        long totalElements;
        boolean hasNext;

        switch (orderType) {
            case CREATED_AT:
                recipePage = recipeRepository
                        .findByIsPublicIsTrueOrderByCreatedAtDesc(pageable);
                totalElements = recipePage.getTotalElements();
                totalPages = recipePage.getTotalPages();
                hasNext = recipePage.hasNext();
                break;
            case HEART_COUNT:
                recipePage = recipeRepository
                        .findByIsPublicIsTrueOrderByHeartCountDesc(pageable);
                totalPages = recipePage.getTotalPages();
                totalElements = recipePage.getTotalElements();
                hasNext = recipePage.hasNext();
                break;
            case COMMENT_COUNT:
                recipePage = recipeRepository
                        .findByIsPublicIsTrueOrderByCommentCountDesc(pageable);
                totalPages = recipePage.getTotalPages();
                totalElements = recipePage.getTotalElements();
                hasNext = recipePage.hasNext();
                break;
            default:
                throw new RecipeException(ORDER_TYPE_NOT_FOUND);
        }

        return ListResponse.<RecipeCardDto>builder()
                .totalPages(totalPages)
                .hasNextPage(hasNext)
                .totalElements(totalElements)
                .content(
                        recipePage.stream()
                                .map(r -> RecipeCardDto.builder()
                                        .recipeId(r.getId())
                                        .nickName(r.getMember().getNickname())
                                        .mainImageUrl(r.getMainImageUrl())
                                        .title(r.getSummary().getTitle())
                                        .content(r.getSummary().getContent())
                                        .heartCount(r.getHeartCount())
                                        .isHeart(heartRepository.existsByRecipeAndMember(r, member))
                                        .build())
                                .collect(Collectors.toList()))
                .build();
    }

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-10-10
     * 오늘 생성된 레시피 중 좋아요 높은 순으로 5개를 조회해 옵니다.
     */
    public List<RecipeCardDto> recommendedRecipe(MemberAuthDto memberAuthDto) {
        Member member = memberRepository.findById(memberAuthDto.getId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        return recipeRepository.findTop5ByIsPublicIsTrueAndCreatedAtAfterOrderByHeartCountDesc(
                        LocalDate.now().atStartOfDay())
                .stream()
                .map(r -> RecipeCardDto.builder()
                        .recipeId(r.getId())
                        .nickName(r.getMember().getNickname())
                        .title(r.getSummary().getTitle())
                        .mainImageUrl(r.getMainImageUrl())
                        .content(r.getSummary().getContent())
                        .heartCount(r.getHeartCount())
                        .isHeart(heartRepository.existsByRecipeAndMember(r, member))
                        .build())
                .collect(Collectors.toList());
    }
}