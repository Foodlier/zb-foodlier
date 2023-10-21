package com.zerobase.foodlier.module.recipe.domain.vo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRecipeIngredient is a Querydsl query type for RecipeIngredient
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QRecipeIngredient extends BeanPath<RecipeIngredient> {

    private static final long serialVersionUID = -1018226650L;

    public static final QRecipeIngredient recipeIngredient = new QRecipeIngredient("recipeIngredient");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath unit = createString("unit");

    public QRecipeIngredient(String variable) {
        super(RecipeIngredient.class, forVariable(variable));
    }

    public QRecipeIngredient(Path<? extends RecipeIngredient> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRecipeIngredient(PathMetadata metadata) {
        super(RecipeIngredient.class, metadata);
    }

}

