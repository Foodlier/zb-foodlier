package com.zerobase.foodlier.module.request.service;

import com.zerobase.foodlier.module.member.chef.repository.ChefMemberRepository;
import com.zerobase.foodlier.module.request.domain.model.Request;
import com.zerobase.foodlier.module.request.exception.RequestException;
import com.zerobase.foodlier.module.request.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.zerobase.foodlier.module.request.exception.RequestErrorCode.REQUEST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final ChefMemberRepository chefMemberRepository;

    @Override
    public void sendRequest(Long requestId, Long chefMemberId) {
        Request request = findById(requestId);
        request.setChefMember(chefMemberRepository.findById(chefMemberId)
                .orElseThrow());

        requestRepository.save(request);
    }

    @Override
    public void cancelRequest(Long requestId) {
        Request request = findById(requestId);
        request.setChefMember(null);

        requestRepository.save(request);
    }

    @Override
    public void rejectRequest(Long requestId) {
        Request request = findById(requestId);
        if (request.getRecipe().getIsQuotation()) {
            request.setRecipe(null);
        }
        request.setChefMember(null);

        requestRepository.save(request);
    }

    private Request findById(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(()->new RequestException(REQUEST_NOT_FOUND));
    }
}
