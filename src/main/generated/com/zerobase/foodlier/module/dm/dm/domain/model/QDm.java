package com.zerobase.foodlier.module.dm.dm.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDm is a Querydsl query type for Dm
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDm extends EntityPathBase<Dm> {

    private static final long serialVersionUID = -1206157612L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDm dm = new QDm("dm");

    public final com.zerobase.foodlier.common.jpa.audit.QAudit _super = new com.zerobase.foodlier.common.jpa.audit.QAudit(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.zerobase.foodlier.module.dm.room.domain.model.QDmRoom dmroom;

    public final StringPath flag = createString("flag");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.zerobase.foodlier.module.dm.dm.type.MessageType> messageType = createEnum("messageType", com.zerobase.foodlier.module.dm.dm.type.MessageType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath text = createString("text");

    public QDm(String variable) {
        this(Dm.class, forVariable(variable), INITS);
    }

    public QDm(Path<? extends Dm> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDm(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDm(PathMetadata metadata, PathInits inits) {
        this(Dm.class, metadata, inits);
    }

    public QDm(Class<? extends Dm> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dmroom = inits.isInitialized("dmroom") ? new com.zerobase.foodlier.module.dm.room.domain.model.QDmRoom(forProperty("dmroom"), inits.get("dmroom")) : null;
    }

}

