package com.zerobase.foodlier.module.request.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRequest is a Querydsl query type for Request
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRequest extends EntityPathBase<Request> {

    private static final long serialVersionUID = -1420524989L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRequest request = new QRequest("request");

    public final com.zerobase.foodlier.common.jpa.audit.QAudit _super = new com.zerobase.foodlier.common.jpa.audit.QAudit(this);

    public final com.zerobase.foodlier.module.member.chef.domain.model.QChefMember chefMember;

    public final com.zerobase.foodlier.module.review.chef.domain.model.QChefReview chefReview;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.zerobase.foodlier.module.dm.room.domain.model.QDmRoom dmRoom;

    public final DateTimePath<java.time.LocalDateTime> expectedAt = createDateTime("expectedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> expectedPrice = createNumber("expectedPrice", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.zerobase.foodlier.module.request.domain.vo.Ingredient, com.zerobase.foodlier.module.request.domain.vo.QIngredient> ingredientList = this.<com.zerobase.foodlier.module.request.domain.vo.Ingredient, com.zerobase.foodlier.module.request.domain.vo.QIngredient>createList("ingredientList", com.zerobase.foodlier.module.request.domain.vo.Ingredient.class, com.zerobase.foodlier.module.request.domain.vo.QIngredient.class, PathInits.DIRECT2);

    public final BooleanPath isPaid = createBoolean("isPaid");

    public final com.zerobase.foodlier.module.member.member.domain.model.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Long> paidPrice = createNumber("paidPrice", Long.class);

    public final com.zerobase.foodlier.module.recipe.domain.model.QRecipe recipe;

    public final StringPath title = createString("title");

    public QRequest(String variable) {
        this(Request.class, forVariable(variable), INITS);
    }

    public QRequest(Path<? extends Request> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRequest(PathMetadata metadata, PathInits inits) {
        this(Request.class, metadata, inits);
    }

    public QRequest(Class<? extends Request> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chefMember = inits.isInitialized("chefMember") ? new com.zerobase.foodlier.module.member.chef.domain.model.QChefMember(forProperty("chefMember"), inits.get("chefMember")) : null;
        this.chefReview = inits.isInitialized("chefReview") ? new com.zerobase.foodlier.module.review.chef.domain.model.QChefReview(forProperty("chefReview"), inits.get("chefReview")) : null;
        this.dmRoom = inits.isInitialized("dmRoom") ? new com.zerobase.foodlier.module.dm.room.domain.model.QDmRoom(forProperty("dmRoom"), inits.get("dmRoom")) : null;
        this.member = inits.isInitialized("member") ? new com.zerobase.foodlier.module.member.member.domain.model.QMember(forProperty("member"), inits.get("member")) : null;
        this.recipe = inits.isInitialized("recipe") ? new com.zerobase.foodlier.module.recipe.domain.model.QRecipe(forProperty("recipe"), inits.get("recipe")) : null;
    }

}

