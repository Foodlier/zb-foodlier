package com.zerobase.foodlier.global.recipe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.foodlier.common.security.config.SecurityConfig;
import com.zerobase.foodlier.global.notification.facade.NotificationFacade;
import com.zerobase.foodlier.global.recipe.facade.RecipeFacade;
import com.zerobase.foodlier.mockuser.WithCustomMockUser;
import com.zerobase.foodlier.module.heart.service.HeartService;
import com.zerobase.foodlier.module.recipe.dto.recipe.RecipeImageResponse;
import com.zerobase.foodlier.module.recipe.service.recipe.RecipeService;
import com.zerobase.foodlier.module.review.recipe.dto.RecipeReviewForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = RecipeController.class, excludeFilters =
@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
class RecipeControllerTest {

    @MockBean
    private RecipeFacade recipeFacade;
    @MockBean
    private RecipeService recipeService;
    @MockBean
    private HeartService heartService;
    @MockBean
    private NotificationFacade notificationFacade;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithCustomMockUser
    @DisplayName("레시피 이미지 업로드 성공")
    void success_upload_recipe_image() throws Exception {
        //given
        String mainImageName = "foodlier_logo.png";
        File file = new File("src/test/resources/foodlier_logo.png");
        String cookingOrderImageName = "foodlier_logo.png";
        MockMultipartFile mainImage = new MockMultipartFile(mainImageName,
                mainImageName,
                MediaType.IMAGE_PNG_VALUE, new FileInputStream(file));
        MockMultipartFile cookingOrderImage1 = new MockMultipartFile(cookingOrderImageName,
                cookingOrderImageName,
                MediaType.IMAGE_PNG_VALUE, new FileInputStream(file));
        MockMultipartFile cookingOrderImage2 = new MockMultipartFile(cookingOrderImageName,
                cookingOrderImageName,
                MediaType.IMAGE_PNG_VALUE, new FileInputStream(file));
        List<MockMultipartFile> cookingOrderImageList = new ArrayList<>(
                List.of(cookingOrderImage1, cookingOrderImage2)
        );

        given(recipeFacade.uploadRecipeImage(any(), any()))
                .willReturn(RecipeImageResponse.builder()
                        .mainImage("http://s3.test/mainimage")
                        .cookingOrderImageList(new ArrayList<>(
                                List.of("http://s3.test/cookingorderimage1",
                                        "http://s3.test/cookingorderimage2")))
                        .build());

        //when
        ResultActions perform = mockMvc.perform(multipart("/recipe/image")
                .file("mainImage", mainImage.getBytes())
                .file("cookingOrderImageList", cookingOrderImageList.get(0).getBytes())
                .file("cookingOrderImageList", cookingOrderImageList.get(1).getBytes())
                .with(request -> {
                    request.setMethod("POST");
                    return request;
                }).with(csrf())
                .contentType(MediaType.MULTIPART_FORM_DATA));

        //then
        verify(recipeFacade, times(1)).uploadRecipeImage(any(), any());
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mainImage")
                        .value("http://s3.test/mainimage"))
                .andExpect(jsonPath("$.cookingOrderImageList.[0]")
                        .value("http://s3.test/cookingorderimage1"))
                .andExpect(jsonPath("$.cookingOrderImageList.[1]")
                        .value("http://s3.test/cookingorderimage2"))
        ;

    }

    @Test
    @WithCustomMockUser
    @DisplayName("레시피 이미지 수정 성공")
    void success_update_recipe_image() throws Exception {
        //given
        String mainImageName = "foodlier_logo.png";
        File file = new File("src/test/resources/foodlier_logo.png");
        String cookingOrderImageName = "foodlier_logo.png";
        MockMultipartFile mainImage = new MockMultipartFile(mainImageName,
                mainImageName,
                MediaType.IMAGE_PNG_VALUE, new FileInputStream(file));
        MockMultipartFile cookingOrderImage1 = new MockMultipartFile(cookingOrderImageName,
                cookingOrderImageName,
                MediaType.IMAGE_PNG_VALUE, new FileInputStream(file));
        MockMultipartFile cookingOrderImage2 = new MockMultipartFile(cookingOrderImageName,
                cookingOrderImageName,
                MediaType.IMAGE_PNG_VALUE, new FileInputStream(file));
        List<MockMultipartFile> cookingOrderImageList = new ArrayList<>(
                List.of(cookingOrderImage1, cookingOrderImage2)
        );

        given(recipeFacade.updateRecipeImage(any(), any(), any(), any()))
                .willReturn(RecipeImageResponse.builder()
                        .mainImage("http://s3.test/mainimage")
                        .cookingOrderImageList(new ArrayList<>(
                                List.of("http://s3.test/cookingorderimage1",
                                        "http://s3.test/cookingorderimage2")))
                        .build());

        long recipeId = 2L;

        //when
        ResultActions perform = mockMvc.perform(multipart("/recipe/image/{recipeId}", recipeId)
                .file("mainImage", mainImage.getBytes())
                .file("cookingOrderImageList", cookingOrderImageList.get(0).getBytes())
                .file("cookingOrderImageList", cookingOrderImageList.get(1).getBytes())
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                }).with(csrf())
                .contentType(MediaType.MULTIPART_FORM_DATA));

        //then
        verify(recipeFacade, times(1))
                .updateRecipeImage(any(), any(), any(), any());
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mainImage")
                        .value("http://s3.test/mainimage"))
                .andExpect(jsonPath("$.cookingOrderImageList.[0]")
                        .value("http://s3.test/cookingorderimage1"))
                .andExpect(jsonPath("$.cookingOrderImageList.[1]")
                        .value("http://s3.test/cookingorderimage2"))
        ;

    }

