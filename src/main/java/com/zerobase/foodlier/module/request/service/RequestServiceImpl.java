package com.zerobase.foodlier.module.request.service;

import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.chef.exception.ChefMemberException;
import com.zerobase.foodlier.module.member.chef.repository.ChefMemberRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.request.domain.model.Request;
import com.zerobase.foodlier.module.request.domain.vo.Ingredient;
import com.zerobase.foodlier.module.request.dto.RequestDetailDto;
import com.zerobase.foodlier.module.request.exception.RequestException;
import com.zerobase.foodlier.module.request.repository.RequestRepository;
import com.zerobase.foodlier.module.requestform.domain.model.RequestForm;
import com.zerobase.foodlier.module.requestform.exception.RequestFormException;
import com.zerobase.foodlier.module.requestform.repository.RequestFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

import static com.zerobase.foodlier.module.member.chef.exception.ChefMemberErrorCode.CHEF_MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.request.exception.RequestErrorCode.*;
import static com.zerobase.foodlier.module.requestform.exception.RequestFormErrorCode.REQUEST_FORM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestFormRepository requestFormRepository;
    private final ChefMemberRepository chefMemberRepository;
    private final MemberRepository memberRepository;

    /**
     *  작성자 : 전현서
     *  작성일 : 2023-09-28
     *  Request에 DmRoom을 할당함.
     */
    @Override
    public void setDmRoom(Request request, DmRoom dmRoom){
        request.setDmRoom(dmRoom);
        requestRepository.save(request);
    }

    @Override
    public RequestDetailDto getRequestDetail(Long memberId, Long requestId){
        Request request = getRequest(requestId);
        Member member = getMember(memberId);

        validateGetRequestDetail(member, request);

        RequestDetailDto.RequestDetailDtoBuilder builder = RequestDetailDto.builder()
                .requestId(request.getId())
                .requesterNickname(request.getMember().getNickname())
                .title(request.getTitle())
                .content(request.getContent())
                .ingredientList(request.getIngredientList().stream()
                        .map(Ingredient::getIngredientName)
                        .collect(Collectors.toList()))
                .expectedPrice(request.getExpectedPrice())
                .expectedAt(request.getExpectedAt())
                .address(request.getMember().getAddress().getRoadAddress())
                .addressDetail(request.getMember().getAddress().getAddressDetail());

        if(!Objects.isNull(request.getRecipe())){
            builder.mainImageUrl(request.getRecipe().getMainImageUrl())
                    .recipeTitle(request.getRecipe().getSummary().getTitle())
                    .recipeContent(request.getRecipe().getSummary().getContent())
                    .heartCount(request.getRecipe().getHeartCount());
        }

        return builder.build();
    }

    /**
     *  작성자 : 전현서
     *  작성일 : 2023-10-02
     *  Request에 Quotation을 할당함.
     */
    @Override
    public void setQuotation(Long requestId, Recipe quotation) {
        Request request = getRequest(requestId);

        if(request.getRecipe() != null){
            throw new RequestException(ALREADY_SETTED_QUOTATION);
        }

        request.setRecipe(quotation);
        requestRepository.save(request);
    }

    /**
     *  작성자 : 이승현 (전현서)
     *  작성일 : 2023-09-27 (2023-09-28)
     *
     *  요청을 보내는 메서드, 같은 요리사에게는 중복 요청 X
     */
    @Override
    public void sendRequest(Long memberId, Long requestFormId, Long chefMemberId) {
        Member member = getMember(memberId);

        ChefMember chefMember = chefMemberRepository.findById(chefMemberId)
                .orElseThrow(() -> new ChefMemberException(CHEF_MEMBER_NOT_FOUND));

        validateSendRequest(member, chefMember);

        RequestForm requestForm = requestFormRepository.findByIdAndMember(requestFormId, member)
                .orElseThrow(() -> new RequestFormException(REQUEST_FORM_NOT_FOUND));

        Request request = Request.builder()
                .member(member)
                .chefMember(chefMember)
                .title(requestForm.getTitle())
                .content(requestForm.getContent())
                .ingredientList(
                        requestForm.getIngredientList().stream()
                                .map(x -> new Ingredient(x.getIngredientName()))
                                .collect(Collectors.toList())
                )
                .expectedPrice(requestForm.getExpectedPrice())
                .expectedAt(requestForm.getExpectedAt())
                .recipe(requestForm.getRecipe())
                .isPaid(false)
                .build();

        requestRepository.save(request);
    }

    /**
     *  작성자 : 이승현 (전현서)
     *  작성일 : 2023-09-27 (2023-09-28)
     *  요청자가 보낸 요청을 취소함, 성사된 요청 취소 X
     */
    @Override
    public void cancelRequest(Long memberId, Long requestId) {
        Member member = getMember(memberId);
        Request request = getRequest(requestId);

        validateCancelRequest(member, request);

        requestRepository.delete(request);
    }

    /**
     *  작성자 : 전현서
     *  작성일 : 2023-09-28
     *  요청자가 견적서를 보고 해당 요청에 대해 승인을 수행함.
     */
    @Override
    public Request requesterApproveRequest(Long memberId, Long requestId) {
        Member member = getMember(memberId);
        Request request = getRequest(requestId);

        validateRequesterApproveRequest(member, request);

        return request;
    }

    /**
     *  작성자 : 전현서
     *  작성일 : 2023-09-28
     *  요리사가 요청을 보고 수락함 (태깅된 요청에 한해서)
     */
    @Override
    public Request chefApproveRequest(Long memberId, Long requestId) {
        Member member = getMember(memberId);
        Request request = getRequest(requestId);

        validateChefApproveRequest(member, request);
        return request;
    }

    /**
     *  작성자 : 이승현 (전현서)
     *  작성일 : 2023-09-27 (2023-09-28)
     *  요리사가 요청을 거절함.
     */
    @Override
    public void rejectRequest(Long memberId, Long requestId) {
        Member member = getMember(memberId);
        Request request = getRequest(requestId);

        validateRejectRequest(member, request);
        requestRepository.delete(request);
    }

    private Member getMember(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    private Request getRequest(Long requestId){
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestException(REQUEST_NOT_FOUND));
    }


    // ===================== Validates =======================

    private void validateGetRequestDetail(Member member, Request request){

        //요청서에 요리사가 할당되지 않았을 경우는 존재할 수 없는 ID를 할당함.
        Long chefMemberId = Objects.isNull(request.getChefMember()) ? -1L :
                request.getChefMember().getMember().getId();

        if(!Objects.equals(request.getMember().getId(), member.getId()) &&
                !Objects.equals(chefMemberId, member.getId())){
            throw new RequestException(CANNOT_ACCESS_REQUEST);
        }
    }

    private void validateSendRequest(Member member, ChefMember chefMember){

        if(Objects.equals(member.getId(), chefMember.getMember().getId())){
            throw new RequestException(CANNOT_REQUEST_TO_ME);
        }
        if(requestRepository.existsByMemberAndChefMemberAndIsPaidFalse(
                member, chefMember
        )){
            throw new RequestException(ALREADY_REQUESTED_CHEF);
        }
    }

    private void validateCancelRequest(Member member, Request request){
        if(!Objects.equals(request.getMember().getId(), member.getId())){
            throw new RequestException(MEMBER_REQUEST_NOT_MATCH);
        }
        if(request.getDmRoom() != null){
            throw new RequestException(CANNOT_CANCEL_APPROVED);
        }
        if(request.isPaid()){
            throw new RequestException(CANNOT_CANCEL_IS_PAID);
        }
    }

    private void validateRequesterApproveRequest(Member member, Request request){
        if(!Objects.equals(request.getMember().getId(), member.getId())){
            throw new RequestException(MEMBER_REQUEST_NOT_MATCH);
        }
        if(request.getRecipe() == null){
            throw new RequestException(CANNOT_REQUESTER_APPROVE_HAS_NOT_QUOTATION);
        }
        if(!request.getRecipe().getIsQuotation()){
            throw new RequestException(CANNOT_REQUESTER_APPROVE_IS_NOT_QUOTATION);
        }
        if(request.getDmRoom() != null){
            throw new RequestException(ALREADY_APPROVED);
        }
    }

    private void validateChefApproveRequest(Member member, Request request){
        if(!Objects.equals(request.getChefMember().getMember().getId(), member.getId())){
            throw new RequestException(CHEF_MEMBER_REQUEST_NOT_MATCH);
        }
        if(request.getRecipe() == null){
            throw new RequestException(CANNOT_CHEF_APPROVE_HAS_NOT_RECIPE);
        }
        if(request.getRecipe().getIsQuotation()){
            throw new RequestException(CANNOT_CHEF_APPROVE_IS_QUOTATION);
        }
        if(request.getDmRoom() != null){
            throw new RequestException(ALREADY_APPROVED);
        }
    }

    private void validateRejectRequest(Member member, Request request){
        if(!Objects.equals(request.getChefMember().getMember().getId(), member.getId())){
            throw new RequestException(CHEF_MEMBER_REQUEST_NOT_MATCH);
        }
        if(request.getDmRoom() != null){
            throw new RequestException(CANNOT_REJECT_APPROVED);
        }
        if(request.isPaid()){
            throw new RequestException(CANNOT_REJECT_IS_PAID);
        }
    }
}
