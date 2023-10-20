package com.zerobase.foodlier.common.redis.service;

import com.zerobase.foodlier.common.redis.domain.model.EmailVerification;
import com.zerobase.foodlier.common.redis.exception.EmailVerificationException;
import com.zerobase.foodlier.common.redis.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static com.zerobase.foodlier.common.redis.exception.EmailVerificationErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private static final int EMAIL_VERIFICATION_EXPIRED_TIME = 3;

    /**
     * 인증 객체를 생성하는 메서드
     * 작성자 : 전현서
     * 작성일 : 2023-09-23(2023-09-24)
     */
    public void createVerification(String email, String verificationCode,
                                   LocalDateTime nowTime) {
        EmailVerification emailVerification = EmailVerification
                .builder()
                .email(email)
                .verificationCode(verificationCode)
                .expiredAt(nowTime.plusMinutes(EMAIL_VERIFICATION_EXPIRED_TIME))
                .isAuthorized(false)
                .build();

        emailVerificationRepository.save(emailVerification);
    }

    /**
     * 이메일 검증을 수행하는 메서드 입니다.
     * Redis를 사용하여, 맵핑되는 데이터를 저장하고,
     * 이와 동일한 경우에, 검증이 완료됩니다.
     * 작성자 : 전현서
     * 작성일 : 2023-09-23
     */
    public void verify(String email, String verificationCode, LocalDateTime nowTime) {
        EmailVerification emailVerification = emailVerificationRepository
                .findById(email)
                .orElseThrow(() ->
                        new EmailVerificationException(VERIFICATION_NOT_FOUND));

        validateVerify(emailVerification, verificationCode, nowTime);

        emailVerification.updateAuthorized();
        emailVerificationRepository.save(emailVerification);
    }

    /**
     * 회원가입을 수행할 떄, 인증된 이메일이 맞는지
     * 서버단 에서 한 번 더 확인하는 메서드입니다.
     * 작성자 : 전현서
     * 작성일 : 2023-09-23
     */
    public void isAuthorized(String email) {
        EmailVerification emailVerification = emailVerificationRepository
                .findById(email)
                .orElseThrow(() ->
                        new EmailVerificationException(VERIFICATION_NOT_FOUND));

        if (!emailVerification.isAuthorized()) {
            throw new EmailVerificationException(VERIFICATION_IS_NOT_AUTHORIZED);
        }
    }


    //========================== Validates ====================================

    private void validateVerify(EmailVerification emailVerification,
                                String verificationCode, LocalDateTime nowTime) {

        if (!verificationCode.equals(emailVerification.getVerificationCode())) {
            throw new EmailVerificationException(VERIFICATION_CODE_MISMATCH);
        }

        //이메일 인증 만료 시간을 초과하면 안됨.
        if (nowTime.isAfter(emailVerification.getExpiredAt())) {
            throw new EmailVerificationException(VERIFICATION_IS_EXPIRED);
        }

    }

}
