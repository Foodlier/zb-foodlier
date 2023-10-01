package com.zerobase.foodlier.module.payment.service;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.history.charge.domain.model.PointChargeHistory;
import com.zerobase.foodlier.module.history.charge.repository.PointChargeHistoryRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.payment.domain.model.Payment;
import com.zerobase.foodlier.module.payment.dto.PaymentRequest;
import com.zerobase.foodlier.module.payment.dto.PaymentResponse;
import com.zerobase.foodlier.module.payment.dto.PaymentResponseHandleDto;
import com.zerobase.foodlier.module.payment.dto.PaymentResponseHandleFailDto;
import com.zerobase.foodlier.module.payment.exception.PaymentException;
import com.zerobase.foodlier.module.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.payment.exception.PaymentErrorCode.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final PointChargeHistoryRepository pointChargeHistoryRepository;
    private final RestTemplate restTemplate;

    @Value("${payments.toss.test_client_api_key}")
    private String testClientApiKey;
    @Value("${payments.toss.test_client_secret_api_key}")
    private String testSecretApiKey;
    @Value("${payments.toss.success_url}")
    private String successCallBackUrl;
    @Value("${payments.toss.fail_url}")
    private String failCallBackUrl;
    @Value("${payments.toss.origin_url}")
    private String tossOriginUrl;

    @Transactional
    @Override
    public PaymentResponse requestPayments(PaymentRequest paymentRequest, MemberAuthDto memberAuthDto) {
        PaymentResponse paymentResponse;
        Payment payment = PaymentRequest.from(paymentRequest);
        Member member = memberRepository.findByEmail(memberAuthDto.getEmail())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        payment.setMember(member);
        payment.setCustomerEmail(memberAuthDto.getEmail());
        payment.setCustomerNickName(member.getNickname());
        paymentRepository.save(payment);

        paymentResponse = Payment.from(payment);
        paymentResponse.setSuccessUrl(successCallBackUrl);
        paymentResponse.setFailUrl(failCallBackUrl);

        return paymentResponse;
    }

    @Transactional
    @Override
    public PaymentResponseHandleDto requestFinalPayment(String paymentKey, String orderId, Long amount) {
        verifyRequest(paymentKey, orderId, amount);

        HttpHeaders httpHeaders = new HttpHeaders();

        testSecretApiKey = testSecretApiKey + ":";
        String encodedAuth = new String(Base64.getEncoder().encode(testSecretApiKey.getBytes(StandardCharsets.UTF_8)));

        httpHeaders.setBasicAuth(encodedAuth);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        JSONObject param = new JSONObject();
        param.put("orderId", orderId);
        param.put("amount", amount);

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentException(PAYMENT_REQUEST_NOT_FOUND));
        payment.setPaySuccessYn("Y");
        payment.getMember().setPoint(payment.getMember().getPoint() + amount);
        paymentRepository.save(payment);

        pointChargeHistoryRepository.save(PointChargeHistory.builder()
                .paymentKey(payment.getPaymentKey())
                .member(payment.getMember())
                .chargePoint(amount)
                .build());

        try {
            return restTemplate.postForEntity(tossOriginUrl + paymentKey,
                            new HttpEntity<>(param, httpHeaders),
                            PaymentResponseHandleDto.class)
                    .getBody();
        } catch (HttpClientErrorException e) {
            throw new PaymentException(ALREADY_PROCESSED_PAYMENT);
        }
    }

    @Transactional
    @Override
    public PaymentResponseHandleFailDto requestFail(String errorCode, String errorMsg, String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentException(PAYMENT_REQUEST_NOT_FOUND));
        payment.setPaySuccessYn("N");
        payment.setPayFailReason(errorMsg);
        paymentRepository.save(payment);

        return PaymentResponseHandleFailDto.builder()
                .orderId(orderId)
                .errorCode(errorCode)
                .errorMsg(errorMsg)
                .build();
    }

    @Transactional
    @Override
    public String requestPaymentCancel(String paymentKey, String cancelReason) {
        URI uri = URI.create(tossOriginUrl + paymentKey + "/cancel");

        HttpHeaders httpHeaders = new HttpHeaders();
        byte[] secretKeyByte = (testSecretApiKey + ":").getBytes(StandardCharsets.UTF_8);
        httpHeaders.setBasicAuth(new String(Base64.getEncoder().encode(secretKeyByte)));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        JSONObject param = new JSONObject();
        param.put("cancelReason", cancelReason);

        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
                .filter(p -> p.getPaySuccessYn().equals("Y"))
                .orElseThrow(() -> new PaymentException(PAYMENT_REQUEST_NOT_FOUND));
        Long amount = payment.getAmount();
        payment.getMember().setPoint(payment.getMember().getPoint() - amount);
        payment.setCanceled(true);
        paymentRepository.save(payment);

        return restTemplate.postForObject(uri,
                new HttpEntity<>(param, httpHeaders), String.class);
    }

    private void verifyRequest(String paymentKey, String orderId, Long amount) {
        paymentRepository.findByOrderId(orderId)
                .ifPresentOrElse(
                        P -> {
                            if (P.getAmount().equals(amount)) {
                                P.setPaymentKey(paymentKey);
                            } else {
                                throw new PaymentException(PAYMENT_ERROR_ORDER_AMOUNT);
                            }
                        },
                        () -> {
                            throw new PaymentException(UNDEFINED_ERROR);
                        }
                );
    }
}
