package com.zerobase.foodlier.module.dm.room.domain.vo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSuggestion is a Querydsl query type for Suggestion
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QSuggestion extends BeanPath<Suggestion> {

    private static final long serialVersionUID = -1084544479L;

    public static final QSuggestion suggestion = new QSuggestion("suggestion");

    public final BooleanPath isAccept = createBoolean("isAccept");

    public final BooleanPath isSuggested = createBoolean("isSuggested");

    public final NumberPath<Integer> suggestedPrice = createNumber("suggestedPrice", Integer.class);

    public QSuggestion(String variable) {
        super(Suggestion.class, forVariable(variable));
    }

    public QSuggestion(Path<? extends Suggestion> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSuggestion(PathMetadata metadata) {
        super(Suggestion.class, metadata);
    }

}

