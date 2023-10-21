package com.zerobase.foodlier.common.redis.service;

import com.zerobase.foodlier.common.redis.domain.model.EmailVerification;
import com.zerobase.foodlier.common.redis.exception.EmailVerificationException;
import com.zerobase.foodlier.common.redis.repository.EmailVerificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.zerobase.foodlier.common.redis.exception.EmailVerificationErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailVerificationServiceTest {

    @Mock
    private EmailVerificationRepository emailVerificationRepository;

    @InjectMocks
    private EmailVerificationService emailVerificationService;

    //====================== SUCCESS CASES ====================================
    @Test
    @DisplayName("검증 객체 생성 성공")
    void success_createVerification(){
        //given
        given(emailVerificationRepository.save(any()))
                .willReturn(
                        EmailVerification.builder().build()
                );

        //when
        emailVerificationService.createVerification(
                "test@test.com", "LzOXCMpEUo",
                LocalDateTime.of(2023, 10, 31,
                        9, 0, 0)
        );

        //then
        ArgumentCaptor<EmailVerification> captor = ArgumentCaptor.forClass(
                EmailVerification.class
        );

        verify(emailVerificationRepository, times(1))
                .save(captor.capture());

        assertAll(
                () -> assertEquals("test@test.com",
                        captor.getValue().getEmail()),
                () -> assertEquals("LzOXCMpEUo",
                        captor.getValue().getVerificationCode()),
                () -> assertEquals(
                        LocalDateTime.of(2023, 10, 31,
                                9, 3, 0),
                        captor.getValue().getExpiredAt()),
                () -> assertFalse(captor.getValue().isAuthorized())
        );
    }

    @Test
    @DisplayName("이메일 검증 성공")
    void success_verify(){
        //given
        given(emailVerificationRepository.findById(anyString()))
                .willReturn(
                        Optional.of(EmailVerification.builder()
                                .email("test@test.com")
                                .verificationCode("LzOXCMpEUo")
                                .isAuthorized(false)
                                .expiredAt(
                                    LocalDateTime.of(2023, 10, 31,
                                            9, 3, 0)
                                )
                                .build())
                );

        //when
        emailVerificationService.verify(
                "test@test.com", "LzOXCMpEUo",
                LocalDateTime.of(2023, 10, 31,
                        9, 1, 0)
        );

        //then
        ArgumentCaptor<EmailVerification> captor = ArgumentCaptor.forClass(
                EmailVerification.class
        );

        verify(emailVerificationRepository, times(1))
                .save(captor.capture());

        assertAll(
                () -> assertEquals("test@test.com",
                        captor.getValue().getEmail()),
                () -> assertEquals("LzOXCMpEUo",
                        captor.getValue().getVerificationCode()),
                () -> assertEquals(
                        LocalDateTime.of(2023, 10, 31,
                                9, 3, 0),
                        captor.getValue().getExpiredAt()),
                () -> assertTrue(captor.getValue().isAuthorized())
        );
    }


    @Test
    @DisplayName("이메일 검증 여부 성공")
    void success_isAuthorized(){
        //given
        given(emailVerificationRepository.findById(anyString()))
                .willReturn(
                        Optional.of(EmailVerification.builder()
                                        .email("test@test.com")
                                        .isAuthorized(true)
                                .build())
                );

        //when
        emailVerificationService.isAuthorized(
            "test@test.com"
        );

        //then
        verify(emailVerificationRepository, times(1))
                .findById(anyString());
    }


    //====================== FAIL CASES ====================================

    @Test
    @DisplayName("이메일 검증 실패 - 인증 객체 X")
    void fail_verify_verification_not_found(){
        //given
        given(emailVerificationRepository.findById(anyString()))
                .willReturn(
                        Optional.empty()
                );

        //when
        EmailVerificationException exception = assertThrows(
                EmailVerificationException.class,
                () -> emailVerificationService.verify(
                "test@test.com", "LzOXCMpEUo",
                        LocalDateTime.now()
                )
        );

        //then
        assertEquals(VERIFICATION_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("이메일 검증 실패 - 인증 코드 일치 X")
    void fail_verify_verification_code_mismatch(){
        //given
        given(emailVerificationRepository.findById(anyString()))
                .willReturn(
                        Optional.of(EmailVerification.builder()
                                .email("test@test.com")
                                .verificationCode("LzOXCMpEUz")
                                .isAuthorized(false)
                                .expiredAt(
                                        LocalDateTime.of(2023, 10, 31,
                                                9, 3, 0)
                                )
                                .build())
                );

        //when
        EmailVerificationException exception = assertThrows(
                EmailVerificationException.class,
                () -> emailVerificationService.verify(
                        "test@test.com", "LzOXCMpEUo",
                        LocalDateTime.of(2023, 10, 31,
                                9, 1, 0)
                )
        );

        //then
        assertEquals(VERIFICATION_CODE_MISMATCH, exception.getErrorCode());
    }

    @Test
    @DisplayName("이메일 검증 실패 - 인증 시간 만료")
    void fail_verify_verification_is_expired(){
        //given
        given(emailVerificationRepository.findById(anyString()))
                .willReturn(
                        Optional.of(EmailVerification.builder()
                                .email("test@test.com")
                                .verificationCode("LzOXCMpEUz")
                                .isAuthorized(false)
                                .expiredAt(
                                        LocalDateTime.of(2023, 10, 30,
                                                9, 3, 0)
                                )
                                .build())
                );

        //when
        EmailVerificationException exception = assertThrows(
                EmailVerificationException.class,
                () -> emailVerificationService.verify(
                        "test@test.com", "LzOXCMpEUz",
                        LocalDateTime.of(2023, 10, 31,
                                9, 3, 1)
                )
        );

        //then
        assertEquals(VERIFICATION_IS_EXPIRED, exception.getErrorCode());
    }

    @Test
    @DisplayName("이메일 검증 여부 실패 -> 인증 객체 X")
    void fail_isAuthorized_verification_not_found(){
        //given
        given(emailVerificationRepository.findById(anyString()))
                .willReturn(
                        Optional.empty()
                );

        //when
        EmailVerificationException exception = assertThrows(
                EmailVerificationException.class,
                () -> emailVerificationService.isAuthorized("test@test.com")
        );

        //then
        assertEquals(VERIFICATION_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("이메일 검증 여부 실패 -> 인증 상태 false")
    void fail_isAuthorized_verification_is_not_authorized(){
        //given
        given(emailVerificationRepository.findById(anyString()))
                .willReturn(
                        Optional.of(EmailVerification.builder()
                                .email("test@test.com")
                                .isAuthorized(false)
                                .build())
                );

        //when
        EmailVerificationException exception = assertThrows(
                EmailVerificationException.class,
                () -> emailVerificationService.isAuthorized("test@test.com")
        );

        //then
        assertEquals(VERIFICATION_IS_NOT_AUTHORIZED, exception.getErrorCode());
    }
}