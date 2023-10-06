package com.zerobase.foodlier.module.review.chef.service;

import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.chef.exception.ChefMemberException;
import com.zerobase.foodlier.module.member.chef.repository.ChefMemberRepository;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.request.domain.model.Request;
import com.zerobase.foodlier.module.request.exception.RequestException;
import com.zerobase.foodlier.module.request.repository.RequestRepository;
import com.zerobase.foodlier.module.review.chef.domain.model.ChefReview;
import com.zerobase.foodlier.module.review.chef.dto.ChefReviewForm;
import com.zerobase.foodlier.module.review.chef.dto.ChefReviewResponseDto;
import com.zerobase.foodlier.module.review.chef.exception.ChefReviewException;
import com.zerobase.foodlier.module.review.chef.repository.ChefReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.zerobase.foodlier.module.member.chef.exception.ChefMemberErrorCode.CHEF_MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.zerobase.foodlier.module.request.exception.RequestErrorCode.REQUEST_NOT_FOUND;
import static com.zerobase.foodlier.module.review.chef.exception.ChefReviewErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChefReviewServiceImplTest {

    @Mock
    private ChefReviewRepository chefReviewRepository;
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ChefMemberRepository chefMemberRepository;
    @InjectMocks
    private ChefReviewServiceImpl chefReviewService;

    @Test
    @DisplayName("요리사 후기 작성 성공")
    void success_createChefReview(){
        //given
        ChefMember chefMember = ChefMember.builder()
                .id(1L)
                .build();

        Member member = Member.builder()
                .id(1L)
                .chefMember(chefMember)
                .build();

        Request request = Request.builder()
                .id(1L)
                .chefMember(chefMember)
                .member(member)
                .isPaid(true)
                .build();

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));

        given(requestRepository.findByIdAndMember(anyLong(), any()))
                .willReturn(Optional.of(request));

        given(chefReviewRepository.existsByRequest(any()))
                .willReturn(false);

        ChefReviewForm form = ChefReviewForm.builder()
                .content("후기 남깁니다.")
                .star(5)
                .build();

        //when
        Long chefMemberId = chefReviewService.createChefReview(1L, 1L, form);

        //then
        ArgumentCaptor<ChefReview> captor = ArgumentCaptor.forClass(ChefReview.class);
        verify(chefReviewRepository, times(1)).save(captor.capture());

        ChefReview chefReview = captor.getValue();

        assertAll(
                () -> assertEquals(chefMember.getId(), chefMemberId),
                () -> assertEquals(form.getContent(), chefReview.getContent()),
                () -> assertEquals(form.getStar(), chefReview.getStar()),
                () -> assertEquals(request.getId(), chefReview.getRequest().getId())
        );
    }

    @Test
    @DisplayName("요리사 후기 작성 실패 - 회원 X")
    void fail_createChefReview_member_not_found(){
        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when
        MemberException exception = assertThrows(MemberException.class,
                () -> chefReviewService.createChefReview(1L, 1L,
                        ChefReviewForm.builder().build()));

        //then
        assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("요리사 후기 작성 실패 - 요청 X")
    void fail_createChefReview_request_not_found(){
        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder().build()));

        given(requestRepository.findByIdAndMember(anyLong(), any()))
                .willReturn(Optional.empty());

        //when
        RequestException exception = assertThrows(RequestException.class,
                () -> chefReviewService.createChefReview(1L, 1L,
                        ChefReviewForm.builder().build()));

        //then
        assertEquals(REQUEST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("요리사 후기 작성 실패 - 요청자가 아님")
    void fail_createChefReview_you_are_not_requester(){
        //given
        ChefMember chefMember = ChefMember.builder()
                .id(1L)
                .build();

        Member member = Member.builder()
                .id(1L)
                .chefMember(chefMember)
                .build();

        Request request = Request.builder()
                .id(1L)
                .chefMember(chefMember)
                .member(Member.builder().id(2L).build())
                .isPaid(true)
                .build();

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));

        given(requestRepository.findByIdAndMember(anyLong(), any()))
                .willReturn(Optional.of(request));

        //when
        ChefReviewException exception = assertThrows(ChefReviewException.class,
                () -> chefReviewService.createChefReview(1L, 1L,
                        ChefReviewForm.builder().build()));

        //then
        assertEquals(YOU_ARE_NOT_REQUESTER, exception.getErrorCode());
    }

    @Test
    @DisplayName("요리사 후기 작성 실패 - 성사된 요청이 아님")
    void fail_createChefReview_is_not_closed_request(){
        //given
        ChefMember chefMember = ChefMember.builder()
                .id(1L)
                .build();

        Member member = Member.builder()
                .id(1L)
                .chefMember(chefMember)
                .build();

        Request request = Request.builder()
                .id(1L)
                .chefMember(chefMember)
                .member(member)
                .isPaid(false)
                .build();

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));

        given(requestRepository.findByIdAndMember(anyLong(), any()))
                .willReturn(Optional.of(request));

        //when
        ChefReviewException exception = assertThrows(ChefReviewException.class,
                () -> chefReviewService.createChefReview(1L, 1L,
                        ChefReviewForm.builder().build()));

        //then
        assertEquals(IS_NOT_CLOSED_REQUEST, exception.getErrorCode());
    }

    @Test
    @DisplayName("요리사 후기 작성 실패 - 이미 남긴 후기")
    void fail_createChefReview_already_reviewed_to_chef(){
        //given
        ChefMember chefMember = ChefMember.builder()
                .id(1L)
                .build();

        Member member = Member.builder()
                .id(1L)
                .chefMember(chefMember)
                .build();

        Request request = Request.builder()
                .id(1L)
                .chefMember(chefMember)
                .member(member)
                .isPaid(true)
                .build();

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));

        given(requestRepository.findByIdAndMember(anyLong(), any()))
                .willReturn(Optional.of(request));

        given(chefReviewRepository.existsByRequest(any()))
                .willReturn(true);

        //when
        ChefReviewException exception = assertThrows(ChefReviewException.class,
                () -> chefReviewService.createChefReview(1L, 1L,
                        ChefReviewForm.builder().build()));

        //then
        assertEquals(ALREADY_REVIEWED_TO_CHEF, exception.getErrorCode());
    }

    @Test
    @DisplayName("요리사 후기 목록 조회 성공")
    void success_getChefReviewList(){
        //given

        Member member = Member.builder()
                .id(1L)
                .nickname("nickname")
                .profileUrl("https://s3.test.com/image.png")
                .build();

        Request request = Request.builder()
                .member(member)
                .build();

        ChefMember chefMember = ChefMember.builder()
                .id(1L)
                .build();

        given(chefMemberRepository.findById(anyLong()))
                .willReturn(Optional.of(chefMember));

        given(chefReviewRepository.findByChefMemberOrderByCreatedAtDesc(any(), any()))
                .willReturn(
                        new PageImpl<>(
                                new ArrayList<>(
                                        Arrays.asList(
                                                ChefReview.builder()
                                                        .content("content")
                                                        .star(5)
                                                        .chefMember(chefMember)
                                                        .request(request)
                                                        .build()
                                        )
                                )
                        )
                );
        //when
        List<ChefReviewResponseDto> chefReviewList = chefReviewService.getChefReviewList(
                1L, PageRequest.of(0, 10)
        );

        //then
        assertAll(
                () -> assertEquals(member.getNickname(), chefReviewList.get(0).getNickname()),
                () -> assertEquals(member.getProfileUrl(), chefReviewList.get(0).getProfileUrl()),
                () -> assertEquals("content", chefReviewList.get(0).getContent()),
                () -> assertEquals(5, chefReviewList.get(0).getStar())
        );
    }

    @Test
    @DisplayName("요리사 후기 목록 조회 실패")
    void fail_getChefReviewList_chef_member_not_found(){
        //given
        given(chefMemberRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        ChefMemberException exception = assertThrows(ChefMemberException.class,
                () -> chefReviewService.getChefReviewList(1L,
                        PageRequest.of(0 , 10)));

        //then
        assertEquals(CHEF_MEMBER_NOT_FOUND, exception.getErrorCode());
    }

}