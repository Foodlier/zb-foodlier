package com.zerobase.foodlier.module.request.service;

import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.chef.exception.ChefMemberException;
import com.zerobase.foodlier.module.member.chef.repository.ChefMemberRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.request.domain.model.Request;
import com.zerobase.foodlier.module.request.domain.vo.Ingredient;
import com.zerobase.foodlier.module.request.exception.RequestException;
import com.zerobase.foodlier.module.request.repository.RequestRepository;
import com.zerobase.foodlier.module.requestform.domain.model.RequestForm;
import com.zerobase.foodlier.module.requestform.repository.RequestFormRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.zerobase.foodlier.module.member.chef.exception.ChefMemberErrorCode.CHEF_MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.request.exception.RequestErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {

    @Mock
    private RequestRepository requestRepository;
    @Mock
    private RequestFormRepository requestFormRepository;
    @Mock
    private ChefMemberRepository chefMemberRepository;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private RequestServiceImpl requestService;

    @Nested
    @DisplayName("setDmRoom() 테스트")
    class setDmRoomTest{

        @Test
        @DisplayName("dm room 할당 성공")
        void success_setDmRoom(){
            //given
            DmRoom dmRoom = DmRoom.builder()
                    .id(1L)
                    .isExist(false)
                    .build();

            Request request = Request.builder()
                    .title("title")
                    .content("content")
                    .build();

            //when
            requestService.setDmRoom(request, dmRoom);

            //then
            ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
            verify(requestRepository, times(1)).save(captor.capture());

            assertEquals(dmRoom, captor.getValue().getDmRoom());

        }

    }

    @Nested
    @DisplayName("sendRequest() 테스트")
    class sendRequestTest{

        @Test
        @DisplayName("요청 보내기 성공 - 레시피 태그")
        void success_sendRequest_tagged_recipe(){
            //given
            Member requester = Member.builder()
                    .id(1L)
                    .nickname("requester")
                    .build();

            Member memberOfChef = Member.builder()
                    .id(2L)
                    .nickname("chef")
                    .build();

            ChefMember chefMember = ChefMember.builder()
                    .id(1L)
                    .member(memberOfChef)
                    .build();

            LocalDateTime time = LocalDateTime.now();

            RequestForm requestForm = RequestForm.builder()
                    .id(1L)
                    .member(requester)
                    .recipe(
                            Recipe.builder()
                                    .id(1L)
                                    .isQuotation(false)
                                    .build()
                    )
                    .title("title")
                    .content("content")
                    .ingredientList(
                            List.of(new Ingredient("재료1"))
                    )
                    .expectedPrice(12000L)
                    .expectedAt(time)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(requester));

            given(chefMemberRepository.findById(anyLong()))
                    .willReturn(Optional.of(chefMember));

            given(requestFormRepository.findByIdAndMember(anyLong(), any()))
                    .willReturn(Optional.of(requestForm));

            //when
            requestService.sendRequest(1L, 1L, 1L);

            //then
            ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
            verify(requestRepository, times(1)).save(captor.capture());
            Request request = captor.getValue();
            assertAll(
                    () -> assertEquals(requestForm.getTitle(), request.getTitle()),
                    () -> assertEquals(requestForm.getContent(), request.getContent()),
                    () -> assertEquals(requestForm.getMember(), request.getMember()),
                    () -> assertEquals(requestForm.getRecipe(), request.getRecipe()),
                    () -> assertEquals(requestForm.getIngredientList().get(0).getIngredientName(),
                            request.getIngredientList().get(0).getIngredientName()),
                    () -> assertEquals(requestForm.getExpectedPrice(), request.getExpectedPrice()),
                    () -> assertEquals(requestForm.getExpectedAt(), request.getExpectedAt()),
                    () -> assertEquals(chefMember, request.getChefMember())
            );

        }

        @Test
        @DisplayName("요청 보내기 성공 - 레시피 미태그")
        void success_sendRequest_untagged_recipe(){
            //given
            Member requester = Member.builder()
                    .id(1L)
                    .nickname("requester")
                    .build();

            Member memberOfChef = Member.builder()
                    .id(2L)
                    .nickname("chef")
                    .build();

            ChefMember chefMember = ChefMember.builder()
                    .id(1L)
                    .member(memberOfChef)
                    .build();

            LocalDateTime time = LocalDateTime.now();

            RequestForm requestForm = RequestForm.builder()
                    .id(1L)
                    .member(requester)
                    .recipe(
                        null
                    )
                    .title("title")
                    .content("content")
                    .ingredientList(
                            List.of(new Ingredient("재료1"))
                    )
                    .expectedPrice(12000L)
                    .expectedAt(time)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(requester));

            given(chefMemberRepository.findById(anyLong()))
                    .willReturn(Optional.of(chefMember));

            given(requestFormRepository.findByIdAndMember(anyLong(), any()))
                    .willReturn(Optional.of(requestForm));

            //when
            requestService.sendRequest(1L, 1L, 1L);

            //then
            ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
            verify(requestRepository, times(1)).save(captor.capture());
            Request request = captor.getValue();
            assertAll(
                    () -> assertEquals(requestForm.getTitle(), request.getTitle()),
                    () -> assertEquals(requestForm.getContent(), request.getContent()),
                    () -> assertEquals(requestForm.getMember(), request.getMember()),
                    () -> assertNull(request.getRecipe()),
                    () -> assertEquals(requestForm.getIngredientList().get(0).getIngredientName(),
                            request.getIngredientList().get(0).getIngredientName()),
                    () -> assertEquals(requestForm.getExpectedPrice(), request.getExpectedPrice()),
                    () -> assertEquals(requestForm.getExpectedAt(), request.getExpectedAt()),
                    () -> assertEquals(chefMember, request.getChefMember())
            );

        }

        @Test
        @DisplayName("요청 보내기 실패 - 회원 X")
        void fail_sendRequest_member_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.empty());

            //when
            MemberException exception = assertThrows(MemberException.class,
                    () -> requestService.sendRequest(1L, 1L, 1L));

            //then
            assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("요청 보내기 실패 - 요리사 X")
        void fail_sendRequest_chef_member_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(Member.builder().build()));

            given(chefMemberRepository.findById(anyLong()))
                    .willReturn(Optional.empty());

            //when
            ChefMemberException exception = assertThrows(ChefMemberException.class,
                    () -> requestService.sendRequest(1L, 1L, 1L));

            //then
            assertEquals(CHEF_MEMBER_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("요청 보내기 실패 - 본인에게 요청함")
        void fail_sendRequest_request_to_me(){
            //given
            Member requester = Member.builder()
                    .id(1L)
                    .nickname("requester")
                    .build();

            ChefMember chefMember = ChefMember.builder()
                    .id(1L)
                    .member(requester)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(requester));

            given(chefMemberRepository.findById(anyLong()))
                    .willReturn(Optional.of(chefMember));

            //when
            RequestException exception = assertThrows(RequestException.class,
                    () -> requestService.sendRequest(1L, 1L, 1L));

            //then
            assertEquals(CANNOT_REQUEST_TO_ME, exception.getErrorCode());
        }

        @Test
        @DisplayName("요청 보내기 실패 - 이미 요리사에게 요청하였음.")
        void fail_sendRequest_already_requested_chef(){
            //given
            Member requester = Member.builder()
                    .id(1L)
                    .nickname("requester")
                    .build();

            ChefMember chefMember = ChefMember.builder()
                    .id(1L)
                    .member(Member.builder().id(2L).build())
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(requester));

            given(chefMemberRepository.findById(anyLong()))
                    .willReturn(Optional.of(chefMember));

            given(requestRepository.existsByMemberAndChefMemberAndIsPaidFalse(requester, chefMember))
                    .willReturn(true);

            //when
            RequestException exception = assertThrows(RequestException.class,
                    () -> requestService.sendRequest(1L, 1L, 1L));

            //then
            assertEquals(ALREADY_REQUESTED_CHEF, exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("cancelRequest() 테스트")
    class cancelRequestTest{

        @Test
        @DisplayName("요청 취소 성공")
        void success_cancelRequest(){
            //given
            Member requester = Member.builder()
                    .id(1L)
                    .nickname("requester")
                    .build();

            Request request = Request.builder()
                    .id(1L)
                    .member(requester)
                    .dmRoom(null)
                    .isPaid(false)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(requester));

            given(requestRepository.findById(anyLong()))
                    .willReturn(Optional.of(request));

            //when
            requestService.cancelRequest(1L, 1L);

            //then
            ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
            verify(requestRepository, times(1)).delete(captor.capture());

            assertEquals(request, captor.getValue());
        }

        @Test
        @DisplayName("요청 취소 실패 - 회원 X")
        void fail_cancelRequest_member_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.empty());

            //when
            MemberException exception = assertThrows(MemberException.class,
                    () -> requestService.cancelRequest(1L, 1L));

            //then
            assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("요청 취소 실패 - 요청 X")
        void fail_cancelRequest_request_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(Member.builder().build()));

            given(requestRepository.findById(anyLong()))
                    .willReturn(Optional.empty());

            //when
            RequestException exception = assertThrows(RequestException.class,
                    () -> requestService.cancelRequest(1L, 1L));

            //then
            assertEquals(REQUEST_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("요청 취소 실패 - 요청의 주인이 같지 않음.")
        void fail_cancelRequest_member_request_not_match(){
            //given
            Member requester = Member.builder()
                    .id(1L)
                    .nickname("requester")
                    .build();

            Member hacker = Member.builder()
                    .id(2L)
                    .nickname("hacker")
                    .build();

            Request request = Request.builder()
                    .id(1L)
                    .member(requester)
                    .dmRoom(null)
                    .isPaid(false)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(hacker));

            given(requestRepository.findById(anyLong()))
                    .willReturn(Optional.of(request));

            //when
            RequestException exception = assertThrows(RequestException.class,
                    () -> requestService.cancelRequest(2L, 1L));

            //then

            assertEquals(MEMBER_REQUEST_NOT_MATCH, exception.getErrorCode());
        }

        @Test
        @DisplayName("요청 취소 실패 - 이미 수락한 요청임.")
        void fail_cancelRequest_cannot_cancel_approved(){
            //given
            Member requester = Member.builder()
                    .id(1L)
                    .nickname("requester")
                    .build();

            Request request = Request.builder()
                    .id(1L)
                    .member(requester)
                    .dmRoom(DmRoom.builder().build())
                    .isPaid(false)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(requester));

            given(requestRepository.findById(anyLong()))
                    .willReturn(Optional.of(request));

            //when
            RequestException exception = assertThrows(RequestException.class,
                    () -> requestService.cancelRequest(2L, 1L));

            //then

            assertEquals(CANNOT_CANCEL_APPROVED, exception.getErrorCode());
        }

        @Test
        @DisplayName("요청 취소 실패 - 이미 성사된 요청임")
        void fail_cancelRequest_cannot_cancel_isPaid(){
            //given
            Member requester = Member.builder()
                    .id(1L)
                    .nickname("requester")
                    .build();

            Request request = Request.builder()
                    .id(1L)
                    .member(requester)
                    .dmRoom(null)
                    .isPaid(true)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(requester));

            given(requestRepository.findById(anyLong()))
                    .willReturn(Optional.of(request));

            //when
            RequestException exception = assertThrows(RequestException.class,
                    () -> requestService.cancelRequest(2L, 1L));

            //then

            assertEquals(CANNOT_CANCEL_IS_PAID, exception.getErrorCode());
        }

    }

    @Nested
    @DisplayName("requesterApproveRequest() 테스트")
    class requesterApproveRequestTest{

        @Test
        @DisplayName("요청자가 요청을 수락을 성공")
        void success_requestApproveRequest(){
            //given
            Member requester = Member.builder()
                    .id(1L)
                    .nickname("requester")
                    .build();

            Request request = Request.builder()
                    .id(1L)
                    .member(requester)
                    .recipe(Recipe.builder()
                            .id(1L)
                            .isQuotation(true)
                            .build())
                    .dmRoom(null)
                    .isPaid(false)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(requester));

            given(requestRepository.findById(anyLong()))
                    .willReturn(Optional.of(request));

            //when
            Request responseRequest = requestService.requesterApproveRequest(1L, 1L);

            //then
            assertEquals(responseRequest, request);

        }
        @Test
        @DisplayName("요청자가 요청을 수락을 실패 - 회원 X")
        void fail_requestApproveRequest_member_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.empty());

            //when
            MemberException exception = assertThrows(MemberException.class,
                    () -> requestService.requesterApproveRequest(1L, 1L));

            //then
            assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());

        }
        @Test
        @DisplayName("요청자가 요청을 수락을 실패 - 요청 X")
        void fail_requestApproveRequest_request_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(Member.builder().build()));

            given(requestRepository.findById(anyLong()))
                    .willReturn(Optional.empty());

            //when
            RequestException exception = assertThrows(RequestException.class,
                    () -> requestService.requesterApproveRequest(1L, 1L));

            //then
            assertEquals(REQUEST_NOT_FOUND, exception.getErrorCode());

        }

        @Test
        @DisplayName("요청자가 요청을 수락을 실패 - 회원과 요청의 회원이 일치 X")
        void fail_requestApproveRequest_member_request_not_match(){
            //given
            Member requester = Member.builder()
                    .id(1L)
                    .nickname("requester")
                    .build();

            Member hacker = Member.builder()
                    .id(2L)
                    .nickname("hacker")
                    .build();

            Request request = Request.builder()
                    .id(1L)
                    .member(requester)
                    .recipe(Recipe.builder()
                            .id(1L)
                            .isQuotation(true)
                            .build())
                    .dmRoom(null)
                    .isPaid(false)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(hacker));

            given(requestRepository.findById(anyLong()))
                    .willReturn(Optional.of(request));

            //when
            RequestException exception = assertThrows(RequestException.class,
                    () -> requestService.requesterApproveRequest(1L, 1L));

            //then
            assertEquals(MEMBER_REQUEST_NOT_MATCH, exception.getErrorCode());

        }

        @Test
        @DisplayName("요청자가 요청을 수락을 실패 - 견적서가 없음")
        void fail_requestApproveRequest_cannot_requester_approve_has_not_quotation(){
            //given
            Member requester = Member.builder()
                    .id(1L)
                    .nickname("requester")
                    .build();

            Request request = Request.builder()
                    .id(1L)
                    .member(requester)
                    .recipe(null)
                    .dmRoom(null)
                    .isPaid(false)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(requester));

            given(requestRepository.findById(anyLong()))
                    .willReturn(Optional.of(request));

            //when
            RequestException exception = assertThrows(RequestException.class,
                    () -> requestService.requesterApproveRequest(1L, 1L));

            //then
            assertEquals(CANNOT_REQUESTER_APPROVE_HAS_NOT_QUOTATION, exception.getErrorCode());

        }

        @Test
        @DisplayName("요청자가 요청을 수락을 실패 - 견적서가 아닌 요청임")
        void fail_requestApproveRequest_cannot_requester_approve_is_not_quotation(){
            //given
            Member requester = Member.builder()
                    .id(1L)
                    .nickname("requester")
                    .build();

            Request request = Request.builder()
                    .id(1L)
                    .member(requester)
                    .recipe(Recipe.builder()
                            .id(1L)
                            .isQuotation(false)
                            .build())
                    .dmRoom(null)
                    .isPaid(true)
                    .paidPrice(10000L)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(requester));

            given(requestRepository.findById(anyLong()))
                    .willReturn(Optional.of(request));

            //when
            RequestException exception = assertThrows(RequestException.class,
                    () -> requestService.requesterApproveRequest(1L, 1L));

            //then
            assertEquals(CANNOT_REQUESTER_APPROVE_IS_NOT_QUOTATION, exception.getErrorCode());

        }

        @Test
        @DisplayName("요청자가 요청을 수락을 실패 - 이미 수락된 요청")
        void fail_requestApproveRequest_already_approved(){
            //given
            Member requester = Member.builder()
                    .id(1L)
                    .nickname("requester")
                    .build();

            Request request = Request.builder()
                    .id(1L)
                    .member(requester)
                    .recipe(Recipe.builder()
                            .id(1L)
                            .isQuotation(true)
                            .build())
                    .dmRoom(DmRoom.builder().build())
                    .isPaid(true)
                    .paidPrice(10000L)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(requester));

            given(requestRepository.findById(anyLong()))
                    .willReturn(Optional.of(request));

            //when
            RequestException exception = assertThrows(RequestException.class,
                    () -> requestService.requesterApproveRequest(1L, 1L));

            //then
            assertEquals(ALREADY_APPROVED, exception.getErrorCode());

        }

    }

    @Nested
    @DisplayName("chefApproveRequest() 테스트")
    class chefApproveRequestTest{

        @Test
        @DisplayName("요리사가 요청을 수락 성공")
        void success_chefApproveRequest(){
            //given
            Member member = Member.builder()
                    .id(1L)
                    .nickname("member")
                    .build();

            ChefMember chefMember = ChefMember.builder()
                    .id(1L)
                    .member(member)
                    .build();

            member.setChefMember(chefMember);

            Request request = Request.builder()
                    .id(1L)
                    .chefMember(chefMember)
                    .recipe(Recipe.builder()
                            .id(1L)
                            .isQuotation(false)
                            .build())
                    .dmRoom(null)
                    .isPaid(false)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(member));

            given(requestRepository.findById(anyLong()))
                    .willReturn(Optional.of(request));

            //when
            Request responseRequest = requestService.chefApproveRequest(1L, 1L);

            //then
            assertEquals(request, responseRequest);

        }

        @Test
        @DisplayName("요리사가 요청을 수락 실패 - 회원 X")
        void success_chefApproveRequest_member_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.empty());

            //when
            MemberException exception = assertThrows(MemberException.class,
                    () -> requestService.chefApproveRequest(1L, 1L));

            //then
            assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());

        }

        @Test
        @DisplayName("요리사가 요청을 수락 실패 - 요청 X")
        void success_chefApproveRequest_request_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(Member.builder().build()));

            given(requestRepository.findById(anyLong()))
                    .willReturn(Optional.empty());
            //when
            RequestException exception = assertThrows(RequestException.class,
                    () -> requestService.chefApproveRequest(1L, 1L));

            //then
            assertEquals(REQUEST_NOT_FOUND, exception.getErrorCode());

        }

        @Test
        @DisplayName("요리사가 요청을 수락 실패 - 요청에 할당된 요리사와 회원과 매칭 X")
        void success_chefApproveRequest_chef_member_request_not_match(){
            //given
            Member member = Member.builder()
                    .id(1L)
                    .nickname("member")
                    .build();

            ChefMember chefMember = ChefMember.builder()
                    .id(1L)
                    .member(member)
                    .build();

            member.setChefMember(chefMember);

            Request request = Request.builder()
                    .id(1L)
                    .chefMember(chefMember)
                    .recipe(Recipe.builder()
                            .id(1L)
                            .isQuotation(false)
                            .build())
                    .dmRoom(null)
                    .isPaid(false)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(Member.builder().id(2L).build()));

            given(requestRepository.findById(anyLong()))
                    .willReturn(Optional.of(request));

            //when
            RequestException exception = assertThrows(RequestException.class,
                    () -> requestService.chefApproveRequest(1L, 1L));

            //then
            assertEquals(CHEF_MEMBER_REQUEST_NOT_MATCH, exception.getErrorCode());

        }

        @Test
        @DisplayName("요리사가 요청을 수락 실패 - 레시피가 할당되지 않음")
        void success_chefApproveRequest_cannot_chef_approve_has_not_recipe(){
            //given
            Member member = Member.builder()
                    .id(1L)
                    .nickname("member")
                    .build();

            ChefMember chefMember = ChefMember.builder()
                    .id(1L)
                    .member(member)
                    .build();

            member.setChefMember(chefMember);

            Request request = Request.builder()
                    .id(1L)
                    .chefMember(chefMember)
                    .recipe(null)
                    .dmRoom(null)
                    .isPaid(false)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(member));

            given(requestRepository.findById(anyLong()))
                    .willReturn(Optional.of(request));

            //when
            RequestException exception = assertThrows(RequestException.class,
                    () -> requestService.chefApproveRequest(1L, 1L));

            //then
            assertEquals(CANNOT_CHEF_APPROVE_HAS_NOT_RECIPE, exception.getErrorCode());

        }

        @Test
        @DisplayName("요리사가 요청을 수락 실패 - 레시피가 견적서인 경우")
        void success_chefApproveRequest_cannot_chef_approve_is_quotation(){
            //given
            Member member = Member.builder()
                    .id(1L)
                    .nickname("member")
                    .build();

            ChefMember chefMember = ChefMember.builder()
                    .id(1L)
                    .member(member)
                    .build();

            member.setChefMember(chefMember);

            Request request = Request.builder()
                    .id(1L)
                    .chefMember(chefMember)
                    .recipe(Recipe.builder()
                            .id(1L)
                            .isQuotation(true)
                            .build())
                    .dmRoom(null)
                    .isPaid(false)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(member));

            given(requestRepository.findById(anyLong()))
                    .willReturn(Optional.of(request));

            //when
            RequestException exception = assertThrows(RequestException.class,
                    () -> requestService.chefApproveRequest(1L, 1L));

            //then
            assertEquals(CANNOT_CHEF_APPROVE_IS_QUOTATION, exception.getErrorCode());

        }

    }

    @Nested
    @DisplayName("rejectRequest() 테스트")
    class rejectRequestTest{

        @Test
        @DisplayName("요리사가 요청 거절 성공")
        void success_rejectRequest(){
            //given
            Member member = Member.builder()
                    .id(1L)
                    .nickname("member")
                    .build();

            ChefMember chefMember = ChefMember.builder()
                    .id(1L)
                    .member(member)
                    .build();

            member.setChefMember(chefMember);

            Request request = Request.builder()
                    .id(1L)
                    .chefMember(chefMember)
                    .recipe(Recipe.builder()
                            .id(1L)
                            .isQuotation(false)
                            .build())
                    .dmRoom(null)
                    .isPaid(false)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(member));

            given(requestRepository.findById(anyLong()))
                    .willReturn(Optional.of(request));

            //when
            requestService.rejectRequest(1L, 1L);

            //then
            ArgumentCaptor<Request> captor = ArgumentCaptor.forClass(Request.class);
            verify(requestRepository, times(1)).delete(captor.capture());
            assertEquals(request, captor.getValue());
        }

        @Test
        @DisplayName("요리사가 요청 거절 실패 - 회원 X")
        void fail_rejectRequest_member_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.empty());

            //when
            MemberException exception = assertThrows(MemberException.class,
                    () -> requestService.rejectRequest(1L, 1L));

            //then
            assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("요리사가 요청 거절 실패 - 요청 X")
        void fail_rejectRequest_request_not_found(){
            //given
            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(Member.builder().build()));

            given(requestRepository.findById(anyLong()))
                    .willReturn(Optional.empty());

            //when
            RequestException exception = assertThrows(RequestException.class,
                    () -> requestService.rejectRequest(1L, 1L));

            //then
            assertEquals(REQUEST_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("요리사가 요청 거절 실패 - 요청에 할당된 요리사와 매칭 X")
        void fail_rejectRequest_chef_member_request_not_match(){
            //given
            Member member = Member.builder()
                    .id(1L)
                    .nickname("member")
                    .build();

            ChefMember chefMember = ChefMember.builder()
                    .id(1L)
                    .member(member)
                    .build();

            member.setChefMember(chefMember);

            Request request = Request.builder()
                    .id(1L)
                    .chefMember(chefMember)
                    .dmRoom(null)
                    .isPaid(false)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(Member.builder().id(2L).build()));

            given(requestRepository.findById(anyLong()))
                    .willReturn(Optional.of(request));

            //when
            RequestException exception = assertThrows(RequestException.class,
                    () -> requestService.rejectRequest(1L, 1L));

            //then
            assertEquals(CHEF_MEMBER_REQUEST_NOT_MATCH, exception.getErrorCode());
        }

        @Test
        @DisplayName("요리사가 요청 거절 실패 - 이미 수락된 요청")
        void fail_rejectRequest_cannot_reject_approved(){
            //given
            Member member = Member.builder()
                    .id(1L)
                    .nickname("member")
                    .build();

            ChefMember chefMember = ChefMember.builder()
                    .id(1L)
                    .member(member)
                    .build();

            member.setChefMember(chefMember);

            Request request = Request.builder()
                    .id(1L)
                    .chefMember(chefMember)
                    .dmRoom(DmRoom.builder().build())
                    .isPaid(false)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(member));

            given(requestRepository.findById(anyLong()))
                    .willReturn(Optional.of(request));

            //when
            RequestException exception = assertThrows(RequestException.class,
                    () -> requestService.rejectRequest(1L, 1L));

            //then
            assertEquals(CANNOT_REJECT_APPROVED, exception.getErrorCode());
        }

        @Test
        @DisplayName("요리사가 요청 거절 실패 - 이미 결제된 요청")
        void fail_rejectRequest_cannot_reject_isPaid(){
            //given
            Member member = Member.builder()
                    .id(1L)
                    .nickname("member")
                    .build();

            ChefMember chefMember = ChefMember.builder()
                    .id(1L)
                    .member(member)
                    .build();

            member.setChefMember(chefMember);

            Request request = Request.builder()
                    .id(1L)
                    .chefMember(chefMember)
                    .isPaid(true)
                    .build();

            given(memberRepository.findById(anyLong()))
                    .willReturn(Optional.of(member));

            given(requestRepository.findById(anyLong()))
                    .willReturn(Optional.of(request));

            //when
            RequestException exception = assertThrows(RequestException.class,
                    () -> requestService.rejectRequest(1L, 1L));

            //then
            assertEquals(CANNOT_REJECT_IS_PAID, exception.getErrorCode());
        }

    }
}