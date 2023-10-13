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
import com.zerobase.foodlier.module.recipe.type.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.recipe.exception.recipe.RecipeErrorCode.*;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
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
    @Override
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
                .writer(recipe.getMember().getNickname())
                .ingredients(recipe.getRecipeIngredientList().stream()
                        .map(RecipeIngredient::getName)
                        .collect(Collectors.joining(DELIMITER)))
                .numberOfHeart(recipe.getHeartList().size())
                .numberOfComment(recipe.getCommentList().size())
                .build());
    }

    /**
     * 작성자: 황태원(이종욱)
     * 레시피 수정 정보를 받아 레시피를 수정
     * 레시피 수정 시 레시피 검색을 위한 객체도 레시피 정보를 기반으로 수정
     * 작성일자: 2023-09-27
     */
    @Transactional
    @Override
    public void updateRecipe(RecipeDtoRequest recipeDtoRequest, Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));

        recipe.setSummary(Summary.builder()
                .title(recipeDtoRequest.getTitle())
                .content(recipeDtoRequest.getContent())
                .build());
        recipe.setMainImageUrl(recipeDtoRequest.getMainImageUrl());
        recipe.setExpectedTime(recipeDtoRequest.getExpectedTime());
        recipe.setDifficulty(recipeDtoRequest.getDifficulty());
        recipe.setRecipeDetailList(recipeDtoRequest.getRecipeDetailDtoList()
                .stream()
                .map(RecipeDetailDto::toEntity)
                .collect(Collectors.toList()));
        recipe.setRecipeIngredientList(recipeDtoRequest.getRecipeIngredientDtoList()
                .stream()
                .map(RecipeIngredientDto::toEntity)
                .collect(Collectors.toList()));

        recipeRepository.save(recipe);
        RecipeDocument recipeDocument = recipeSearchRepository.findById(recipe.getId())
                .orElseThrow(() -> new RecipeException(RecipeErrorCode.NO_SUCH_RECIPE_DOCUMENT));

        recipeDocument.updateTitle(recipe.getSummary().getTitle());
        recipeDocument.updateIngredients(recipe.getRecipeIngredientList().stream()
                .map(RecipeIngredient::getName)
                .collect(Collectors.joining(DELIMITER)));

        recipeSearchRepository.save(recipeDocument);
    }

    @Override
    public Recipe getRecipe(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));
    }

    /**
     * 2023-09-26
     * 황태원
     * 레시피 상세보기, 레시피 수정 시 정보 로드
     */
    @Override
    public RecipeDtoResponse getRecipeDetail(MemberAuthDto memberAuthDto, Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));

        Member member = memberRepository.findById(memberAuthDto.getId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        RecipeDtoResponse recipeDtoResponse =
                RecipeDtoResponse.fromEntity(recipe);

        recipeDtoResponse.setHeart(heartRepository
                .existsByRecipeAndMember(recipe, member));

        return recipeDtoResponse;
    }

    /**
     * 작성자: 황태원(이종욱)
     * 레시피 id를 이용하여 등록된 레시피 삭제
     * 레시피 삭제로 인해 해당 레시피에 대한 검색 객체 삭제
     * 작성일자: 2023-09-27
     */
    @Override
    public void deleteRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));
        recipeRepository.deleteById(recipe.getId());

        if (!recipeSearchRepository.existsById(id)) {
            throw new RecipeException(RecipeErrorCode.NO_SUCH_RECIPE_DOCUMENT);
        }
        recipeSearchRepository.deleteById(id);
    }

    private List<RecipeCardDto> searchBy(Page<RecipeDocument> recipeDocumentPage, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        List<RecipeCardDto> recipeListByWriter = new ArrayList<>();
        for (RecipeDocument recipeDocument : recipeDocumentPage.getContent()) {
            Recipe recipe = recipeRepository.findById(recipeDocument.getId())
                    .orElseThrow(() -> new RecipeException(NO_SUCH_RECIPE));
            recipeListByWriter.add(RecipeCardDto.builder()
                    .id(recipe.getId())
                    .nickName(recipe.getMember().getNickname())
                    .content(recipe.getSummary().getContent())
                    .title(recipe.getSummary().getTitle())
                    .mainImageUrl(recipe.getMainImageUrl())
                    .isHeart(heartRepository.existsByRecipeAndMember(recipe, member))
                    .heartCount(recipe.getHeartCount())
                    .build());
        }
        return recipeListByWriter;
    }

    /**
     * - 작성자: 이종욱
     * - 검색어와 정렬 타입을 이용하여 조건에 맞는 레시피 목록 반환
     * - 작성일자: 2023-10-11
     */
    @Override
    @Transactional(readOnly = true)
    public ListResponse<RecipeCardDto> getRecipeList(RecipeSearchRequest recipeSearchRequest) {
        Page<RecipeDocument> findingResult;

        if (!SortType.existsSortType(recipeSearchRequest.getSortType())) {
            throw new RecipeException(SORT_TYPE_NOT_FOUND);
        }

        switch (recipeSearchRequest.getSearchType()) {

            case TITLE: {
                findingResult = recipeSearchRepository.findByTitle(recipeSearchRequest.getSearchText(),
                        recipeSearchRequest.getPageable());
                break;
            }

            case WRITER: {
                findingResult = recipeSearchRepository.findByWriter(recipeSearchRequest.getSearchText(),
                        recipeSearchRequest.getPageable());
                break;
            }

            case INGREDIENTS: {
                findingResult = recipeSearchRepository.findByIngredients(recipeSearchRequest.getSearchText(),
                        recipeSearchRequest.getPageable());
                break;
            }

            default:
                throw new RecipeException(SEARCH_TYPE_NOT_FOUND);
        }

        return ListResponse.<RecipeCardDto>builder()
                .totalElements(findingResult.getTotalElements())
                .totalPages(findingResult.getTotalPages())
                .hasNextPage(findingResult.hasNext())
                .content(searchBy(findingResult, recipeSearchRequest.getMemberId()))
                .build();
    }


    /**
     * 업데이트 시 기존의 이미지 반환
     */
    @Override
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
    @Override
    public ListResponse<RecipeDtoTopResponse> getRecipeForHeart(Long memberId,
                                                                Pageable pageable) {
        return ListResponse.from(
                recipeRepository.findByHeart(memberId, pageable),
                recipe -> RecipeDtoTopResponse.from(recipe, true));

    }

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-10-06
     * 해당 회원이 작성한 꿀조합 목록을 반환함.
     */
    @Override
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

    @Override
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

    @Override
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
    @Override
    public List<RecipeCardDto> getMainPageRecipeList(MemberAuthDto memberAuthDto) {
        Member member = memberRepository.findById(memberAuthDto.getId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        return recipeRepository.findTop3ByIsPublicOrderByCreatedAtDesc(false)
                .stream()
                .map(r -> RecipeCardDto.builder()
                        .id(r.getId())
                        .nickName(r.getMember().getNickname())
                        .title(r.getSummary().getTitle())
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
    @Override
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
                recipePage = recipeRepository.findByIsPublicOrderByCreatedAtDesc(
                        false, pageable);
                totalElements = recipePage.getTotalElements();
                totalPages = recipePage.getTotalPages();
                hasNext = recipePage.hasNext();
                break;
            case HEART_COUNT:
                recipePage = recipeRepository.findByIsPublicOrderByHeartCountDesc(
                        false, pageable);
                totalPages = recipePage.getTotalPages();
                totalElements = recipePage.getTotalElements();
                hasNext = recipePage.hasNext();
                break;
            case COMMENT_COUNT:
                recipePage = recipeRepository.findByIsPublicOrderByCommentCountDesc(
                        false, pageable);
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
                                        .id(r.getId())
                                        .nickName(r.getMember().getNickname())
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
    @Override
    public List<RecipeCardDto> recommendedRecipe(MemberAuthDto memberAuthDto) {
        Member member = memberRepository.findById(memberAuthDto.getId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        return recipeRepository.findTop5ByIsPublicCreatedAtAfterOrderByHeartCountDesc(
                        false, LocalDate.now().atStartOfDay())
                .stream()
                .map(r -> RecipeCardDto.builder()
                        .id(r.getId())
                        .nickName(r.getMember().getNickname())
                        .title(r.getSummary().getTitle())
                        .content(r.getSummary().getContent())
                        .heartCount(r.getHeartCount())
                        .isHeart(heartRepository.existsByRecipeAndMember(r, member))
                        .build())
                .collect(Collectors.toList());
    }
}