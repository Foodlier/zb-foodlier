package com.zerobase.foodlier.module.transaction.service;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.dm.room.domain.vo.Suggestion;
import com.zerobase.foodlier.module.dm.room.exception.DmRoomException;
import com.zerobase.foodlier.module.dm.room.repository.DmRoomRepository;
import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.chef.exception.ChefMemberException;
import com.zerobase.foodlier.module.member.chef.repository.ChefMemberRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.request.domain.model.Request;
import com.zerobase.foodlier.module.request.exception.RequestException;
import com.zerobase.foodlier.module.request.repository.RequestRepository;
import com.zerobase.foodlier.module.transaction.dto.SuggestionForm;
import com.zerobase.foodlier.module.transaction.exception.TransactionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.zerobase.foodlier.module.dm.room.exception.DmRoomErrorCode.DM_ROOM_NOT_FOUND;
import static com.zerobase.foodlier.module.member.chef.exception.ChefMemberErrorCode.CHEF_MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.request.exception.RequestErrorCode.REQUEST_NOT_FOUND;
import static com.zerobase.foodlier.module.transaction.exception.TransactionErrorCode.NOT_ENOUGH_POINT;
import static com.zerobase.foodlier.module.transaction.exception.TransactionErrorCode.SUGGESTION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final DmRoomRepository dmRoomRepository;
    private final MemberRepository memberRepository;
    private final ChefMemberRepository chefMemberRepository;
    private final RequestRepository requestRepository;

    @Override
    public String sendSuggestion(MemberAuthDto memberAuthDto,
                                 SuggestionForm form,
                                 Long requestMemberId) {
        ChefMember chefMember = chefMemberRepository.findByMemberId(memberAuthDto.getId())
                .orElseThrow(() -> new ChefMemberException(CHEF_MEMBER_NOT_FOUND));
        DmRoom dmRoom = getDmRoom(requestMemberId, chefMember.getId());
        dmRoom.setSuggestion(Suggestion.builder()
                .suggestedPrice(form.getSuggestedPrice())
                .isAccept(false)
                .isSuggested(true)
                .build());

        dmRoomRepository.save(dmRoom);

        return "가격을 제안했습니다.";
    }

    @Override
    public String approveSuggestion(MemberAuthDto memberAuthDto, Long chefMemberId) {
        Member requestMember = memberRepository.findById(memberAuthDto.getId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        ChefMember chefMember = chefMemberRepository.findById(chefMemberId)
                .orElseThrow(() -> new ChefMemberException(CHEF_MEMBER_NOT_FOUND));
        DmRoom dmRoom = getDmRoom(memberAuthDto.getId(), chefMemberId);

        int suggestedPrice = dmRoom.getSuggestion().getSuggestedPrice();
        long requestMemberPoint = requestMember.getPoint();

        validApproveSuggestion(requestMemberPoint, suggestedPrice, dmRoom);

        requestMember.transaction(-suggestedPrice);
        memberRepository.save(requestMember);
        chefMember.getMember().transaction(+suggestedPrice);
        chefMemberRepository.save(chefMember);
        dmRoom.getSuggestion().setIsAccept(true);
        dmRoomRepository.save(dmRoom);

        return "제안을 수락하였습니다.";
    }

    @Override
    public String rejectSuggestion(MemberAuthDto memberAuthDto, Long chefMemberId) {
        DmRoom dmRoom = getDmRoom(memberAuthDto.getId(), chefMemberId);
        dmRoom.setSuggestion(Suggestion.builder()
                .suggestedPrice(0)
                .isAccept(false)
                .isSuggested(false)
                .build());

        validRejectSuggestion(dmRoom);

        dmRoomRepository.save(dmRoom);

        return "제안을 거절하였습니다.";
    }

    private DmRoom getDmRoom(Long memberId, Long chefMemberId) {
        Member requestMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        ChefMember chefMember = chefMemberRepository.findById(chefMemberId)
                .orElseThrow(() -> new ChefMemberException(CHEF_MEMBER_NOT_FOUND));
        Request request = requestRepository
                .findByMemberAndChefMember(requestMember, chefMember)
                .orElseThrow(() -> new RequestException(REQUEST_NOT_FOUND));
        return dmRoomRepository.findByRequest(request)
                .orElseThrow(() -> new DmRoomException(DM_ROOM_NOT_FOUND));
    }

    private static void validApproveSuggestion(long requestMemberPoint, int suggestedPrice, DmRoom dmRoom) {
        if (!dmRoom.getSuggestion().getIsSuggested()){
            throw new TransactionException(SUGGESTION_NOT_FOUND);
        }
        if (requestMemberPoint < suggestedPrice) {
            throw new TransactionException(NOT_ENOUGH_POINT);
        }
    }

    private static void validRejectSuggestion(DmRoom dmRoom) {
        if (!dmRoom.getSuggestion().getIsSuggested()){
            throw new TransactionException(SUGGESTION_NOT_FOUND);
        }
    }
}
