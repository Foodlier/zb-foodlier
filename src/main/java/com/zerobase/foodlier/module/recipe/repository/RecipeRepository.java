package com.zerobase.foodlier.module.recipe.repository;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.recipe.dto.quotation.QuotationTopResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    int countByMember(Member member);
    Optional<Recipe> findByIdAndMemberAndIsQuotationTrue(Long recipeId, Member member);

    @Query(
            "SELECT " +
            "new com.zerobase.foodlier.module.recipe.dto.quotation.QuotationTopResponse" +
            "(q.id, q.summary.title, q.summary.content, q.difficulty, q.expectedTime) " +
            "FROM Recipe q " +
            "JOIN Member m ON m = q.member AND m.id = :memberId " +
            "WHERE q.isQuotation = true AND q.id NOT IN " +
            "(SELECT q.id FROM Recipe q " +
            "JOIN Request r ON r.recipe = q " +
            "WHERE q.member.id = :memberId)"
    )
    Page<QuotationTopResponse> findQuotationListForRefrigerator(
            @Param("memberId")Long memberId,
            Pageable pageable
    );

    @Query(
            "SELECT " +
            "new com.zerobase.foodlier.module.recipe.dto.quotation.QuotationTopResponse" +
            "(q.id, q.summary.title, q.summary.content, q.difficulty, q.expectedTime) " +
            "FROM Recipe q " +
            "JOIN Request r ON r.recipe = q AND r.isPaid = true " +
            "WHERE q.member.id = :memberId AND q.isQuotation = true"
    )
    Page<QuotationTopResponse> findQuotationListForRecipe(
            @Param("memberId")Long memberId,
            Pageable pageable
    );

    @Query(
            "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
            "FROM Recipe q " +
            "LEFT JOIN Request r ON r.recipe = q " +
            "WHERE q.id = :quotationId AND q.isQuotation = true AND (q.member.id = :memberId OR r.member.id = :memberId)"
    )
    boolean existsByIdAndMemberForQuotation(
            @Param("memberId")Long memberId,
            @Param("quotationId")Long quotationId
    );

    @Query(
            "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
            "FROM Recipe q " +
            "JOIN Request r ON r.recipe = q " +
            "WHERE q.id = :quotationId AND q.isQuotation = true AND q.member.id = :memberId"
    )
    boolean isNotAbleToDeleteForQuotation(
            @Param("memberId")Long memberId,
            @Param("quotationId")Long quotationId
    );

    @Query(
            "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
            "FROM Recipe q " +
            "JOIN Request r ON r.recipe = q AND r.isPaid = true " +
            "WHERE q = :quotation"
    )
    boolean isAbleToConvert(
            @Param("quotation") Recipe quotation
    );

}
