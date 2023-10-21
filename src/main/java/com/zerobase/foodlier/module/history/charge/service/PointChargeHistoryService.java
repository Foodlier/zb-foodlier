package com.zerobase.foodlier.module.history.charge.service;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.history.charge.domain.model.PointChargeHistory;
import com.zerobase.foodlier.module.history.charge.dto.PointChargeHistoryDto;
import com.zerobase.foodlier.module.history.charge.repository.PointChargeHistoryRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.payment.domain.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.foodlier.module.history.type.TransactionType.CHARGE_CANCEL;
import static com.zerobase.foodlier.module.history.type.TransactionType.CHARGE_POINT;
import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class PointChargeHistoryService {
    private final MemberRepository memberRepository;
    private final PointChargeHistoryRepository pointChargeHistoryRepository;

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-10-09
     * 포인트 충전 내역을 생성합니다.
     */
    public void createPointChargeHistory(Payment payment) {
        pointChargeHistoryRepository.save(PointChargeHistory.builder()
                .paymentKey(payment.getPaymentKey())
                .member(payment.getMember())
                .chargePoint(payment.getAmount())
                .transactionType(CHARGE_POINT)
                .build());
    }

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-10-09
     * 포인트 결제 취소 내역을 생성합니다.
     */
    public void createPointCancelHistory(Payment payment) {
        pointChargeHistoryRepository.save(PointChargeHistory.builder()
                .paymentKey(payment.getPaymentKey())
                .member(payment.getMember())
                .chargePoint(payment.getAmount())
                .transactionType(CHARGE_CANCEL)
                .build());
    }

    /**
     * 작성자 : 이승현
     * 작성일 : 2023-10-09
     * 포인트 충전 내역을 조회합니다.
     */
    public ListResponse<PointChargeHistoryDto> getPointHistory(MemberAuthDto memberAuthDto,
                                                               Pageable pageable) {
        Member member = memberRepository.findById(memberAuthDto.getId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        return ListResponse.from(
                pointChargeHistoryRepository
                        .findByMemberOrderByCreatedAtDesc(member, pageable),
                PointChargeHistoryDto::from);
    }
}
