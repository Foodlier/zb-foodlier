package com.zerobase.foodlier.module.member.chef.service;

import com.zerobase.foodlier.common.aop.RedissonLock;
import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.chef.dto.*;
import com.zerobase.foodlier.module.member.chef.exception.ChefMemberException;
import com.zerobase.foodlier.module.member.chef.repository.ChefMemberRepository;
import com.zerobase.foodlier.module.member.chef.type.ChefSearchType;
import com.zerobase.foodlier.module.member.chef.type.GradeType;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.domain.vo.Address;
import com.zerobase.foodlier.module.member.member.exception.MemberErrorCode;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.member.member.type.RoleType;
import com.zerobase.foodlier.module.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.zerobase.foodlier.module.member.chef.exception.ChefMemberErrorCode.*;

@Service
@RequiredArgsConstructor
public class ChefMemberServiceImpl implements ChefMemberService {

    private static final int EXPERIENCE_MULTIPLIER = 100; //경험치 배수

    private static final double AROUND_DISTANCE = 0.012;
    private static final int MIN_RECIPES_FOR_CHEF = 3;
    private final ChefMemberRepository chefMemberRepository;
    private final MemberRepository memberRepository;
    private final RecipeRepository recipeRepository;

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-09-26
     * Recipe가 3개 이상인 회원에 대해 요리사를 등록
     */
    @Transactional
    public void registerChef(Long memberId, ChefIntroduceForm chefIntroduceForm) {
        Member member = getMember(memberId);

        validateRegisterChef(member);

        ChefMember chefMember = ChefMember.builder()
                .member(member)
                .introduce(chefIntroduceForm.getIntroduce())
                .gradeType(GradeType.BRONZE)
                .build();

        ChefMember chefMemberEntity = chefMemberRepository.save(chefMember);
        member.updateChefMember(chefMemberEntity);
        member.getRoles().add(RoleType.ROLE_CHEF.name());
        memberRepository.save(member);
    }

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-09-26
     * 요리사의 소개를 변경함.
     */
    public void updateChefIntroduce(Long memberId, ChefIntroduceForm chefIntroduceForm) {
        Member member = getMember(memberId);

        if (member.getChefMember() == null) {
            throw new ChefMemberException(CHEF_MEMBER_NOT_FOUND);
        }

        ChefMember chefMember = member.getChefMember();
        chefMember.updateIntroduce(chefIntroduceForm.getIntroduce());
        chefMemberRepository.save(chefMember);
    }

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-09-29
     * 요청된 요리사의 정보를 가져옴.
     */
    @Override
    public ListResponse<RequestedChefDto> getRequestedChefList(Long memberId,
                                                               Pageable pageable) {
        return ListResponse.from(
                chefMemberRepository.findRequestedChef(memberId, pageable));
    }

    @Override
    public List<TopChefDto> getTopChefList() {
        return chefMemberRepository.findTop5ByOrderByExpDesc()
                .stream()
                .map(TopChefDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-10-08
     * 요리사의 등급 정보를 가져옴.
     */
    @Override
    public ChefProfileDto getChefProfile(Long chefMemberId) {
        return ChefProfileDto.from(chefMemberRepository.findById(chefMemberId)
                .orElseThrow(() -> new ChefMemberException(CHEF_MEMBER_NOT_FOUND)));
    }

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-10-05
     * 요리사의 경험치를 올려줍니다.
     */
    @RedissonLock(group = "chefexp", key = "#chefMemberId")
    public void plusExp(Long chefMemberId, int star) {
        ChefMember chefMember = getChefMember(chefMemberId);
        chefMember.plusExp(star * EXPERIENCE_MULTIPLIER);
        chefMemberRepository.save(chefMember);
    }

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-10-05
     * 요리사의 별점을 올리고, 평균 별점을 계산합니다.
     */
    @RedissonLock(group = "chefstar", key = "#chefMemberId")
    public void plusStar(Long chefMemberId, int star) {
        ChefMember chefMember = getChefMember(chefMemberId);
        chefMember.plusStar(star);
        chefMemberRepository.save(chefMember);
    }

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-09-29
     * 반경 1km내의 요리사 리스트를 페이징하여 반환
     * type이 지정되지 않은 경우는 기본적으로 가까운 거리순
     */
    @Override
    public ListResponse<AroundChefDto> getAroundChefList(Long memberId,
                                                         Pageable pageable,
                                                         ChefSearchType type) {
        Address address = getMember(memberId).getAddress();

        return ListResponse.from(chefMemberRepository.findAroundChefOrderByType(memberId, address.getLat(),
                address.getLnt(), AROUND_DISTANCE, pageable, type));
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    private ChefMember getChefMember(Long chefMemberId) {
        return chefMemberRepository.findById(chefMemberId)
                .orElseThrow(() -> new ChefMemberException(CHEF_MEMBER_NOT_FOUND));
    }

    //================= Validates ====================

    private void validateRegisterChef(Member member) {

        if (chefMemberRepository.existsByMember(member)) {
            throw new ChefMemberException(ALREADY_REGISTER_CHEF);
        }

        if (recipeRepository.countByMember(member) < MIN_RECIPES_FOR_CHEF) {
            throw new ChefMemberException(CANNOT_REGISTER_LESS_THAN_THREE_RECIPE);
        }
    }

}
