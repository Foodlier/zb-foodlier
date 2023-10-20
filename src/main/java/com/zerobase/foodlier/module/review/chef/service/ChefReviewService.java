package com.zerobase.foodlier.module.review.chef.service;

import com.zerobase.foodlier.common.response.ListResponse;
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

import java.util.Objects;

import static com.zerobase.foodlier.module.member.chef.exception.ChefMemberErrorCode.CHEF_MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.request.exception.RequestErrorCode.REQUEST_NOT_FOUND;
import static com.zerobase.foodlier.module.review.chef.exception.ChefReviewErrorCode.*;

@Service
@RequiredArgsConstructor
public class ChefReviewService {

    private final ChefReviewRepository chefReviewRepository;
    private final RequestRepository requestRepository;
    private final MemberRepository memberRepository;
    private final ChefMemberRepository chefMemberRepository;

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-10-06
     * 요청에 대해서 요리사에 대한 후기를 생성함.
     * 결제가 성사된 요청에만 리뷰를 남길 수 있고, Update, Delete는 허용 하지 않음.
     */
    public Long createChefReview(Long memberId, Long requestId,
                                 ChefReviewForm form) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        Request request = requestRepository.findByIdAndMember(requestId, member)
                .orElseThrow(() -> new RequestException(REQUEST_NOT_FOUND));

        validateCreateChefReview(member, request);

        ChefReview chefReview = ChefReview.builder()
                .request(request)
                .chefMember(request.getChefMember())
                .content(form.getContent())
                .star(form.getStar())
                .build();

        chefReviewRepository.save(chefReview);

        return request.getChefMember().getId();
    }

    /**
     * 작성자 : 전현서
     * 작성일 : 2023-10-06
     * 요리사에 대한 후기 목록을 조회함.
     */
    public ListResponse<ChefReviewResponseDto> getChefReviewList(Long chefMemberId,
                                                                 Pageable pageable) {

        ChefMember chefMember = chefMemberRepository.findById(chefMemberId)
                .orElseThrow(() -> new ChefMemberException(CHEF_MEMBER_NOT_FOUND));

        return ListResponse.from(
                chefReviewRepository.findByChefMemberOrderByCreatedAtDesc(chefMember, pageable),
                ChefReviewResponseDto::from);

    }

    //=============== Validates ===================

    private void validateCreateChefReview(Member member, Request request) {

        if (!Objects.equals(request.getMember().getId(), member.getId())) {
            throw new ChefReviewException(YOU_ARE_NOT_REQUESTER);
        }

        if (!request.isPaid()) {
            throw new ChefReviewException(IS_NOT_CLOSED_REQUEST);
        }

        if (chefReviewRepository.existsByRequest(request)) {
            throw new ChefReviewException(ALREADY_REVIEWED_TO_CHEF);
        }
    }

}
