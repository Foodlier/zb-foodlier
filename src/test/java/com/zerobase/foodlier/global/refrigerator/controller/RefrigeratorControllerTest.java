package com.zerobase.foodlier.global.refrigerator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.config.SecurityConfig;
import com.zerobase.foodlier.global.notification.facade.NotificationFacade;
import com.zerobase.foodlier.global.refrigerator.facade.RefrigeratorFacade;
import com.zerobase.foodlier.mockuser.WithCustomMockUser;
import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.chef.dto.AroundChefDto;
import com.zerobase.foodlier.module.member.chef.dto.RequestedChefDto;
import com.zerobase.foodlier.module.member.chef.service.ChefMemberService;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.dto.RequestedMemberDto;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import com.zerobase.foodlier.module.notification.domain.type.ActionType;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;
import com.zerobase.foodlier.module.notification.domain.type.PerformerType;
import com.zerobase.foodlier.module.notification.dto.notify.Impl.ChefNotify;
import com.zerobase.foodlier.module.notification.dto.notify.Impl.RequesterNotify;
import com.zerobase.foodlier.module.request.domain.model.Request;
import com.zerobase.foodlier.module.request.domain.vo.Ingredient;
import com.zerobase.foodlier.module.request.dto.RequestDetailDto;
import com.zerobase.foodlier.module.request.service.RequestService;
import com.zerobase.foodlier.module.requestform.dto.RequestFormDetailDto;
import com.zerobase.foodlier.module.requestform.dto.RequestFormDto;
import com.zerobase.foodlier.module.requestform.dto.RequestFormResponseDto;
import com.zerobase.foodlier.module.requestform.service.RequestFormService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = RefrigeratorController.class, excludeFilters =
@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class}))
class RefrigeratorControllerTest {

    @MockBean
    private RequestService requestService;
    @MockBean
    private RefrigeratorFacade refrigeratorFacade;
    @MockBean
    private ChefMemberService chefMemberService;
    @MockBean
    private RequestFormService requestFormService;
    @MockBean
    private MemberService memberService;
    @MockBean
    private NotificationFacade notificationFacade;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //======================== 테스트 객체 정의 ============================
    private Member getRequester(){
        return Member.builder()
                .id(1L)
                .nickname("requester")
                .email("requester@test.com")
                .build();
    }

    private Member getChef(){
        return Member.builder()
                .id(2L)
                .nickname("chef")
                .email("chef@test.com")
                .build();
    }

    private ChefMember getChefMember(){
        return ChefMember.builder()
                .id(2L)
                .member(getChef())
                .build();
    }

    private Request getRequest(){
        return Request.builder()
                .id(1L)
                .member(getRequester())
                .chefMember(getChefMember())
                .title("요청 제목")
                .content("요청 내용")
                .expectedPrice(1000L)
                .expectedAt(LocalDateTime.of(2023, 10, 16, 9, 30, 50))
                .isPaid(false)
                .ingredientList(List.of(Ingredient.builder()
                                .ingredientName("삼겹살")
                        .build()))
                .build();
    }

    private RequestFormDto getRequestFormDto(){
        return RequestFormDto.builder()
                .recipeId(1L)
                .title("요청서 제목")
                .content("이러쿵 저러쿵")
                .expectedPrice(1000L)
                .expectedAt(LocalDateTime.of(2023, 10, 18,
                        9, 50, 30))
                .ingredientList(List.of("김치"))
                .build();
    }

    //======================== 테스트 코드 =================================

