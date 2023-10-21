package com.zerobase.foodlier.module.review.recipe.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecipeReview is a Querydsl query type for RecipeReview
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecipeReview extends EntityPathBase<RecipeReview> {

    private static final long serialVersionUID = 221497447L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecipeReview recipeReview = new QRecipeReview("recipeReview");

    public final com.zerobase.foodlier.common.jpa.audit.QAudit _super = new com.zerobase.foodlier.common.jpa.audit.QAudit(this);

    public final StringPath content = createString("content");

    public final StringPath cookUrl = createString("cookUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.zerobase.foodlier.module.member.member.domain.model.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.zerobase.foodlier.module.recipe.domain.model.QRecipe recipe;

    public final NumberPath<Integer> star = createNumber("star", Integer.class);

    public QRecipeReview(String variable) {
        this(RecipeReview.class, forVariable(variable), INITS);
    }

    public QRecipeReview(Path<? extends RecipeReview> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecipeReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecipeReview(PathMetadata metadata, PathInits inits) {
        this(RecipeReview.class, metadata, inits);
    }

    public QRecipeReview(Class<? extends RecipeReview> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.zerobase.foodlier.module.member.member.domain.model.QMember(forProperty("member"), inits.get("member")) : null;
        this.recipe = inits.isInitialized("recipe") ? new com.zerobase.foodlier.module.recipe.domain.model.QRecipe(forProperty("recipe"), inits.get("recipe")) : null;
    }

}

