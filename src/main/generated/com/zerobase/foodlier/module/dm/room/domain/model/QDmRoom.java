package com.zerobase.foodlier.module.dm.room.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDmRoom is a Querydsl query type for DmRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDmRoom extends EntityPathBase<DmRoom> {

    private static final long serialVersionUID = -1743227679L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDmRoom dmRoom = new QDmRoom("dmRoom");

    public final com.zerobase.foodlier.common.jpa.audit.QAudit _super = new com.zerobase.foodlier.common.jpa.audit.QAudit(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isChefExit = createBoolean("isChefExit");

    public final BooleanPath isMemberExit = createBoolean("isMemberExit");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.zerobase.foodlier.module.request.domain.model.QRequest request;

    public final com.zerobase.foodlier.module.dm.room.domain.vo.QSuggestion suggestion;

    public QDmRoom(String variable) {
        this(DmRoom.class, forVariable(variable), INITS);
    }

    public QDmRoom(Path<? extends DmRoom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDmRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDmRoom(PathMetadata metadata, PathInits inits) {
        this(DmRoom.class, metadata, inits);
    }

    public QDmRoom(Class<? extends DmRoom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.request = inits.isInitialized("request") ? new com.zerobase.foodlier.module.request.domain.model.QRequest(forProperty("request"), inits.get("request")) : null;
        this.suggestion = inits.isInitialized("suggestion") ? new com.zerobase.foodlier.module.dm.room.domain.vo.QSuggestion(forProperty("suggestion")) : null;
    }

}

