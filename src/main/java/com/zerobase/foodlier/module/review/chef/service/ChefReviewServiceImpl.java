package com.zerobase.foodlier.module.review.chef.service;

import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.chef.exception.ChefMemberException;
import com.zerobase.foodlier.module.member.chef.repository.ChefMemberRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.request.domain.model.Request;
import com.zerobase.foodlier.module.request.exception.RequestException;
import com.zerobase.foodlier.module.request.repository.RequestRepository;
import com.zerobase.foodlier.module.review.chef.domain.model.ChefReview;
import com.zerobase.foodlier.module.review.chef.dto.ChefReviewForm;
import com.zerobase.foodlier.module.review.chef.dto.ChefReviewResponseDto;
import com.zerobase.foodlier.module.review.chef.exception.ChefReviewException;
import com.zerobase.foodlier.module.review.chef.repository.ChefReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.zerobase.foodlier.module.member.chef.exception.ChefMemberErrorCode.CHEF_MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.request.exception.RequestErrorCode.REQUEST_NOT_FOUND;
import static com.zerobase.foodlier.module.review.chef.exception.ChefReviewErrorCode.*;

@Service
@RequiredArgsConstructor
public class ChefReviewServiceImpl {

    private final ChefReviewRepository chefReviewRepository;
    private final RequestRepository requestRepository;
    private final MemberRepository memberRepository;
    private final ChefMemberRepository chefMemberRepository;

    public void createChefReview(Long memberId, Long requestId,
                                 ChefReviewForm form){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        Request request = requestRepository.findByIdAndMember(requestId, member)
                .orElseThrow(() -> new RequestException(REQUEST_NOT_FOUND));

        validateCreateChefReview(member, request);

        ChefReview chefReview = ChefReview.builder()
                .request(request)
                .content(form.getContent())
                .star(form.getStar())
                .build();

        chefReviewRepository.save(chefReview);
    }

    public List<ChefReviewResponseDto> getChefReviewList(Long chefMemberId,
                                                         Pageable pageable){

        ChefMember chefMember = chefMemberRepository.findById(chefMemberId)
                .orElseThrow(() -> new ChefMemberException(CHEF_MEMBER_NOT_FOUND));

        return chefReviewRepository.findByChefMember(chefMember, pageable)
                .getContent()
                .stream()
                .map(ChefReviewResponseDto::from)
                .collect(Collectors.toList());
    }

    //=============== Validates ===================

    private void validateCreateChefReview(Member member, Request request){

        if(!Objects.equals(request.getMember().getId(), member.getId())){
            throw new ChefReviewException(YOU_ARE_NOT_REQUESTER);
        }

        if(!request.isPaid()){
            throw new ChefReviewException(IS_NOT_CLOSED_REQUEST);
        }

        if(chefReviewRepository.existsByRequest(request)){
            throw new ChefReviewException(ALREADY_REVIEWED_TO_CHEF);
        }
    }

}
