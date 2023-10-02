package com.zerobase.foodlier.module.recipe.service.quotation;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.domain.vo.RecipeDetail;
import com.zerobase.foodlier.module.recipe.domain.vo.RecipeStatistics;
import com.zerobase.foodlier.module.recipe.domain.vo.Summary;
import com.zerobase.foodlier.module.recipe.dto.quotation.QuotationDetailResponse;
import com.zerobase.foodlier.module.recipe.dto.quotation.QuotationDtoRequest;
import com.zerobase.foodlier.module.recipe.dto.quotation.QuotationTopResponse;
import com.zerobase.foodlier.module.recipe.dto.recipe.RecipeIngredientDto;
import com.zerobase.foodlier.module.recipe.exception.quotation.QuotationException;
import com.zerobase.foodlier.module.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.recipe.exception.quotation.QuotationErrorCode.*;

@Service
@RequiredArgsConstructor
public class QuotationServiceImpl {

    private final RecipeRepository recipeRepository;
    private final MemberRepository memberRepository;

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-10-02
     * 견적서를 새로 생성합니다. (이미지 없는 레시피 생성)
     */
    public void createQuotation(Long memberId, QuotationDtoRequest request){
        Member member = getMember(memberId);

        Recipe.builder()
                .summary(Summary.builder()
                        .title(request.getTitle())
                        .content(request.getContent())
                        .build())
                .expectedTime(request.getExpectedTime())
                .recipeStatistics(new RecipeStatistics())
                .difficulty(request.getDifficulty())
                .isPublic(true)
                .member(member)
                .recipeIngredientList(request.getRecipeIngredientDtoList()
                        .stream()
                        .map(RecipeIngredientDto::toEntity)
                        .collect(Collectors.toList()))
                .recipeDetailList(request.getRecipeDetailDtoList()
                        .stream()
                        .map(x -> new RecipeDetail(x, null))
                        .collect(Collectors.toList()))
                .isQuotation(true) //견적서 여부 true
                .build();
    }

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-10-02
     * 본인이 전송 가능한, 견적서 목록을 조회합니다. (냉장고를 부탁해에서 호출되는 목록)
     */
    public List<QuotationTopResponse> getQuotationListForRefrigerator(Long memberId, Pageable pageable){
        return recipeRepository.findQuotationListForRefrigerator(memberId, pageable).getContent();
    }

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-10-02
     * 결제까지 완료된 견적서 목록을 조회합니다. (레시피 작성시에, 레시피로 변환할 견적서 조회)
     */
    public List<QuotationTopResponse> getQuotationListForRecipe(Long memberId, Pageable pageable){
        return recipeRepository.findQuotationListForRecipe(memberId, pageable).getContent();
    }

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-10-02
     * 본인이 작성한 견적서 상세정보를 반환합니다.
     *
     * 본인 뿐만 아니라, 견적서를 받은 상대방도 볼 수 있는지 확인이 필요함.
     */
    public QuotationDetailResponse getQuotationDetail(Long memberId, Long quotationId){
        if(recipeRepository.existsByIdAndMemberForQuotation(memberId, quotationId)){
            throw new QuotationException(HAS_NOT_QUOTATION_READ_PERMISSION);
        }
        return QuotationDetailResponse.fromEntity(recipeRepository.findById(quotationId)
                .orElseThrow(() -> new QuotationException(QUOTATION_NOT_FOUND)));
    }

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-10-02
     * 견적서에 사진을 담아서, 레시피를 완성시켜, 변환합니다.
     */
    public void convertToRecipe(Long memberId){

    }

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-10-02
     * 견적서를 수정합니다.
     */
    public void updateQuotation(Long memberId, Long quotationId, QuotationDtoRequest request){
        Member member = getMember(memberId);
        Recipe recipe = recipeRepository.findByIdAndMemberAndIsQuotationTrue(quotationId, member)
                .orElseThrow(() -> new QuotationException(QUOTATION_NOT_FOUND));

        recipe.setSummary(Summary.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build());
        recipe.setExpectedTime(request.getExpectedTime());
        recipe.setDifficulty(request.getDifficulty());

        recipe.setRecipeDetailList(request.getRecipeDetailDtoList()
                .stream()
                .map(x -> new RecipeDetail(x, null))
                .collect(Collectors.toList()));

        recipe.setRecipeIngredientList(request.getRecipeIngredientDtoList()
                .stream()
                .map(RecipeIngredientDto::toEntity)
                .collect(Collectors.toList()));

        recipeRepository.save(recipe);
    }

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-10-02
     * 본인의 견적서를 삭제합니다. 단, 요청 중인 견적서, 성사된 견적서는 삭제 불가능
     */
    public void deleteQuotation(Long memberId, Long quotationId){
        if(!recipeRepository.existsDeletePermissionForQuotation(memberId, quotationId)){
            throw new QuotationException(CANNOT_DELETE_IS_LOCKED);
        }
        recipeRepository.deleteById(quotationId);
    }

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-10-02
     * 견적서를 보냅니다.
     */
    public void send(Long memberId, Long quotationId){

    }

    private Member getMember(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    //============= Validates ====================



}
