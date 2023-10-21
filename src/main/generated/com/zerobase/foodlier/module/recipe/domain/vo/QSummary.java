package com.zerobase.foodlier.module.recipe.domain.vo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSummary is a Querydsl query type for Summary
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QSummary extends BeanPath<Summary> {

    private static final long serialVersionUID = -438156897L;

    public static final QSummary summary = new QSummary("summary");

    public final StringPath content = createString("content");

    public final StringPath title = createString("title");

    public QSummary(String variable) {
        super(Summary.class, forVariable(variable));
    }

    public QSummary(Path<? extends Summary> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSummary(PathMetadata metadata) {
        super(Summary.class, metadata);
    }

}

