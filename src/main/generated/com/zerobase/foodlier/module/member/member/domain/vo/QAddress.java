package com.zerobase.foodlier.module.member.member.domain.vo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAddress is a Querydsl query type for Address
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QAddress extends BeanPath<Address> {

    private static final long serialVersionUID = -1578832443L;

    public static final QAddress address = new QAddress("address");

    public final StringPath addressDetail = createString("addressDetail");

    public final NumberPath<Double> lat = createNumber("lat", Double.class);

    public final NumberPath<Double> lnt = createNumber("lnt", Double.class);

    public final StringPath roadAddress = createString("roadAddress");

    public QAddress(String variable) {
        super(Address.class, forVariable(variable));
    }

    public QAddress(Path<? extends Address> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAddress(PathMetadata metadata) {
        super(Address.class, metadata);
    }

}

