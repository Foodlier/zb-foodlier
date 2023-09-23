package com.zerobase.foodlier.module.recipe.domain.model;


import com.zerobase.foodlier.common.jpa.audit.Audit;
import com.zerobase.foodlier.module.comment.comment.domain.model.Comment;
import com.zerobase.foodlier.module.like.domain.model.Like;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.vo.RecipeDetail;
import com.zerobase.foodlier.module.recipe.domain.vo.RecipeIngredient;
import com.zerobase.foodlier.module.recipe.domain.type.Difficulty;
import com.zerobase.foodlier.module.recipe.domain.vo.RecipeStatistics;
import com.zerobase.foodlier.module.recipe.domain.vo.Summary;
import com.zerobase.foodlier.module.review.recipe.domain.model.RecipeReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Summary summary;

    private String mainImageUrl;

    private int expectedTime;

    private Long likeCount;

    @Embedded
    private RecipeStatistics recipeStatistics;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private boolean isPublic;

    private boolean isQuotation;
    private boolean isDeleted;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY)
    @Builder.Default
    private List<RecipeReview> recipeReviews = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Like> likeList = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "recipe_detail")
    @Builder.Default
    private List<RecipeDetail> recipeDetailList = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "recipe_ingredient")
    @Builder.Default
    private List<RecipeIngredient> recipeIngredientList = new ArrayList<>();

}
