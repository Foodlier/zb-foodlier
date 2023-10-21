package com.zerobase.foodlier.module.payment.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPayment is a Querydsl query type for Payment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPayment extends EntityPathBase<Payment> {

    private static final long serialVersionUID = -292838749L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPayment payment = new QPayment("payment");

    public final com.zerobase.foodlier.common.jpa.audit.QAudit _super = new com.zerobase.foodlier.common.jpa.audit.QAudit(this);

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath customerEmail = createString("customerEmail");

    public final StringPath customerNickName = createString("customerNickName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isCanceled = createBoolean("isCanceled");

    public final com.zerobase.foodlier.module.member.member.domain.model.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath orderId = createString("orderId");

    public final EnumPath<com.zerobase.foodlier.module.payment.type.OrderNameType> orderName = createEnum("orderName", com.zerobase.foodlier.module.payment.type.OrderNameType.class);

    public final StringPath payDate = createString("payDate");

    public final StringPath payFailReason = createString("payFailReason");

    public final StringPath paymentKey = createString("paymentKey");

    public final StringPath paySuccessYn = createString("paySuccessYn");

    public final EnumPath<com.zerobase.foodlier.module.payment.type.PayType> payType = createEnum("payType", com.zerobase.foodlier.module.payment.type.PayType.class);

    public QPayment(String variable) {
        this(Payment.class, forVariable(variable), INITS);
    }

    public QPayment(Path<? extends Payment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPayment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPayment(PathMetadata metadata, PathInits inits) {
        this(Payment.class, metadata, inits);
    }

    public QPayment(Class<? extends Payment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.zerobase.foodlier.module.member.member.domain.model.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

