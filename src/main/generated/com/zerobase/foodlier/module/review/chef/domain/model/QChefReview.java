package com.zerobase.foodlier.module.review.chef.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChefReview is a Querydsl query type for ChefReview
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChefReview extends EntityPathBase<ChefReview> {

    private static final long serialVersionUID = -1770646633L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChefReview chefReview = new QChefReview("chefReview");

    public final com.zerobase.foodlier.common.jpa.audit.QAudit _super = new com.zerobase.foodlier.common.jpa.audit.QAudit(this);

    public final com.zerobase.foodlier.module.member.chef.domain.model.QChefMember chefMember;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.zerobase.foodlier.module.request.domain.model.QRequest request;

    public final NumberPath<Integer> star = createNumber("star", Integer.class);

    public QChefReview(String variable) {
        this(ChefReview.class, forVariable(variable), INITS);
    }

    public QChefReview(Path<? extends ChefReview> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChefReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChefReview(PathMetadata metadata, PathInits inits) {
        this(ChefReview.class, metadata, inits);
    }

    public QChefReview(Class<? extends ChefReview> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chefMember = inits.isInitialized("chefMember") ? new com.zerobase.foodlier.module.member.chef.domain.model.QChefMember(forProperty("chefMember"), inits.get("chefMember")) : null;
        this.request = inits.isInitialized("request") ? new com.zerobase.foodlier.module.request.domain.model.QRequest(forProperty("request"), inits.get("request")) : null;
    }

}

