package com.zerobase.foodlier.module.dm.room.repository;

import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.dm.room.dto.DmRoomDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface DmRoomRepository extends JpaRepository<DmRoom, Long> {

    /**
     * DmRoomDto는 다음과 같은 값을 가져옵니다.
     * dm방 번호, 상대방 닉네임, 상대방 프로필이미지, 요청번호, 희망가격
     * 이 때 현재 나의 아이디가 포함되는 요청이면서
     * 요청에서의 아이디가 다른지 판별하여
     * 다르다면 닉네임과 프로필 이미지를 가져옵니다.
     */
    @Query("SELECT new com.zerobase.foodlier.module.dm.room.dto.DmRoomDto" +
            "(d.id, " +
            "CASE " +
            "  WHEN r.chefMember.member.id <> :requester THEN r.chefMember.member.nickname " +
            "  ELSE r.member.nickname " +
            "END, " +
            "CASE " +
            "  WHEN r.chefMember.member.id <> :requester THEN r.chefMember.member.profileUrl " +
            "  ELSE r.member.profileUrl " +
            "END, " +
            "r.id, " +
            "r.expectedPrice) " +
            "FROM DmRoom d " +
            "JOIN Request r ON (r.chefMember.member.id = :requester OR r.member.id = :requester)")
    Page<DmRoomDto> getDmRoomPage(@Param("requester") Long id, Pageable pageable);
}
