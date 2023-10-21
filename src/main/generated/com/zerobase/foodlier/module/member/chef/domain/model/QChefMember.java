package com.zerobase.foodlier.module.member.chef.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChefMember is a Querydsl query type for ChefMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChefMember extends EntityPathBase<ChefMember> {

    private static final long serialVersionUID = 1650930391L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChefMember chefMember = new QChefMember("chefMember");

    public final com.zerobase.foodlier.common.jpa.audit.QAudit _super = new com.zerobase.foodlier.common.jpa.audit.QAudit(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> exp = createNumber("exp", Long.class);

    public final EnumPath<com.zerobase.foodlier.module.member.chef.type.GradeType> gradeType = createEnum("gradeType", com.zerobase.foodlier.module.member.chef.type.GradeType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath introduce = createString("introduce");

    public final com.zerobase.foodlier.module.member.member.domain.model.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Integer> reviewCount = createNumber("reviewCount", Integer.class);

    public final NumberPath<Double> starAvg = createNumber("starAvg", Double.class);

    public final NumberPath<Integer> starSum = createNumber("starSum", Integer.class);

    public QChefMember(String variable) {
        this(ChefMember.class, forVariable(variable), INITS);
    }

    public QChefMember(Path<? extends ChefMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChefMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChefMember(PathMetadata metadata, PathInits inits) {
        this(ChefMember.class, metadata, inits);
    }

    public QChefMember(Class<? extends ChefMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.zerobase.foodlier.module.member.member.domain.model.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

