package com.zerobase.foodlier.module.request.service;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.chef.exception.ChefMemberException;
import com.zerobase.foodlier.module.member.chef.repository.ChefMemberRepository;
import com.zerobase.foodlier.module.request.domain.model.Request;
import com.zerobase.foodlier.module.request.exception.RequestException;
import com.zerobase.foodlier.module.request.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.zerobase.foodlier.module.member.chef.exception.ChefMemberErrorCode.CHEF_MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.request.exception.RequestErrorCode.*;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final ChefMemberRepository chefMemberRepository;

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-09-27
     * 요청서를 보냅니다.
     */
    @Override
    public void sendRequest(MemberAuthDto memberAuthDto, Long requestId, Long chefMemberId) {
        Request request = findById(requestId);
        validRequestMember(memberAuthDto, request);
        request.setChefMember(chefMemberRepository.findById(chefMemberId)
                .orElseThrow(() -> new ChefMemberException(CHEF_MEMBER_NOT_FOUND)));

        requestRepository.save(request);
    }

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-09-27
     * 요청을 거절합니다.
     */
    @Override
    public void cancelRequest(MemberAuthDto memberAuthDto, Long requestId) {
        Request request = findById(requestId);
        validRequestMember(memberAuthDto, request);
        request.setChefMember(null);

        requestRepository.save(request);
    }

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-09-27
     * 요청을 수락합니다.
     */
    @Override
    public Request approveRequest(MemberAuthDto memberAuthDto, Long requestId) {
        Request request = findById(requestId);
        if (request.getRecipe().getIsQuotation()) {
            validRequestMember(memberAuthDto, request);
        } else {
            ChefMember chefMember = chefMemberRepository.findById(memberAuthDto.getId())
                    .orElseThrow(() -> new ChefMemberException(CHEF_MEMBER_NOT_FOUND));
            validRequestChefMember(chefMember.getId(), request);
        }

        return request;
    }

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-09-27
     * 요청을 거절합니다.
     */
    @Override
    public void rejectRequest(MemberAuthDto memberAuthDto, Long requestId) {
        Request request = findById(requestId);
        if (request.getRecipe().getIsQuotation()) {
            validRequestMember(memberAuthDto, request);
            request.setRecipe(null);
        } else {
            ChefMember chefMember = chefMemberRepository.findById(memberAuthDto.getId())
                    .orElseThrow(() -> new ChefMemberException(CHEF_MEMBER_NOT_FOUND));
            validRequestChefMember(chefMember.getId(), request);
        }
        request.setChefMember(null);

        requestRepository.save(request);
    }

    private Request findById(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestException(REQUEST_NOT_FOUND));
    }

    private void validRequestMember(MemberAuthDto memberAuthDto, Request request) {
        if (!Objects.equals(memberAuthDto.getId(), request.getMember().getId())) {
            throw new RequestException(MEMBER_REQUEST_NOT_MATCH);
        }
    }

    private void validRequestChefMember(Long chefMemberId, Request request) {
        if (!Objects.equals(chefMemberId, request.getChefMember().getId())) {
            throw new RequestException(CHEF_MEMBER_REQUEST_NOT_MATCH);
        }
    }
}
