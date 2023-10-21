package com.zerobase.foodlier.module.history.charge.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPointChargeHistory is a Querydsl query type for PointChargeHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPointChargeHistory extends EntityPathBase<PointChargeHistory> {

    private static final long serialVersionUID = -170829277L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPointChargeHistory pointChargeHistory = new QPointChargeHistory("pointChargeHistory");

    public final com.zerobase.foodlier.common.jpa.audit.QAudit _super = new com.zerobase.foodlier.common.jpa.audit.QAudit(this);

    public final NumberPath<Long> chargePoint = createNumber("chargePoint", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.zerobase.foodlier.module.member.member.domain.model.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath paymentKey = createString("paymentKey");

    public final EnumPath<com.zerobase.foodlier.module.history.type.TransactionType> transactionType = createEnum("transactionType", com.zerobase.foodlier.module.history.type.TransactionType.class);

    public QPointChargeHistory(String variable) {
        this(PointChargeHistory.class, forVariable(variable), INITS);
    }

    public QPointChargeHistory(Path<? extends PointChargeHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPointChargeHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPointChargeHistory(PathMetadata metadata, PathInits inits) {
        this(PointChargeHistory.class, metadata, inits);
    }

    public QPointChargeHistory(Class<? extends PointChargeHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.zerobase.foodlier.module.member.member.domain.model.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

