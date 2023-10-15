package com.zerobase.foodlier.module.recipe.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecipe is a Querydsl query type for Recipe
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecipe extends EntityPathBase<Recipe> {

    private static final long serialVersionUID = -873296671L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecipe recipe = new QRecipe("recipe");

    public final com.zerobase.foodlier.common.jpa.audit.QAudit _super = new com.zerobase.foodlier.common.jpa.audit.QAudit(this);

    public final NumberPath<Integer> commentCount = createNumber("commentCount", Integer.class);

    public final ListPath<com.zerobase.foodlier.module.comment.comment.domain.model.Comment, com.zerobase.foodlier.module.comment.comment.domain.model.QComment> commentList = this.<com.zerobase.foodlier.module.comment.comment.domain.model.Comment, com.zerobase.foodlier.module.comment.comment.domain.model.QComment>createList("commentList", com.zerobase.foodlier.module.comment.comment.domain.model.Comment.class, com.zerobase.foodlier.module.comment.comment.domain.model.QComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<com.zerobase.foodlier.module.recipe.domain.type.Difficulty> difficulty = createEnum("difficulty", com.zerobase.foodlier.module.recipe.domain.type.Difficulty.class);

    public final NumberPath<Integer> expectedTime = createNumber("expectedTime", Integer.class);

    public final NumberPath<Integer> heartCount = createNumber("heartCount", Integer.class);

    public final ListPath<com.zerobase.foodlier.module.heart.domain.model.Heart, com.zerobase.foodlier.module.heart.domain.model.QHeart> heartList = this.<com.zerobase.foodlier.module.heart.domain.model.Heart, com.zerobase.foodlier.module.heart.domain.model.QHeart>createList("heartList", com.zerobase.foodlier.module.heart.domain.model.Heart.class, com.zerobase.foodlier.module.heart.domain.model.QHeart.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final BooleanPath isPublic = createBoolean("isPublic");

    public final BooleanPath isQuotation = createBoolean("isQuotation");

    public final StringPath mainImageUrl = createString("mainImageUrl");

    public final com.zerobase.foodlier.module.member.member.domain.model.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final ListPath<com.zerobase.foodlier.module.recipe.domain.vo.RecipeDetail, com.zerobase.foodlier.module.recipe.domain.vo.QRecipeDetail> recipeDetailList = this.<com.zerobase.foodlier.module.recipe.domain.vo.RecipeDetail, com.zerobase.foodlier.module.recipe.domain.vo.QRecipeDetail>createList("recipeDetailList", com.zerobase.foodlier.module.recipe.domain.vo.RecipeDetail.class, com.zerobase.foodlier.module.recipe.domain.vo.QRecipeDetail.class, PathInits.DIRECT2);

    public final ListPath<com.zerobase.foodlier.module.recipe.domain.vo.RecipeIngredient, com.zerobase.foodlier.module.recipe.domain.vo.QRecipeIngredient> recipeIngredientList = this.<com.zerobase.foodlier.module.recipe.domain.vo.RecipeIngredient, com.zerobase.foodlier.module.recipe.domain.vo.QRecipeIngredient>createList("recipeIngredientList", com.zerobase.foodlier.module.recipe.domain.vo.RecipeIngredient.class, com.zerobase.foodlier.module.recipe.domain.vo.QRecipeIngredient.class, PathInits.DIRECT2);

    public final ListPath<com.zerobase.foodlier.module.review.recipe.domain.model.RecipeReview, com.zerobase.foodlier.module.review.recipe.domain.model.QRecipeReview> recipeReviewList = this.<com.zerobase.foodlier.module.review.recipe.domain.model.RecipeReview, com.zerobase.foodlier.module.review.recipe.domain.model.QRecipeReview>createList("recipeReviewList", com.zerobase.foodlier.module.review.recipe.domain.model.RecipeReview.class, com.zerobase.foodlier.module.review.recipe.domain.model.QRecipeReview.class, PathInits.DIRECT2);

    public final com.zerobase.foodlier.module.recipe.domain.vo.QRecipeStatistics recipeStatistics;

    public final com.zerobase.foodlier.module.recipe.domain.vo.QSummary summary;

    public QRecipe(String variable) {
        this(Recipe.class, forVariable(variable), INITS);
    }

    public QRecipe(Path<? extends Recipe> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecipe(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecipe(PathMetadata metadata, PathInits inits) {
        this(Recipe.class, metadata, inits);
    }

    public QRecipe(Class<? extends Recipe> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.zerobase.foodlier.module.member.member.domain.model.QMember(forProperty("member"), inits.get("member")) : null;
        this.recipeStatistics = inits.isInitialized("recipeStatistics") ? new com.zerobase.foodlier.module.recipe.domain.vo.QRecipeStatistics(forProperty("recipeStatistics")) : null;
        this.summary = inits.isInitialized("summary") ? new com.zerobase.foodlier.module.recipe.domain.vo.QSummary(forProperty("summary")) : null;
    }

}

