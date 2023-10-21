package com.zerobase.foodlier.module.history.transaction.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberBalanceHistory is a Querydsl query type for MemberBalanceHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberBalanceHistory extends EntityPathBase<MemberBalanceHistory> {

    private static final long serialVersionUID = 601995987L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberBalanceHistory memberBalanceHistory = new QMemberBalanceHistory("memberBalanceHistory");

    public final com.zerobase.foodlier.common.jpa.audit.QAudit _super = new com.zerobase.foodlier.common.jpa.audit.QAudit(this);

    public final NumberPath<Integer> changePoint = createNumber("changePoint", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> currentPoint = createNumber("currentPoint", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.zerobase.foodlier.module.member.member.domain.model.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath sender = createString("sender");

    public final EnumPath<com.zerobase.foodlier.module.history.type.TransactionType> transactionType = createEnum("transactionType", com.zerobase.foodlier.module.history.type.TransactionType.class);

    public QMemberBalanceHistory(String variable) {
        this(MemberBalanceHistory.class, forVariable(variable), INITS);
    }

    public QMemberBalanceHistory(Path<? extends MemberBalanceHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberBalanceHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberBalanceHistory(PathMetadata metadata, PathInits inits) {
        this(MemberBalanceHistory.class, metadata, inits);
    }

    public QMemberBalanceHistory(Class<? extends MemberBalanceHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.zerobase.foodlier.module.member.member.domain.model.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

