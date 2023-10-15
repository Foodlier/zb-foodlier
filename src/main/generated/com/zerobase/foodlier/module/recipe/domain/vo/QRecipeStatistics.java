package com.zerobase.foodlier.module.recipe.domain.vo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRecipeStatistics is a Querydsl query type for RecipeStatistics
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QRecipeStatistics extends BeanPath<RecipeStatistics> {

    private static final long serialVersionUID = -906406024L;

    public static final QRecipeStatistics recipeStatistics = new QRecipeStatistics("recipeStatistics");

    public final NumberPath<Integer> reviewCount = createNumber("reviewCount", Integer.class);

    public final NumberPath<Double> reviewStarAverage = createNumber("reviewStarAverage", Double.class);

    public final NumberPath<Integer> reviewStarSum = createNumber("reviewStarSum", Integer.class);

    public QRecipeStatistics(String variable) {
        super(RecipeStatistics.class, forVariable(variable));
    }

    public QRecipeStatistics(Path<? extends RecipeStatistics> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRecipeStatistics(PathMetadata metadata) {
        super(RecipeStatistics.class, metadata);
    }

}