    @Test
    @WithCustomMockUser
    @DisplayName("요청 보내기 성공")
    void success_sendRequest() throws Exception {
        //given
        Member requester = getRequester();
        Member chef = getChef();
        Request request = getRequest();
        given(requestService.sendRequest(anyLong(), anyLong(), anyLong()))
                .willReturn(request);

        //when
        ResultActions perform = mockMvc.perform(patch("/refrigerator/send?requestFormId=1&chefMemberId=1")
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("요청서가 전송되었습니다."));

        verify(requestService, times(1)).sendRequest(eq(1L), eq(1L), eq(1L));

        ArgumentCaptor<ChefNotify> captor = ArgumentCaptor.forClass(ChefNotify.class);
        verify(notificationFacade, times(1)).send(captor.capture());
        ChefNotify notify = captor.getValue();

        assertAll(
                () -> assertEquals(chef.getId(), notify.getReceiver().getId()),
                () -> assertEquals(requester.getNickname(), notify.getPerformerNickname()),
                () -> assertEquals(request.getId(), notify.getTargetSubjectId()),
                () -> assertEquals(request.getTitle(), notify.getTargetTitle()),
                () -> assertEquals(PerformerType.REQUESTER, notify.getNotifyInfoDto().getPerformerType()),
                () -> assertEquals(ActionType.SEND_RECIPE_REQUEST, notify.getNotifyInfoDto().getActionType())
        );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("요청 취소 성공")
    void success_cancelRequest() throws Exception {

        //given
        Member requester = getRequester();
        Member chef = getChef();
        Request request = getRequest();
        given(requestService.cancelRequest(anyLong(), anyLong()))
                .willReturn(request);

        //when
        ResultActions perform = mockMvc.perform(patch("/refrigerator/cancel/1")
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("요청이 취소되었습니다."));

        verify(requestService, times(1)).cancelRequest(eq(1L), eq(1L));

        ArgumentCaptor<ChefNotify> captor = ArgumentCaptor.forClass(ChefNotify.class);
        verify(notificationFacade, times(1)).send(captor.capture());
        ChefNotify notify = captor.getValue();

        assertAll(
                () -> assertEquals(chef.getId(), notify.getReceiver().getId()),
                () -> assertEquals(requester.getNickname(), notify.getPerformerNickname()),
                () -> assertEquals(request.getId(), notify.getTargetSubjectId()),
                () -> assertEquals(request.getTitle(), notify.getTargetTitle()),
                () -> assertEquals(PerformerType.REQUESTER, notify.getNotifyInfoDto().getPerformerType()),
                () -> assertEquals(ActionType.REQUEST_CANCEL, notify.getNotifyInfoDto().getActionType())
        );

    }

    @Test
    @WithCustomMockUser
    @DisplayName("요청자가 요청 수락 성공")
    void success_requesterApproveRequest() throws Exception {

        //given
        Member requester = getRequester();
        Member chef = getChef();
        Request request = getRequest();
        given(refrigeratorFacade.requesterApproveAndCreateDm(anyLong(), anyLong()))
                .willReturn(request);

        //when
        ResultActions perform = mockMvc.perform(post("/refrigerator/requester/approve/1")
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("요청을 수락하였습니다."));

        verify(refrigeratorFacade, times(1))
                .requesterApproveAndCreateDm(eq(1L), eq(1L));

        ArgumentCaptor<ChefNotify> captor = ArgumentCaptor.forClass(ChefNotify.class);
        verify(notificationFacade, times(1)).send(captor.capture());
        ChefNotify notify = captor.getValue();

        assertAll(
                () -> assertEquals(chef.getId(), notify.getReceiver().getId()),
                () -> assertEquals(requester.getNickname(), notify.getPerformerNickname()),
                () -> assertEquals(request.getId(), notify.getTargetSubjectId()),
                () -> assertEquals(request.getTitle(), notify.getTargetTitle()),
                () -> assertEquals(PerformerType.REQUESTER, notify.getNotifyInfoDto().getPerformerType()),
                () -> assertEquals(ActionType.QUOTATION_APPROVE, notify.getNotifyInfoDto().getActionType())
        );

    }

    @Test
    @WithCustomMockUser(role = "ROLE_CHEF")
    @DisplayName("요리사가 요청 수락 성공")
    void success_chefApproveRequest() throws Exception {

        //given
        Member requester = getRequester();
        Member chef = getChef();
        Request request = getRequest();
        given(refrigeratorFacade.chefApproveAndCreateDm(anyLong(), anyLong()))
                .willReturn(request);

        //when
        ResultActions perform = mockMvc.perform(post("/refrigerator/chef/approve/1")
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("요청을 수락하였습니다."));

        verify(refrigeratorFacade, times(1))
                .chefApproveAndCreateDm(eq(1L), eq(1L));

        ArgumentCaptor<RequesterNotify> captor = ArgumentCaptor.forClass(RequesterNotify.class);
        verify(notificationFacade, times(1)).send(captor.capture());
        RequesterNotify notify = captor.getValue();

        assertAll(
                () -> assertEquals(requester.getId(), notify.getReceiver().getId()),
                () -> assertEquals(chef.getNickname(), notify.getPerformerNickname()),
                () -> assertEquals(request.getId(), notify.getTargetSubjectId()),
                () -> assertEquals(request.getTitle(), notify.getTargetTitle()),
                () -> assertEquals(PerformerType.CHEF, notify.getNotifyInfoDto().getPerformerType()),
                () -> assertEquals(ActionType.REQUEST_APPROVE, notify.getNotifyInfoDto().getActionType()),
                () -> assertEquals(NotificationType.REQUEST, notify.getNotifyInfoDto().getNotificationType())
        );

    }

    @Test
    @WithCustomMockUser(role = "ROLE_CHEF")
    @DisplayName("요리사가 요청 거절 성공")
    void success_rejectRequest() throws Exception {

        //given
        Member requester = getRequester();
        Member chef = getChef();
        Request request = getRequest();
        given(requestService.rejectRequest(anyLong(), anyLong()))
                .willReturn(request);

        //when
        ResultActions perform = mockMvc.perform(patch("/refrigerator/reject/1")
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("요청을 거절하였습니다."));

        verify(requestService, times(1))
                .rejectRequest(eq(1L), eq(1L));

        ArgumentCaptor<RequesterNotify> captor = ArgumentCaptor.forClass(RequesterNotify.class);
        verify(notificationFacade, times(1)).send(captor.capture());
        RequesterNotify notify = captor.getValue();

        assertAll(
                () -> assertEquals(requester.getId(), notify.getReceiver().getId()),
                () -> assertEquals(chef.getNickname(), notify.getPerformerNickname()),
                () -> assertEquals(request.getId(), notify.getTargetSubjectId()),
                () -> assertEquals(request.getTitle(), notify.getTargetTitle()),
                () -> assertEquals(PerformerType.CHEF, notify.getNotifyInfoDto().getPerformerType()),
                () -> assertEquals(ActionType.REQUEST_REJECT, notify.getNotifyInfoDto().getActionType()),
                () -> assertEquals(NotificationType.REQUEST, notify.getNotifyInfoDto().getNotificationType())
        );

    }

    @Test
    @WithCustomMockUser
    @DisplayName("요청서 작성 성공")
    void success_createRequestForm() throws Exception {

        //when
        RequestFormDto form = getRequestFormDto();

        ResultActions perform = mockMvc.perform(post("/refrigerator")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("요청서 작성이 완료되었습니다."));

        ArgumentCaptor<RequestFormDto> captor = ArgumentCaptor.forClass(RequestFormDto.class);
        verify(requestFormService, times(1))
                .createRequestForm(eq(1L), captor.capture());

        RequestFormDto expectedForm = captor.getValue();

        assertAll(
                () -> assertEquals(form.getRecipeId(), expectedForm.getRecipeId()),
                () -> assertEquals(form.getTitle(), expectedForm.getTitle()),
                () -> assertEquals(form.getContent(), expectedForm.getContent()),
                () -> assertEquals(form.getExpectedPrice(), expectedForm.getExpectedPrice()),
                () -> assertEquals(form.getExpectedAt(), expectedForm.getExpectedAt()),
                () -> assertEquals(form.getIngredientList().get(0),
                        expectedForm.getIngredientList().get(0))
        );

    }

    @Test
    @WithCustomMockUser
    @DisplayName("요청서 목록 조회 성공")
    void success_getMyRequestForm() throws Exception {
        //given
        RequestFormResponseDto responseDto = RequestFormResponseDto.builder()
                .requestFormId(1L)
                .title("요청서 1")
                .content("내용 1")
                .build();

        given(requestFormService.getMyRequestForm(anyLong(), anyInt(), anyInt()))
                .willReturn(ListResponse.from(
                        new PageImpl<>(
                                List.of(
                                        responseDto
                                )
                        )
                ));

        //when
        ResultActions perform = mockMvc.perform(get("/refrigerator/0/1"));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content.[0].requestFormId").value(1L),
                        jsonPath("$.content.[0].title").value(responseDto.getTitle()),
                        jsonPath("$.content.[0].content").value(responseDto.getContent())
                );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("요청서 상세 조회 성공")
    void success_getRequestFormDetail() throws Exception {

        //given
        RequestFormDetailDto responseDto = RequestFormDetailDto.builder()
                .requestFormId(1L)
                .requesterNickname("요청자")
                .title("요청서 1")
                .content("내용 1")
                .ingredientList(List.of("고등어"))
                .expectedPrice(1000L)
                .expectedAt(LocalDateTime.of(2023, 10,
                        18, 9, 30, 10))
                .address("경기도 성남시 분당구 판교역로 8888")
                .addressDetail("판교점")
                .mainImageUrl("https://s3.test.com/main.png")
                .recipeTitle("고등어 구이")
                .recipeContent("고등어 구이는 이 꿀조합을 따라잡을 수 없다!")
                .heartCount(77777)
                .build();

        given(requestFormService.getRequestFormDetail(anyLong(), anyLong()))
                .willReturn(responseDto);

        //when
        ResultActions perform = mockMvc.perform(get("/refrigerator/1"));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.requestFormId").value(responseDto.getRequestFormId()),
                        jsonPath("$.requesterNickname").value(responseDto.getRequesterNickname()),
                        jsonPath("$.title").value(responseDto.getTitle()),
                        jsonPath("$.content").value(responseDto.getContent()),
                        jsonPath("$.ingredientList.[0]").value(responseDto.getIngredientList().get(0)),
                        jsonPath("$.expectedPrice").value(responseDto.getExpectedPrice()),
                        jsonPath("$.expectedAt").value("2023-10-18 09:30:10"),
                        jsonPath("$.address").value(responseDto.getAddress()),
                        jsonPath("$.addressDetail").value(responseDto.getAddressDetail()),
                        jsonPath("$.mainImageUrl").value(responseDto.getMainImageUrl()),
                        jsonPath("$.recipeTitle").value(responseDto.getRecipeTitle()),
                        jsonPath("$.recipeContent").value(responseDto.getRecipeContent()),
                        jsonPath("$.heartCount").value(responseDto.getHeartCount())
                );

    }

