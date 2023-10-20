package com.zerobase.foodlier.module.transaction.service;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.dm.room.domain.vo.Suggestion;
import com.zerobase.foodlier.module.dm.room.exception.DmRoomException;
import com.zerobase.foodlier.module.dm.room.repository.DmRoomRepository;
import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.transaction.dto.SuggestionForm;
import com.zerobase.foodlier.module.transaction.dto.TransactionDto;
import com.zerobase.foodlier.module.transaction.exception.TransactionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.zerobase.foodlier.module.dm.room.exception.DmRoomErrorCode.DM_ROOM_NOT_FOUND;
import static com.zerobase.foodlier.module.transaction.exception.TransactionErrorCode.*;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final DmRoomRepository dmRoomRepository;

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-10-04
     * 요리사가 요청자에게 제안을 보냅니다.
     */
    public String sendSuggestion(MemberAuthDto memberAuthDto,
                                 SuggestionForm form,
                                 Long dmRoomId) {
        DmRoom dmRoom = dmRoomRepository.findById(dmRoomId)
                .orElseThrow(() -> new DmRoomException(DM_ROOM_NOT_FOUND));

        validSendSuggestion(memberAuthDto, dmRoom);

        dmRoom.updateSuggestion(Suggestion.builder()
                .suggestedPrice(form.getSuggestedPrice())
                .isAccept(false)
                .isSuggested(true)
                .build());

        dmRoomRepository.save(dmRoom);

        return "가격을 제안했습니다.";
    }

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-10-05
     * 요리사가 제안을 취소힙니다.
     */
    public String cancelSuggestion(MemberAuthDto memberAuthDto, Long dmRoomId) {
        DmRoom dmRoom = dmRoomRepository.findById(dmRoomId)
                .orElseThrow(() -> new DmRoomException(DM_ROOM_NOT_FOUND));

        validCancelSuggestion(memberAuthDto, dmRoom);

        dmRoom.updateSuggestion(Suggestion.builder()
                .suggestedPrice(0)
                .isSuggested(false)
                .isAccept(false)
                .build());

        dmRoomRepository.save(dmRoom);

        return "제안이 취소되었습니다.";
    }

    /**
     * 작성자 : 이승현 (전현서)
     * 작성일 : 2023-10-04 (2023-10-14)
     * 요청자가 제안을 수락합니다.
     */
    public TransactionDto approveSuggestion(MemberAuthDto memberAuthDto, Long dmRoomId) {
        DmRoom dmRoom = dmRoomRepository.findById(dmRoomId)
                .orElseThrow(() -> new DmRoomException(DM_ROOM_NOT_FOUND));
        Member requestMember = dmRoom.getRequest().getMember();
        ChefMember chefMember = dmRoom.getRequest().getChefMember();

        int suggestedPrice = dmRoom.getSuggestion().getSuggestedPrice();
        long requestMemberPoint = requestMember.getPoint();

        validApproveSuggestion(memberAuthDto, requestMember, dmRoom, requestMemberPoint, suggestedPrice);

        dmRoom.getRequest().getMember().transaction(-suggestedPrice);
        dmRoom.getRequest().getChefMember().getMember().transaction(suggestedPrice);
        dmRoom.getSuggestion().updateAccept(true);
        dmRoom.getRequest().updatePaid(true);
        dmRoom.getRequest().updateFinished(true);
        dmRoomRepository.save(dmRoom);

        return TransactionDto.builder()
                .requestMember(requestMember)
                .chefMember(chefMember.getMember())
                .changePoint(suggestedPrice)
                .build();
    }

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-10-04
     * 요청자가 제안을 거절합니다.
     */
    public String rejectSuggestion(MemberAuthDto memberAuthDto, Long dmRoomId) {
        DmRoom dmRoom = dmRoomRepository.findById(dmRoomId)
                .orElseThrow(() -> new DmRoomException(DM_ROOM_NOT_FOUND));

        validRejectSuggestion(memberAuthDto, dmRoom);

        dmRoom.updateSuggestion(Suggestion.builder()
                .suggestedPrice(0)
                .isAccept(false)
                .isSuggested(false)
                .build());

        dmRoomRepository.save(dmRoom);

        return "제안을 거절하였습니다.";
    }

    private static void validSendSuggestion(MemberAuthDto memberAuthDto, DmRoom dmRoom) {
        if (!Objects.equals(memberAuthDto.getId(),
                dmRoom.getRequest().getChefMember().getId())) {
            throw new TransactionException(CHEF_MEMBER_NOT_MATCH);
        }
        if (dmRoom.getSuggestion().getIsSuggested()) {
            throw new TransactionException(ALREADY_SUGGESTED);
        }
    }

    private static void validCancelSuggestion(MemberAuthDto memberAuthDto, DmRoom dmRoom) {
        if (!Objects.equals(memberAuthDto.getId(),
                dmRoom.getRequest().getChefMember().getId())) {
            throw new TransactionException(CHEF_MEMBER_NOT_MATCH);
        }
        if (!dmRoom.getSuggestion().getIsSuggested()) {
            throw new TransactionException(SUGGESTION_NOT_FOUND);
        }
    }

    private static void validApproveSuggestion(MemberAuthDto memberAuthDto, Member requestMember, DmRoom dmRoom, long requestMemberPoint, int suggestedPrice) {
        if (!Objects.equals(memberAuthDto.getId(),
                requestMember.getId())) {
            throw new TransactionException(REQUEST_MEMBER_NOT_MATCH);
        }
        if (!dmRoom.getSuggestion().getIsSuggested()) {
            throw new TransactionException(SUGGESTION_NOT_FOUND);
        }
        if (requestMemberPoint < suggestedPrice) {
            throw new TransactionException(NOT_ENOUGH_POINT);
        }
    }

    private static void validRejectSuggestion(MemberAuthDto memberAuthDto, DmRoom dmRoom) {
        if (!Objects.equals(memberAuthDto.getId(),
                dmRoom.getRequest().getMember().getId())) {
            throw new TransactionException(REQUEST_MEMBER_NOT_MATCH);
        }
        if (!dmRoom.getSuggestion().getIsSuggested()) {
            throw new TransactionException(SUGGESTION_NOT_FOUND);
        }
    }

}
