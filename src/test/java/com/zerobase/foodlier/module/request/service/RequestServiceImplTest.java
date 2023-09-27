package com.zerobase.foodlier.module.request.service;

import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.chef.exception.ChefMemberException;
import com.zerobase.foodlier.module.member.chef.repository.ChefMemberRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.request.domain.model.Request;
import com.zerobase.foodlier.module.request.exception.RequestException;
import com.zerobase.foodlier.module.request.repository.RequestRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.zerobase.foodlier.module.member.chef.exception.ChefMemberErrorCode.CHEF_MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.request.exception.RequestErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {
    @Mock
    private RequestRepository requestRepository;

    @Mock
    private ChefMemberRepository chefMemberRepository;

    @InjectMocks
    private RequestServiceImpl requestService;

    @Test
    @DisplayName("요청 보내기 성공")
    void success_sendRequest() {
        //given
        LocalDateTime now = LocalDateTime.now();

        Request request = Request.builder()
                .id(1L)
                .title("test")
                .content("test recipe")
                .expectPrice(10000L)
                .expectedAt(now)
                .isPaid(false)
                .ingredientList(List.of())
                .member(Member.builder()
                        .id(1L)
                        .build())
                .recipe(Recipe.builder()
                        .build())
                .build();

        MemberAuthDto memberAuthDto = MemberAuthDto.builder()
                .id(1L).build();

        given(requestRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(request));
        given(chefMemberRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(ChefMember.builder()
                        .id(1L)
                        .build()));

        //when
        requestService.sendRequest(memberAuthDto, 1L, 1L);

        //then
        verify(requestRepository, times(1))
                .save(Objects.requireNonNull(request));
    }

    @Test
    @DisplayName("요청 보내기 실패 - 요청서를 찾을 수 없음")
    void fail_sendRequest_requestNotFound() {
        //given
        given(requestRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        RequestException requestException = assertThrows(RequestException.class,
                () -> requestService.sendRequest(new MemberAuthDto(),
                        1L, 1L));

        //then
        assertEquals(REQUEST_NOT_FOUND, requestException.getErrorCode());
    }

    @Test
    @DisplayName("요청 보내기 실패 - 요청서의 요청자 id와 로그인한 멤버 id가 불일치")
    void fail_sendRequest_MemberRequestNotMatch() {
        //given
        given(requestRepository.findById(anyLong()))
                .willReturn(Optional.of(Request.builder()
                        .id(1L)
                        .member(Member.builder()
                                .id(1L).build())
                        .build()));

        //when
        RequestException requestException = assertThrows(RequestException.class,
                () -> requestService.sendRequest(MemberAuthDto.builder()
                                .id(2L)
                                .build(),
                        1L, 1L));

        //then
        assertEquals(MEMBER_REQUEST_NOT_MATCH, requestException.getErrorCode());
    }

    @Test
    @DisplayName("요청 보내기 실패 - 요리사를 찾을 수 없음")
    void fail_sendRequest_ChefMemberNotFound() {
        //given
        given(requestRepository.findById(anyLong()))
                .willReturn(Optional.of(Request.builder()
                        .id(1L)
                        .member(Member.builder()
                                .id(1L).build())
                        .build()));
        given(chefMemberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        ChefMemberException chefMemberException = assertThrows(ChefMemberException.class,
                () -> requestService.sendRequest(MemberAuthDto.builder()
                                .id(1L)
                                .build(),
                        1L, 1L));

        //then
        assertEquals(CHEF_MEMBER_NOT_FOUND, chefMemberException.getErrorCode());
    }

    @Test
    @DisplayName("요청 취소 성공")
    void success_cancelRequest() {
        //given
        LocalDateTime now = LocalDateTime.now();

        Request request = Request.builder()
                .id(1L)
                .title("test")
                .content("test recipe")
                .expectPrice(10000L)
                .expectedAt(now)
                .isPaid(false)
                .ingredientList(List.of())
                .member(Member.builder()
                        .id(1L)
                        .build())
                .recipe(Recipe.builder()
                        .build())
                .chefMember(ChefMember.builder()
                        .id(1L)
                        .build())
                .build();

        given(requestRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(request));

        //when
        requestService.cancelRequest(MemberAuthDto.builder()
                .id(1L).build(), 1L);

        //then
        assert request != null;
        verify(requestRepository, times(1))
                .save(request);
        assertNull(request.getChefMember());
    }

    @Test
    @DisplayName("요청 취소 실패 - 요청서를 찾을 수 없음")
    void fail_cancelRequest_requestNotFound() {
        //given
        given(requestRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        RequestException requestException = assertThrows(RequestException.class,
                () -> requestService.cancelRequest(new MemberAuthDto(), 1L));

        //then
        assertEquals(REQUEST_NOT_FOUND, requestException.getErrorCode());
    }

    @Test
    @DisplayName("요청 취소 실패 - 요청서의 멤버 id와 로그인한 멤버 id가 불일치")
    void fail_cancelRequest_MemberRequestNotMatch() {
        //given
        given(requestRepository.findById(anyLong()))
                .willReturn(Optional.of(Request.builder()
                        .id(1L)
                        .member(Member.builder()
                                .id(1L).build())
                        .build()));

        //when
        RequestException requestException = assertThrows(RequestException.class,
                () -> requestService.cancelRequest(MemberAuthDto.builder()
                        .id(2L)
                        .build(), 1L));

        //then
        assertEquals(MEMBER_REQUEST_NOT_MATCH, requestException.getErrorCode());
    }

    @Test
    @DisplayName("요청 수락 성공 - 사용자가 견적서를 수락했을 경우")
    void success_approveRequest_member() {
        //given
        LocalDateTime now = LocalDateTime.now();

        Request request = Request.builder()
                .id(1L)
                .title("test")
                .content("test recipe")
                .expectPrice(10000L)
                .expectedAt(now)
                .isPaid(false)
                .ingredientList(List.of())
                .member(Member.builder()
                        .id(1L)
                        .build())
                .recipe(Recipe.builder()
                        .isQuotation(true)
                        .build())
                .chefMember(ChefMember.builder()
                        .id(1L)
                        .build())
                .build();

        given(requestRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(request));

        //when
        Request approvedRequest = requestService.approveRequest(MemberAuthDto.builder()
                .id(1L).build(), 1L);

        //then
        assertEquals(approvedRequest, request);
    }

    @Test
    @DisplayName("요청 수락 성공 - 요청자가 요청서를 수락했을 경우")
    void success_approveRequest_chefMember() {
        //given
        LocalDateTime now = LocalDateTime.now();

        Request request = Request.builder()
                .id(1L)
                .title("test")
                .content("test recipe")
                .expectPrice(10000L)
                .expectedAt(now)
                .isPaid(false)
                .ingredientList(List.of())
                .member(Member.builder()
                        .id(1L)
                        .build())
                .recipe(Recipe.builder()
                        .isQuotation(false)
                        .build())
                .chefMember(ChefMember.builder()
                        .id(1L)
                        .build())
                .build();

        given(requestRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(request));

        //when
        Request approvedRequest = requestService.approveRequest(MemberAuthDto.builder()
                .id(1L).build(), 1L);

        //then
        assertEquals(approvedRequest, request);
    }

    @Test
    @DisplayName("요청 수락 실패 - 요청서를 찾을 수 없음")
    void fail_approveRequest_requestNotFound() {
        //given
        given(requestRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        RequestException requestException = assertThrows(RequestException.class,
                () -> requestService.approveRequest(new MemberAuthDto(), 1L));

        //then
        assertEquals(REQUEST_NOT_FOUND, requestException.getErrorCode());
    }

    @Test
    @DisplayName("요청 수락 실패 - 요청자가 견적서를 수락했을 시," +
            " 요청서의 요청자 id와 로그인한 멤버 id가 불일치")
    void fail_approveRequest_MemberRequestNotMatch() {
        //given
        given(requestRepository.findById(anyLong()))
                .willReturn(Optional.of(Request.builder()
                        .id(1L)
                        .member(Member.builder()
                                .id(1L)
                                .build())
                        .recipe(Recipe.builder()
                                .isQuotation(true)
                                .build())
                        .build()));

        //when
        RequestException requestException = assertThrows(RequestException.class,
                () -> requestService.approveRequest(MemberAuthDto.builder()
                        .id(2L)
                        .build(), 1L));

        //then
        assertEquals(MEMBER_REQUEST_NOT_MATCH, requestException.getErrorCode());
    }

    @Test
    @DisplayName("요청 수락 실패 - 요리사가 요청서를 수락했을 시," +
            " 요청서의 요리사 id와 로그인한 멤버 id가 불일치")
    void fail_approveRequest_ChefMemberRequestNotMatch() {
        //given
        given(requestRepository.findById(anyLong()))
                .willReturn(Optional.of(Request.builder()
                        .id(1L)
                        .member(Member.builder()
                                .id(1L)
                                .build())
                        .recipe(Recipe.builder()
                                .isQuotation(false)
                                .build())
                        .chefMember(ChefMember.builder()
                                .id(2L)
                                .build())
                        .build()));
        given(chefMemberRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(ChefMember.builder()
                        .id(1L)
                        .build()));

        //when
        RequestException requestException = assertThrows(RequestException.class,
                () -> requestService.approveRequest(MemberAuthDto.builder()
                        .id(1L)
                        .build(), 1L));

        //then
        assertEquals(CHEF_MEMBER_REQUEST_NOT_MATCH, requestException.getErrorCode());
    }

    @Test
    @DisplayName("요청 수락 실패 - 로그인한 회원의 요리사id를 찾을 수 없음")
    void fail_approveRequest_ChefMemberNotFound() {
        //given
        given(requestRepository.findById(anyLong()))
                .willReturn(Optional.of(Request.builder()
                        .id(1L)
                        .member(Member.builder()
                                .id(1L)
                                .build())
                        .recipe(Recipe.builder()
                                .isQuotation(false)
                                .build())
                        .chefMember(ChefMember.builder()
                                .id(2L)
                                .build())
                        .build()));
        given(chefMemberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        ChefMemberException chefMemberException = assertThrows(ChefMemberException.class,
                () -> requestService.approveRequest(MemberAuthDto.builder()
                        .id(1L)
                        .build(), 1L));

        //then
        assertEquals(CHEF_MEMBER_NOT_FOUND, chefMemberException.getErrorCode());
    }

    @Test
    @DisplayName("요청 거절 성공 - 사용자가 견적서를 거절했을 경우")
    void success_rejectRequest_member() {
        //given
        LocalDateTime now = LocalDateTime.now();

        Request request = Request.builder()
                .id(1L)
                .title("test")
                .content("test recipe")
                .expectPrice(10000L)
                .expectedAt(now)
                .isPaid(false)
                .ingredientList(List.of())
                .member(Member.builder()
                        .id(1L)
                        .build())
                .recipe(Recipe.builder()
                        .isQuotation(true)
                        .build())
                .chefMember(ChefMember.builder()
                        .id(1L)
                        .build())
                .build();

        given(requestRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(request));

        //when
        requestService.rejectRequest(MemberAuthDto.builder()
                .id(1L).build(), 1L);

        //then
        assert request != null;
        verify(requestRepository, times(1))
                .save(request);
        assertNull(request.getRecipe());
        assertNull(request.getChefMember());
    }

    @Test
    @DisplayName("요청 거절 성공 - 요청자가 요청서를 거절했을 경우")
    void success_rejectRequest_chefMember() {
        //given
        LocalDateTime now = LocalDateTime.now();

        Request request = Request.builder()
                .id(1L)
                .title("test")
                .content("test recipe")
                .expectPrice(10000L)
                .expectedAt(now)
                .isPaid(false)
                .ingredientList(List.of())
                .member(Member.builder()
                        .id(1L)
                        .build())
                .recipe(Recipe.builder()
                        .isQuotation(false)
                        .build())
                .chefMember(ChefMember.builder()
                        .id(1L)
                        .build())
                .build();

        given(requestRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(request));
        given(chefMemberRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(ChefMember.builder()
                        .id(1L)
                        .build()));

        //when
        requestService.rejectRequest(MemberAuthDto.builder()
                .id(1L).build(), 1L);

        //then
        assert request != null;
        verify(requestRepository, times(1))
                .save(request);
        assertNull(request.getChefMember());
    }

    @Test
    @DisplayName("요청 거절 실패 - 요청서를 찾을 수 없음")
    void fail_rejectRequest_requestNotFound() {
        //given
        given(requestRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        RequestException requestException = assertThrows(RequestException.class,
                () -> requestService.rejectRequest(new MemberAuthDto(), 1L));

        //then
        assertEquals(REQUEST_NOT_FOUND, requestException.getErrorCode());
    }

    @Test
    @DisplayName("요청 거절 실패 - 요청자가 견적서를 거절했을 시," +
            " 요청서의 요청자 id와 로그인한 멤버 id가 불일치")
    void fail_rejectRequest_MemberRequestNotMatch() {
        //given
        given(requestRepository.findById(anyLong()))
                .willReturn(Optional.of(Request.builder()
                        .id(1L)
                        .member(Member.builder()
                                .id(1L)
                                .build())
                        .recipe(Recipe.builder()
                                .isQuotation(true)
                                .build())
                        .build()));

        //when
        RequestException requestException = assertThrows(RequestException.class,
                () -> requestService.rejectRequest(MemberAuthDto.builder()
                        .id(2L)
                        .build(), 1L));

        //then
        assertEquals(MEMBER_REQUEST_NOT_MATCH, requestException.getErrorCode());
    }

    @Test
    @DisplayName("요청 거절 실패 - 요리사가 요청서를 거절했을 시," +
            " 요청서의 요리사 id와 로그인한 멤버 id가 불일치")
    void fail_rejectRequest_ChefMemberRequestNotMatch() {
        //given
        given(requestRepository.findById(anyLong()))
                .willReturn(Optional.of(Request.builder()
                        .id(1L)
                        .member(Member.builder()
                                .id(1L)
                                .build())
                        .recipe(Recipe.builder()
                                .isQuotation(false)
                                .build())
                        .chefMember(ChefMember.builder()
                                .id(2L)
                                .build())
                        .build()));
        given(chefMemberRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(ChefMember.builder()
                        .id(1L)
                        .build()));

        //when
        RequestException requestException = assertThrows(RequestException.class,
                () -> requestService.rejectRequest(MemberAuthDto.builder()
                        .id(1L)
                        .build(), 1L));

        //then
        assertEquals(CHEF_MEMBER_REQUEST_NOT_MATCH, requestException.getErrorCode());
    }

    @Test
    @DisplayName("요청 거절 실패 - 로그인한 회원의 요리사id를 찾을 수 없음")
    void fail_rejectRequest_ChefMemberNotFound() {
        //given
        given(requestRepository.findById(anyLong()))
                .willReturn(Optional.of(Request.builder()
                        .id(1L)
                        .member(Member.builder()
                                .id(1L)
                                .build())
                        .recipe(Recipe.builder()
                                .isQuotation(false)
                                .build())
                        .chefMember(ChefMember.builder()
                                .id(2L)
                                .build())
                        .build()));
        given(chefMemberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        ChefMemberException chefMemberException = assertThrows(ChefMemberException.class,
                () -> requestService.rejectRequest(MemberAuthDto.builder()
                        .id(1L)
                        .build(), 1L));

        //then
        assertEquals(CHEF_MEMBER_NOT_FOUND, chefMemberException.getErrorCode());
    }
}