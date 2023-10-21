package com.zerobase.foodlier.module.history.transaction.service;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.history.transaction.domain.model.MemberBalanceHistory;
import com.zerobase.foodlier.module.history.transaction.dto.MemberBalanceHistoryDto;
import com.zerobase.foodlier.module.history.transaction.repository.MemberBalanceHistoryRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.transaction.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.foodlier.module.history.type.TransactionType.POINT_RECEIVE;
import static com.zerobase.foodlier.module.history.type.TransactionType.POINT_SEND;
import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberBalanceHistoryService {
    private final MemberBalanceHistoryRepository memberBalanceHistoryRepository;
    private final MemberRepository memberRepository;

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-10-04
     * 요청자의 포인트 거래내역을 생성합니다.
     */
    public void createRequestMemberBalanceHistory(TransactionDto transactionDto) {
        Member requestMember = transactionDto.getRequestMember();
        memberBalanceHistoryRepository.save(MemberBalanceHistory.builder()
                .member(requestMember)
                .changePoint(transactionDto.getChangePoint())
                .currentPoint((int) requestMember.getPoint())
                .sender(transactionDto.getChefMember().getNickname())
                .transactionType(POINT_SEND)
                .build());
    }

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-10-04
     * 요리사의 포인트 거래내역을 생성합니다.
     */
    public void createChefMemberBalanceHistory(TransactionDto transactionDto) {
        Member chefMember = transactionDto.getChefMember();
        memberBalanceHistoryRepository.save(MemberBalanceHistory.builder()
                .member(chefMember)
                .changePoint(transactionDto.getChangePoint())
                .currentPoint((int) chefMember.getPoint())
                .sender(transactionDto.getRequestMember().getNickname())
                .transactionType(POINT_RECEIVE)
                .build());
    }

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-10-09
     * 거래 내역을 조회합니다.
     */
    public ListResponse<MemberBalanceHistoryDto> getTransactionHistory(MemberAuthDto memberAuthDto,
                                                                       Pageable pageable) {
        Member member = memberRepository.findById(memberAuthDto.getId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        return ListResponse.from(
                memberBalanceHistoryRepository
                        .findByMemberOrderByCreatedAtDesc(member, pageable),
                MemberBalanceHistoryDto::from);
    }
}
