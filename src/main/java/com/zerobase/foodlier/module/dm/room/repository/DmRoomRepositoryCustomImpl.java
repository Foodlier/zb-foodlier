package com.zerobase.foodlier.module.dm.room.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.foodlier.module.dm.room.domain.model.QDmRoom;
import com.zerobase.foodlier.module.dm.room.dto.DmRoomDto;
import com.zerobase.foodlier.module.request.domain.model.QRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class DmRoomRepositoryCustomImpl implements DmRoomRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<DmRoomDto> getDmRoomPage(Long memberId, Pageable pageable) {
        QDmRoom dmRoom = QDmRoom.dmRoom;
        QRequest request = QRequest.request;

        List<DmRoomDto> content = queryFactory.select(Projections.constructor(DmRoomDto.class,
                        dmRoom.id.as("roomId"),
                        new CaseBuilder().when(request.chefMember.member.id.ne(memberId))
                                .then(request.chefMember.member.nickname)
                                .otherwise(request.member.nickname),
                        new CaseBuilder().when(request.chefMember.member.id.ne(memberId))
                                .then(request.chefMember.member.profileUrl)
                                .otherwise(request.member.profileUrl),
                        dmRoom.request.id, request.expectedPrice,
                        new CaseBuilder().when(request.chefMember.member.id.ne(memberId))
                                .then((ComparableExpression<Boolean>) dmRoom.isChefExit)
                                .otherwise(dmRoom.isMemberExit).as("isExit"),
                        dmRoom.suggestion.isSuggested,
                        new CaseBuilder().when(request.chefMember.member.id.ne(memberId))
                                .then("chef")
                                .otherwise("requester")))
                .from(dmRoom)
                .join(request)
                .on(dmRoom.request.eq(request))
                .where(request.chefMember.member.id.eq(memberId)
                        .and(Objects.requireNonNull(dmRoom.isChefExit).isFalse())
                        .or(request.member.id.eq(memberId)
                                .and(Objects.requireNonNull(dmRoom.isMemberExit).isFalse())))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content);
    }
}