//    @Test
//    @WithCustomMockUser
//    @DisplayName("꿀조합 후기 작성 성공")
//    void success_createRecipeReview() throws Exception {
//        //when & then
//        String cookImageName = "foodlier_logo.png";
//        File file = new File("src/test/resources/foodlier_logo.png");
//
//        RecipeReviewForm form = RecipeReviewForm.builder()
//                .content("최고의 극찬")
//                .star(5)
//                .cookImage(new MockMultipartFile(cookImageName,
//                        cookImageName, MediaType.IMAGE_PNG_VALUE, new FileInputStream(file)))
//                .build();
//
//        mockMvc.perform(multipart("/review/recipe/1")
//                        .file("cookImage", form.getCookImage().getBytes())
//                        .param("content", form.getContent())
//                        .param("star", String.valueOf(form.getStar()))
//                        .with(request -> {
//                            request.setMethod("POST");
//                            return request;
//                        }).with(csrf())
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string("꿀조합 후기를 작성하였습니다."));
//
//        ArgumentCaptor<RecipeReviewForm> captor = ArgumentCaptor.forClass(RecipeReviewForm.class);
//        verify(recipeReviewFacade, times(1))
//                .createRecipeReview(eq(1L), eq(1L), captor.capture());
//        RecipeReviewForm expectedForm = captor.getValue();
//
//        assertAll(
//                () -> assertEquals(form.getContent(), expectedForm.getContent()),
//                () -> assertEquals(form.getStar(), expectedForm.getStar()),
//                () -> assertEquals(form.getCookImage().getSize(), expectedForm.getCookImage().getSize())
//        );
//    }