    @Test
    @WithCustomMockUser
    @DisplayName("요청 상세 조회 성공")
    void success_getRequestDetail() throws Exception {

        //given
        RequestDetailDto responseDto = RequestDetailDto.builder()
                .requestId(1L)
                .requesterNickname("요청자")
                .title("요청서 1")
                .content("내용 1")
                .ingredientList(List.of("고등어"))
                .expectedPrice(1000L)
                .expectedAt(LocalDateTime.of(2023, 10,
                        18, 9, 30, 10))
                .address("경기도 성남시 분당구 판교역로 8888")
                .addressDetail("판교점")
                .mainImageUrl("https://s3.test.com/main.png")
                .recipeTitle("고등어 구이")
                .recipeContent("고등어 구이는 이 꿀조합을 따라잡을 수 없다!")
                .heartCount(77777)
                .build();

        given(requestService.getRequestDetail(anyLong(), anyLong()))
                .willReturn(responseDto);

        //when
        ResultActions perform = mockMvc.perform(get("/refrigerator/request/1"));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.requestId").value(responseDto.getRequestId()),
                        jsonPath("$.requesterNickname").value(responseDto.getRequesterNickname()),
                        jsonPath("$.title").value(responseDto.getTitle()),
                        jsonPath("$.content").value(responseDto.getContent()),
                        jsonPath("$.ingredientList.[0]").value(responseDto.getIngredientList().get(0)),
                        jsonPath("$.expectedPrice").value(responseDto.getExpectedPrice()),
                        jsonPath("$.expectedAt").value("2023-10-18 09:30:10"),
                        jsonPath("$.address").value(responseDto.getAddress()),
                        jsonPath("$.addressDetail").value(responseDto.getAddressDetail()),
                        jsonPath("$.mainImageUrl").value(responseDto.getMainImageUrl()),
                        jsonPath("$.recipeTitle").value(responseDto.getRecipeTitle()),
                        jsonPath("$.recipeContent").value(responseDto.getRecipeContent()),
                        jsonPath("$.heartCount").value(responseDto.getHeartCount())
                );


    }

    @Test
    @WithCustomMockUser
    @DisplayName("요청서 수정 성공")
    void success_updateRequestForm() throws Exception {

        //when
        RequestFormDto form = getRequestFormDto();
        ResultActions perform = mockMvc.perform(put("/refrigerator/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("요청서 변경을 완료했습니다."));


        ArgumentCaptor<RequestFormDto> captor = ArgumentCaptor.forClass(RequestFormDto.class);
        verify(requestFormService, times(1))
                .updateRequestForm(eq(1L), captor.capture(), eq(1L));

        RequestFormDto expectedForm = captor.getValue();

        assertAll(
                () -> assertEquals(form.getRecipeId(), expectedForm.getRecipeId()),
                () -> assertEquals(form.getTitle(), expectedForm.getTitle()),
                () -> assertEquals(form.getContent(), expectedForm.getContent()),
                () -> assertEquals(form.getExpectedPrice(), expectedForm.getExpectedPrice()),
                () -> assertEquals(form.getExpectedAt(), expectedForm.getExpectedAt()),
                () -> assertEquals(form.getIngredientList().get(0),
                        expectedForm.getIngredientList().get(0))
        );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("요청서 삭제 성공")
    void success_deleteRequestForm() throws Exception {

        //when
        ResultActions perform = mockMvc.perform(delete("/refrigerator/1")
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("요청서 삭제를 완료하였습니다."));


        verify(requestFormService, times(1))
                .deleteRequestForm(eq(1L), eq(1L));

    }

    @Test
    @WithCustomMockUser
    @DisplayName("요청한 요리사 조회 성공")
    void success_getRequestedChefList() throws Exception {

        //given
        RequestedChefDto dto = RequestedChefDto.builder()
                .chefId(1L)
                .introduce("요리사 소개")
                .starAvg(5D)
                .reviewCount(1)
                .profileUrl("https://s3.test.com/profile.png")
                .nickname("최 셰프")
                .lat(37.1)
                .lnt(127.1)
                .distance(0.5)
                .recipeCount(100L)
                .requestId(1L)
                .isQuotation(false)
                .quotationId(null)
                .build();

        given(chefMemberService.getRequestedChefList(anyLong(), any()))
                .willReturn(ListResponse.from(
                        new PageImpl<>(
                                List.of(
                                        dto
                                )
                        )
                ));

        //when
        ResultActions perform = mockMvc.perform(get("/refrigerator/chef/requested/0/10"));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content.[0].chefId").value(dto.getChefId()),
                        jsonPath("$.content.[0].introduce").value(dto.getIntroduce()),
                        jsonPath("$.content.[0].reviewCount").value(dto.getReviewCount()),
                        jsonPath("$.content.[0].lat").value(dto.getLat()),
                        jsonPath("$.content.[0].lnt").value(dto.getLnt()),
                        jsonPath("$.content.[0].distance").value(dto.getDistance()),
                        jsonPath("$.content.[0].recipeCount").value(dto.getRecipeCount()),
                        jsonPath("$.content.[0].isQuotation").value(dto.getIsQuotation()),
                        jsonPath("$.content.[0].quotationId").value(dto.getQuotationId())
                );

    }

    @Test
    @WithCustomMockUser
    @DisplayName("주변 요리사 조회 성공")
    void success_getAroundChefList() throws Exception {

        //given
        AroundChefDto dto = AroundChefDto.builder()
                .chefId(1L)
                .introduce("요리사 소개")
                .starAvg(5D)
                .reviewCount(1)
                .profileUrl("https://s3.test.com/profile.png")
                .nickname("최 셰프")
                .lat(37.1)
                .lnt(127.1)
                .distance(0.5)
                .recipeCount(100L)
                .build();

        given(chefMemberService.getAroundChefList(anyLong(), any(), any()))
                .willReturn(ListResponse.from(
                        new PageImpl<>(
                                List.of(
                                        dto
                                )
                        )
                ));

        //when
        ResultActions perform = mockMvc.perform(get("/refrigerator/chef/0/10?type=DISTANCE"));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content.[0].chefId").value(dto.getChefId()),
                        jsonPath("$.content.[0].introduce").value(dto.getIntroduce()),
                        jsonPath("$.content.[0].reviewCount").value(dto.getReviewCount()),
                        jsonPath("$.content.[0].lat").value(dto.getLat()),
                        jsonPath("$.content.[0].lnt").value(dto.getLnt()),
                        jsonPath("$.content.[0].distance").value(dto.getDistance()),
                        jsonPath("$.content.[0].recipeCount").value(dto.getRecipeCount())
                );

    }

    @Test
    @WithCustomMockUser
    @DisplayName("요청자 조회 성공")
    void success_getRequestedMemberList() throws Exception {

        //given
        RequestedMemberDto dto = RequestedMemberDto.builder()
                .memberId(1L)
                .profileUrl("https://s3.test.com/profile.png")
                .nickname("요청자")
                .distance(0.5)
                .lat(37.1)
                .lnt(127.1)
                .requestId(1L)
                .title("요리사님 도와주세요!")
                .content("제가 요리를 못합니다. 요리 부탁드려요!")
                .expectedPrice(100000L)
                .mainImageUrl("http://s3.test.com/mainImage.png")
                .build();

        given(memberService.getRequestedMemberList(anyLong(), any(), any()))
                .willReturn(ListResponse.from(
                        new PageImpl<>(
                                List.of(
                                        dto
                                )
                        )
                ));

        //when
        ResultActions perform = mockMvc.perform(get("/refrigerator/requester/0/10?type=DISTANCE"));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content.[0].memberId").value(dto.getMemberId()),
                        jsonPath("$.content.[0].profileUrl").value(dto.getProfileUrl()),
                        jsonPath("$.content.[0].nickname").value(dto.getNickname()),
                        jsonPath("$.content.[0].distance").value(dto.getDistance()),
                        jsonPath("$.content.[0].lat").value(dto.getLat()),
                        jsonPath("$.content.[0].lnt").value(dto.getLnt()),
                        jsonPath("$.content.[0].requestId").value(dto.getRequestId()),
                        jsonPath("$.content.[0].title").value(dto.getTitle()),
                        jsonPath("$.content.[0].content").value(dto.getContent()),
                        jsonPath("$.content.[0].expectedPrice").value(dto.getExpectedPrice()),
                        jsonPath("$.content.[0].mainImageUrl").value(dto.getMainImageUrl())
                );
    }
}