package com.zerobase.foodlier.module.recipe.domain.model;


import com.zerobase.foodlier.common.jpa.audit.Audit;
import com.zerobase.foodlier.module.comment.comment.domain.model.Comment;
import com.zerobase.foodlier.module.heart.domain.model.Heart;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.type.Difficulty;
import com.zerobase.foodlier.module.recipe.domain.vo.RecipeDetail;
import com.zerobase.foodlier.module.recipe.domain.vo.RecipeIngredient;
import com.zerobase.foodlier.module.recipe.domain.vo.RecipeStatistics;
import com.zerobase.foodlier.module.recipe.domain.vo.Summary;
import com.zerobase.foodlier.module.recipe.dto.recipe.RecipeDetailDto;
import com.zerobase.foodlier.module.recipe.dto.recipe.RecipeDtoRequest;
import com.zerobase.foodlier.module.recipe.dto.recipe.RecipeIngredientDto;
import com.zerobase.foodlier.module.review.recipe.domain.model.RecipeReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE recipe SET is_deleted = true WHERE id = ?")
public class Recipe extends Audit {
    private final static int ZERO = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Summary summary;

    private String mainImageUrl;

    private int expectedTime;

    private int heartCount;
    private int commentCount;
    @Embedded
    private RecipeStatistics recipeStatistics;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private Boolean isPublic;

    @Builder.Default
    private Boolean isQuotation = false;

    @Builder.Default
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY)
    @Builder.Default
    private List<RecipeReview> recipeReviewList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Heart> heartList = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "recipe_detail")
    @Builder.Default
    private List<RecipeDetail> recipeDetailList = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "recipe_ingredient")
    @Builder.Default
    private List<RecipeIngredient> recipeIngredientList = new ArrayList<>();

    public void plusHeart() {
        this.heartCount++;
    }

    public void minusHeart() {
        if (heartCount > ZERO) {
            this.heartCount--;
        }
    }

    public void plusCommentCount() {
        ++this.commentCount;
    }

    public void minusCommentCount() {
        this.commentCount = Math.max(ZERO, --this.commentCount);
    }

    public void updateRecipe(RecipeDtoRequest recipeDtoRequest) {
        this.summary = Summary.builder()
                .title(recipeDtoRequest.getTitle())
                .content(recipeDtoRequest.getContent())
                .build();
        this.mainImageUrl = recipeDtoRequest.getMainImageUrl();
        this.expectedTime = recipeDtoRequest.getExpectedTime();
        this.difficulty = recipeDtoRequest.getDifficulty();
        this.recipeDetailList = recipeDtoRequest.getRecipeDetailDtoList()
                .stream()
                .map(RecipeDetailDto::toEntity)
                .collect(Collectors.toList());
        this.recipeIngredientList = recipeDtoRequest.getRecipeIngredientDtoList()
                .stream()
                .map(RecipeIngredientDto::toEntity)
                .collect(Collectors.toList());
    }

    public void updateSummary(Summary summary) {
        this.summary = summary;
    }

    public void updateMainImageUrl(String url) {
        this.mainImageUrl = url;
    }

    public void updateExpectedTime(int time) {
        this.expectedTime = time;
    }

    public void updateDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void updateRecipeDetailList(List<RecipeDetail> recipeDetailList) {
        this.recipeDetailList = recipeDetailList;
    }

    public void updateRecipeIngredientList(List<RecipeIngredient> recipeIngredientList) {
        this.recipeIngredientList = recipeIngredientList;
    }

    public void updatePublic() {
        this.isPublic = true;
    }

    public void updateNoQuotation() {
        this.isQuotation = false;
    }
}