//    @Test
//    @DisplayName("충전 요청 성공")
//    @WithCustomMockUser
//    void success_requestPayments() throws Exception {
//        //given
//        PaymentRequest paymentRequest = PaymentRequest.builder()
//                .payType(PayType.CARD)
//                .orderName(OrderNameType.CHARGE)
//                .amount(1000L)
//                .build();
//
//        given(paymentService.requestPayments(any(), any()))
//                .willReturn(PaymentResponse.builder()
//                        .payType(PayType.CARD.name())
//                        .amount(1000L)
//                        .orderId("orderId")
//                        .orderName(OrderNameType.CHARGE.name())
//                        .customerEmail("test@test.com")
//                        .customerNickName("test")
//                        .successUrl("success")
//                        .failUrl("fail")
//                        .payDate(String.valueOf(LocalDate.now()))
//                        .build());
//
//        //when
//        ResultActions perform = mockMvc.perform(post("/point/charge")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(paymentRequest))
//                .with(csrf()));
//
//        //then
//        perform.andDo(print())
//                .andExpectAll(
//                        status().isOk(),
//                        jsonPath("$.payType")
//                                .value(paymentRequest.getPayType().name()),
//                        jsonPath("$.amount")
//                                .value(paymentRequest.getAmount()),
//                        jsonPath("$.orderId")
//                                .value("orderId"),
//                        jsonPath("$.orderName")
//                                .value(paymentRequest.getOrderName().name()),
//                        jsonPath("$.customerEmail")
//                                .value("test@test.com"),
//                        jsonPath("$.customerNickName")
//                                .value("test"),
//                        jsonPath("$.successUrl")
//                                .value("success"),
//                        jsonPath("$.failUrl")
//                                .value("fail"),
//                        jsonPath("$.payDate")
//                                .value(String.valueOf(LocalDate.now()))
//                );
//    }
//
//    @Test
//    @DisplayName("결제 완료 성공")
//    @WithCustomMockUser
//    void success_requestFinalPayments() throws Exception {
//        //given
//
//        //when
//        ResultActions perform = mockMvc.perform(get("/point/success?" +
//                "paymentKey=paymentKey&orderId=orderId&amount=1000"));
//
//        //then
//        perform.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string("결제 완료, 금액 : 1000"));
//    }
//
//    @Test
//    @DisplayName("결제 실패 성공")
//    @WithCustomMockUser
//    void success_requestFail() throws Exception {
//        //given
//        PaymentResponseHandleFailDto paymentResponseHandleFailDto =
//                PaymentResponseHandleFailDto.builder()
//                        .errorCode("errorCode")
//                        .errorMsg("결제실패")
//                        .orderId("orderId")
//                        .build();
//
//        given(paymentService.requestFail(any(), any(), any()))
//                .willReturn(paymentResponseHandleFailDto);
//
//        //when
//        ResultActions perform = mockMvc.perform(get("/point/fail?" +
//                "code=errorCode&message=결제실패&orderId=orderId"));
//
//        //then
//        perform.andDo(print())
//                .andExpectAll(
//                        status().isOk(),
//                        jsonPath("$.errorCode")
//                                .value(paymentResponseHandleFailDto.getErrorCode()),
//                        jsonPath("$.errorMsg")
//                                .value(paymentResponseHandleFailDto.getErrorMsg()),
//                        jsonPath("$.orderId")
//                                .value(paymentResponseHandleFailDto.getOrderId())
//                );
//    }
//
//    @Test
//    @WithCustomMockUser
//    @DisplayName("결제 취소 성공")
//    void success_requestPaymentCancel() throws Exception {
//        //given
//
//        //when
//        ResultActions perform = mockMvc.perform(post("/point/cancel?" +
//                "paymentKey=paymentKey&cancelReason=단순변심")
//                .with(csrf()));
//
//        //then
//        perform.andDo(print())
//                .andExpectAll(
//                        status().isOk(),
//                        content().string("결제 취소 완료, 이유 : 단순변심")
//                );
//    }
//
//    @Test
//    @WithCustomMockUser
//    @DisplayName("가격 제안 성공")
//    void success_suggestPrice() throws Exception {
//        //given
//        SuggestionForm suggestionForm = new SuggestionForm(1000);
//
//        given(transactionService.sendSuggestion(any(), any(), anyLong()))
//                .willReturn("가격을 제안했습니다.");
//
//        //when
//        ResultActions perform = mockMvc.perform(post("/point/suggest/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(suggestionForm))
//                .with(csrf()));
//
//        //then
//        perform.andDo(print())
//                .andExpectAll(
//                        status().isOk(),
//                        content().string("가격을 제안했습니다.")
//                );
//    }
//
//    @Test
//    @WithCustomMockUser
//    @DisplayName("가격 제안 취소 성공")
//    void success_cancelSuggestion() throws Exception {
//        //given
//        given(transactionService.cancelSuggestion(any(), anyLong()))
//                .willReturn("제안이 취소되었습니다.");
//
//        //when
//        ResultActions perform = mockMvc.perform(post("/point/suggest/cancel/1")
//                .with(csrf()));
//
//        //then
//        perform.andDo(print())
//                .andExpectAll(
//                        status().isOk(),
//                        content().string("제안이 취소되었습니다.")
//                );
//    }
//
//    @Test
//    @WithCustomMockUser
//    @DisplayName("제안 수락 성공")
//    void success_approveSuggestion() throws Exception {
//        //given
//
//        //when
//        ResultActions perform = mockMvc.perform(patch("/point/suggest/approve/1")
//                .with(csrf()));
//
//        //then
//        perform.andDo(print())
//                .andExpectAll(
//                        status().isOk(),
//                        content().string("제안을 수락했습니다.")
//                );
//    }
//
//    @Test
//    @WithCustomMockUser
//    @DisplayName("제안 거절 성공")
//    void success_rejectSuggestion() throws Exception {
//        //given
//        given(transactionService.rejectSuggestion(any(), anyLong()))
//                .willReturn("제안을 거절하였습니다.");
//
//        //when
//        ResultActions perform = mockMvc.perform(patch("/point/suggest/reject/1")
//                .with(csrf()));
//
//        //then
//        perform.andDo(print())
//                .andExpectAll(
//                        status().isOk(),
//                        content().string("제안을 거절하였습니다.")
//                );
//    }
//
//    @Test
//    @WithCustomMockUser
//    @DisplayName("포인 충전 내역 조회 성공")
//    void success_getPointHistory() throws Exception {
//        //given
//        PointChargeHistoryDto pointCharge = PointChargeHistoryDto.builder()
//                .paymentKey("paymentKey1")
//                .chargePoint(1000L)
//                .description(TransactionType.CHARGE_POINT.getDescription())
//                .chargeAt(LocalDateTime.of(2023, 12, 25, 1, 23, 45))
//                .build();
//        PointChargeHistoryDto pointChargeCancel = PointChargeHistoryDto.builder()
//                .paymentKey("paymentKey2")
//                .chargePoint(1000L)
//                .description(TransactionType.CHARGE_CANCEL.getDescription())
//                .chargeAt(LocalDateTime.of(2023, 12, 25, 1, 23, 45))
//                .build();
//
//
//        given(pointChargeHistoryService.getPointHistory(any(), any()))
//                .willReturn(ListResponse.from(new PageImpl<>(
//                        List.of(pointCharge, pointChargeCancel)
//                )));
//
//
//        //when
//        ResultActions perform = mockMvc.perform(get("/point/charge/0/10"));
//
//        //then
//        perform.andDo(print())
//                .andExpectAll(
//                        status().isOk(),
//                        jsonPath("$.content.[0].chargePoint")
//                                .value(pointCharge.getChargePoint()),
//                        jsonPath("$.content.[0].paymentKey")
//                                .value(pointCharge.getPaymentKey()),
//                        jsonPath("$.content.[0].chargeAt")
//                                .value("2023-12-25 01:23:45"),
//                        jsonPath("$.content.[0].description")
//                                .value(pointCharge.getDescription()),
//
//                        jsonPath("$.content.[1].chargePoint")
//                                .value(pointChargeCancel.getChargePoint()),
//                        jsonPath("$.content.[1].paymentKey")
//                                .value(pointChargeCancel.getPaymentKey()),
//                        jsonPath("$.content.[1].chargeAt")
//                                .value("2023-12-25 01:23:45"),
//                        jsonPath("$.content.[1].description")
//                                .value(pointChargeCancel.getDescription())
//                );
//    }
//
//    @Test
//    @WithCustomMockUser
//    @DisplayName("거래 내역 조회 성공")
//    void success_getTransactionHistory() throws Exception {
//        //given
//        MemberBalanceHistoryDto memberBalanceHistoryDto = MemberBalanceHistoryDto.builder()
//                .currentPoint(1000)
//                .changePoint(1000)
//                .sender("testSender")
//                .description(TransactionType.POINT_RECEIVE.getDescription())
//                .transactionAt(LocalDateTime.of(2023, 12, 25, 1, 23, 45))
//                .build();
//
//        given(memberBalanceHistoryService.getTransactionHistory(any(), any()))
//                .willReturn(ListResponse.from(new PageImpl<>(
//                        List.of(memberBalanceHistoryDto)
//                )));
//
//        //when
//        ResultActions perform = mockMvc.perform(get("/point/transaction/0/10"));
//
//        //then
//        perform.andDo(print())
//                .andExpectAll(
//                        status().isOk(),
//                        jsonPath("$.content.[0].changePoint")
//                                .value(memberBalanceHistoryDto.getChangePoint()),
//                        jsonPath("$.content.[0].currentPoint")
//                                .value(memberBalanceHistoryDto.getCurrentPoint()),
//                        jsonPath("$.content.[0].sender")
//                                .value(memberBalanceHistoryDto.getSender()),
//                        jsonPath("$.content.[0].transactionAt")
//                                .value("2023-12-25 01:23:45"),
//                        jsonPath("$.content.[0].description")
//                                .value(memberBalanceHistoryDto.getDescription())
//                );
//    }
}