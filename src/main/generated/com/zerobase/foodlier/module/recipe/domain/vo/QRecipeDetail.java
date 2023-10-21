package com.zerobase.foodlier.module.recipe.domain.vo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRecipeDetail is a Querydsl query type for RecipeDetail
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QRecipeDetail extends BeanPath<RecipeDetail> {

    private static final long serialVersionUID = 1368994086L;

    public static final QRecipeDetail recipeDetail = new QRecipeDetail("recipeDetail");

    public final StringPath cookingOrder = createString("cookingOrder");

    public final StringPath cookingOrderImageUrl = createString("cookingOrderImageUrl");

    public QRecipeDetail(String variable) {
        super(RecipeDetail.class, forVariable(variable));
    }

    public QRecipeDetail(Path<? extends RecipeDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRecipeDetail(PathMetadata metadata) {
        super(RecipeDetail.class, metadata);
    }

}

