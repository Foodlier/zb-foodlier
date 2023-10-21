package com.zerobase.foodlier.module.requestform.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRequestForm is a Querydsl query type for RequestForm
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRequestForm extends EntityPathBase<RequestForm> {

    private static final long serialVersionUID = -1160548637L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRequestForm requestForm = new QRequestForm("requestForm");

    public final com.zerobase.foodlier.common.jpa.audit.QAudit _super = new com.zerobase.foodlier.common.jpa.audit.QAudit(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> expectedAt = createDateTime("expectedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> expectedPrice = createNumber("expectedPrice", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.zerobase.foodlier.module.request.domain.vo.Ingredient, com.zerobase.foodlier.module.request.domain.vo.QIngredient> ingredientList = this.<com.zerobase.foodlier.module.request.domain.vo.Ingredient, com.zerobase.foodlier.module.request.domain.vo.QIngredient>createList("ingredientList", com.zerobase.foodlier.module.request.domain.vo.Ingredient.class, com.zerobase.foodlier.module.request.domain.vo.QIngredient.class, PathInits.DIRECT2);

    public final com.zerobase.foodlier.module.member.member.domain.model.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.zerobase.foodlier.module.recipe.domain.model.QRecipe recipe;

    public final StringPath title = createString("title");

    public QRequestForm(String variable) {
        this(RequestForm.class, forVariable(variable), INITS);
    }

    public QRequestForm(Path<? extends RequestForm> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRequestForm(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRequestForm(PathMetadata metadata, PathInits inits) {
        this(RequestForm.class, metadata, inits);
    }

    public QRequestForm(Class<? extends RequestForm> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.zerobase.foodlier.module.member.member.domain.model.QMember(forProperty("member"), inits.get("member")) : null;
        this.recipe = inits.isInitialized("recipe") ? new com.zerobase.foodlier.module.recipe.domain.model.QRecipe(forProperty("recipe"), inits.get("recipe")) : null;
    }

}

