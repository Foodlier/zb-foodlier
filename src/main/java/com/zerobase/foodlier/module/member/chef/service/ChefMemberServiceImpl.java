package com.zerobase.foodlier.module.member.chef.service;

import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.chef.dto.AroundChefDto;
import com.zerobase.foodlier.module.member.chef.dto.ChefIntroduceForm;
import com.zerobase.foodlier.module.member.chef.dto.RequestedChefDto;
import com.zerobase.foodlier.module.member.chef.exception.ChefMemberException;
import com.zerobase.foodlier.module.member.chef.repository.ChefMemberRepository;
import com.zerobase.foodlier.module.member.chef.type.GradeType;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.domain.vo.Address;
import com.zerobase.foodlier.module.member.member.exception.MemberErrorCode;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;

import static com.zerobase.foodlier.module.member.chef.exception.ChefMemberErrorCode.*;

@Service
@RequiredArgsConstructor
public class ChefMemberServiceImpl implements ChefMemberService{

    private static final double AROUND_DISTANCE = 0.01;
    private static final int MIN_RECIPES_FOR_CHEF = 3;
    private final ChefMemberRepository chefMemberRepository;
    private final MemberRepository memberRepository;
    private final RecipeRepository recipeRepository;

    /**
     *  작성자 : 전현서
     *  작성일 : 2023-09-26
     *  Recipe가 3개 이상인 회원에 대해 요리사를 등록
     */
    @Transactional
    public void registerChef(Long memberId, ChefIntroduceForm chefIntroduceForm){
        Member member = getMember(memberId);

        validateRegisterChef(member);

        ChefMember chefMember = ChefMember.builder()
                .member(member)
                .introduce(chefIntroduceForm.getIntroduce())
                .gradeType(GradeType.BRONZE)
                .build();

        ChefMember chefMemberEntity = chefMemberRepository.save(chefMember);
        member.setChefMember(chefMemberEntity);
        memberRepository.save(member);
    }

    /**
     *  작성자 : 전현서
     *  작성일 : 2023-09-26
     *  요리사의 소개를 변경함.
     */
    public void updateChefIntroduce(Long memberId, ChefIntroduceForm chefIntroduceForm){
        Member member = getMember(memberId);

        if(member.getChefMember() == null){
            throw new ChefMemberException(CHEF_MEMBER_NOT_FOUND);
        }

        ChefMember chefMember = member.getChefMember();
        chefMember.setIntroduce(chefIntroduceForm.getIntroduce());
        chefMemberRepository.save(chefMember);
    }

    /**
     *  작성자 : 전현서
     *  작성일 : 2023-09-29
     *  요청된 요리사의 정보를 가져옴.
     */
    public List<RequestedChefDto> getRequestedChefList(Long memberId,
                                                   int pageIdx, int pageSize){
        return chefMemberRepository.findRequestedChef(memberId, pageIdx * pageSize, pageSize);
    }

    /**
     *  작성자 : 전현서
     *  작성일 : 2023-09-29
     *  반경 1km내의 요리사 리스트를 페이징하여 반환
     */
    public List<AroundChefDto> getAroundChefList(Long memberId,
                                                  int pageIdx, int pageSize){
        Address address = getMember(memberId).getAddress();
        return chefMemberRepository.findAroundChef(memberId, address.getLat(),
                address.getLnt(), AROUND_DISTANCE, pageIdx * pageSize, pageSize);
    }

    private Member getMember(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    //================= Validates ====================

    private void validateRegisterChef(Member member){

        if(chefMemberRepository.existsByMember(member)){
            throw new ChefMemberException(ALREADY_REGISTER_CHEF);
        }

        if(recipeRepository.countByMember(member) < MIN_RECIPES_FOR_CHEF){
            throw new ChefMemberException(CANNOT_REGISTER_LESS_THAN_THREE_RECIPE);
        }
    }

}
