package com.zerobase.foodlier.module.member.member.repository;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.dto.RequestedMemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsByPhoneNumber(String phoneNumber);

    String baseRequestedMemberQuery =
            "SELECT m.id as memberId, m.profile_url as profileUrl, m.nickname as nickname,\n" +
            "ROUND(st_distance_sphere(point(m.lnt, m.lat), point(:lnt, :lat))/1000, 2) as distance,\n" +
            "m.lat as lat, m.lnt as lnt, r.id as requestId, r.title as title, r.expected_price as expectedPrice, \n" +
            "rp.main_image_url as mainImageUrl\n" +
            "FROM chef_member cm\n" +
            "JOIN cook_request r ON r.chef_member_id = cm.id AND r.is_paid = false AND r.dm_room_id IS NULL\n" +
            "JOIN member m ON m.id = r.member_id\n" +
            "LEFT JOIN recipe rp ON rp.id = r.recipe_id\n" +
            "WHERE cm.id = :chefMemberId\n";
    String requestedMemberCountQuery =
            "SELECT COUNT(*)\n" +
            "FROM chef_member cm\n" +
            "JOIN cook_request r ON r.chef_member_id = cm.id AND r.is_paid = false AND r.dm_room_id IS NULL\n" +
            "JOIN member m ON m.id = r.member_id\n" +
            "LEFT JOIN recipe rp ON rp.id = r.recipe_id\n" +
            "WHERE cm.id = :chefMemberId\n";

    @Query(
            value = baseRequestedMemberQuery + "ORDER BY distance ASC",
            countQuery = requestedMemberCountQuery,
            nativeQuery = true
    )
    Page<RequestedMemberDto> getRequestedMemberListOrderByDistance(
            @Param("chefMemberId")Long chefMemberId,
            @Param("lat")double lat,
            @Param("lnt")double lnt,
            Pageable pageable
    );
    @Query(
            value = baseRequestedMemberQuery + "ORDER BY expectedPrice ASC",
            countQuery = requestedMemberCountQuery,
            nativeQuery = true
    )
    Page<RequestedMemberDto> getRequestedMemberListOrderByPrice(
            @Param("chefMemberId")Long chefMemberId,
            @Param("lat")double lat,
            @Param("lnt")double lnt,
            Pageable pageable
    );


}
