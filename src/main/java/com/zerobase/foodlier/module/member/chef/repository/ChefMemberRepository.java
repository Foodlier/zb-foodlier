package com.zerobase.foodlier.module.member.chef.repository;

import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.chef.dto.AroundChefDto;
import com.zerobase.foodlier.module.member.chef.dto.RequestedChefDto;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChefMemberRepository extends JpaRepository<ChefMember, Long> {
    boolean existsByMember(Member member);

    @Query(
            value = "SELECT c.id as chefId, c.introduce as introduce, c.star_avg as starAvg,\n" +
                    "c.review_count as reviewCount, m.profile_url as profileUrl, m.nickname as nickname,\n" +
                    "ROUND(st_distance_sphere(point(m.lnt, m.lat), point(rm.lnt, rm.lat))/1000, 2) as distance,\n" +
                    "rq.id as requestId, IFNULL(rp.is_quotation, false) as isQuotation, rp.id as quotationId\n" +
                    "FROM member rm\n" +
                    "RIGHT JOIN cook_request rq ON rq.member_id = rm.id\n" +
                    "LEFT JOIN chef_member c ON c.id = rq.chef_member_id\n" +
                    "JOIN member m ON m.id = c.member_id\n" +
                    "LEFT JOIN recipe rp ON rp.id = rq.recipe_id\n" +
                    "where rm.id = :requester AND rq.is_paid = false AND rq.dm_room_id is null\n" +
                    "limit :start, :end"
            ,nativeQuery = true
    )
    List<RequestedChefDto> findRequestedChef(
        @Param("requester") Long memberId,
        @Param("start") int start,
        @Param("end") int end
    );

    String baseAroundSearchQuery = "SELECT c.id as chefId, c.introduce as introduce, c.star_avg as starAvg,\n" +
            "c.review_count as reviewCount, m.profile_url as profileUrl, m.nickname as nickname,\n" +
            "ROUND(st_distance_sphere(point(m.lnt, m.lat), point(:lnt, :lat))/1000, 2) as distance\n" +
            "FROM chef_member c\n" +
            "LEFT JOIN member m ON m.chef_member_id = c.id\n" +
            "WHERE m.id <> :requester\n" +
            "AND ST_CONTAINS(\n" +
            "    ST_BUFFER(POINT(:lat, :lnt), :distance),\n" +
            "    POINT(m.lat, m.lnt))\n" +
            "AND c.id NOT IN\n" +
            "(\n" +
            "\tSELECT c.id\n" +
            "\tFROM member rm\n" +
            "\tRIGHT JOIN cook_request rq ON rq.member_id = rm.id\n" +
            "\tLEFT JOIN chef_member c ON c.id = rq.chef_member_id\n" +
            "\twhere rm.id = :requester AND rq.is_paid = false AND rq.dm_room_id is null\n" +
            ")\n";
    @Query(
            value = baseAroundSearchQuery +
                    "ORDER BY distance ASC\n" +
                    "LIMIT :start, :end",
            nativeQuery = true
    )
    List<AroundChefDto> findAroundChefOrderByDistance(
            @Param("requester") Long memberId,
            @Param("lat") double lat,
            @Param("lnt") double lnt,
            @Param("distance") double distance,
            @Param("start") int start,
            @Param("end") int end
    );

    @Query(
            value = baseAroundSearchQuery +
                    "ORDER BY reviewCount ASC\n" +
                    "LIMIT :start, :end",
            nativeQuery = true
    )
    List<AroundChefDto> findAroundChefOrderByReview(
            @Param("requester") Long memberId,
            @Param("lat") double lat,
            @Param("lnt") double lnt,
            @Param("distance") double distance,
            @Param("start") int start,
            @Param("end") int end
    );

    @Query(
            value = baseAroundSearchQuery +
                    "ORDER BY starAvg ASC\n" +
                    "LIMIT :start, :end",
            nativeQuery = true
    )
    List<AroundChefDto> findAroundChefOrderByStar(
            @Param("requester") Long memberId,
            @Param("lat") double lat,
            @Param("lnt") double lnt,
            @Param("distance") double distance,
            @Param("start") int start,
            @Param("end") int end
    );



}
