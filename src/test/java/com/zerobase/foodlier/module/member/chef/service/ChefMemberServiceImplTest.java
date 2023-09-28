package com.zerobase.foodlier.module.member.chef.service;

import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.chef.dto.ChefIntroduceForm;
import com.zerobase.foodlier.module.member.chef.exception.ChefMemberException;
import com.zerobase.foodlier.module.member.chef.repository.ChefMemberRepository;
import com.zerobase.foodlier.module.member.chef.type.GradeType;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.exception.MemberErrorCode;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.zerobase.foodlier.module.member.chef.exception.ChefMemberErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChefMemberServiceImplTest {

    @Mock
    private ChefMemberRepository chefMemberRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private RecipeRepository recipeRepository;
    @InjectMocks
    private ChefMemberServiceImpl chefMemberService;

    @Test
    @DisplayName("요리사 등록 성공")
    void success_registerChef(){

        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder()
                        .id(1L)
                        .email("test@test.com")
                        .nickname("nickname")
                        .build())
                );

        given(chefMemberRepository.existsByMember(any()))
                .willReturn(false);

        given(recipeRepository.countByMember(any()))
                .willReturn(3);

        //when
        chefMemberService.registerChef(1L, new ChefIntroduceForm("소개"));

        //then
        ArgumentCaptor<ChefMember> chefMemberArgumentCaptor = ArgumentCaptor.forClass(ChefMember.class);
        ArgumentCaptor<Member> memberArgumentCaptor = ArgumentCaptor.forClass(Member.class);

        verify(chefMemberRepository, times(1)).save(chefMemberArgumentCaptor.capture());
        verify(memberRepository, times(1)).save(memberArgumentCaptor.capture());

        ChefMember chefMember = chefMemberArgumentCaptor.getValue();
        Member member = memberArgumentCaptor.getValue();

        assertAll(
                () -> assertEquals(1L, member.getId()),
                () -> assertEquals("test@test.com", member.getEmail()),
                () -> assertEquals("nickname", member.getNickname()),
                () -> assertEquals(0, chefMember.getExp()),
                () -> assertEquals(0, chefMember.getStarAvg()),
                () -> assertEquals(0, chefMember.getStarSum()),
                () -> assertEquals(GradeType.BRONZE, chefMember.getGradeType()),
                () -> assertEquals(member, chefMember.getMember())
        );
    }

    @Test
    @DisplayName("요리사 정보 수정 성공")
    void success_updateChefIntroduce(){
        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder()
                        .id(1L)
                        .email("test@test.com")
                        .nickname("nickname")
                                .chefMember(
                                        ChefMember.builder()
                                                .id(1L)
                                                .introduce("수정 전 소개")
                                                .build()
                                )
                        .build())
                );

        //when
        chefMemberService.updateChefIntroduce(
                1L, new ChefIntroduceForm("수정 후 소개")
        );

        //then
        ArgumentCaptor<ChefMember> captor = ArgumentCaptor.forClass(ChefMember.class);
        verify(chefMemberRepository, times(1)).save(captor.capture());

        ChefMember chefMember = captor.getValue();

        assertAll(
                () -> assertEquals(1L, chefMember.getId()),
                () -> assertEquals("수정 후 소개", chefMember.getIntroduce())
        );

    }

    @Test
    @DisplayName("요리사 등록 실패 - 회원을 찾을 수 없음")
    void fail_registerChef_member_not_found(){

        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty()
                );

        //when
        MemberException exception = assertThrows(MemberException.class,
                () -> chefMemberService
                        .registerChef(1L, new ChefIntroduceForm("소개")));

        //then
        assertEquals(MemberErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());

    }

    @Test
    @DisplayName("요리사 등록 실패 - 이미 등록된 요리사")
    void fail_registerChef_already_register_chef(){

        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder()
                        .id(2L)
                        .email("test2@test.com")
                        .nickname("nickname2")
                        .build())
                );

        given(chefMemberRepository.existsByMember(any()))
                .willReturn(true);

        //when
        ChefMemberException exception = assertThrows(ChefMemberException.class,
                () -> chefMemberService
                        .registerChef(1L, new ChefIntroduceForm("소개")));

        //then
        assertEquals(ALREADY_REGISTER_CHEF, exception.getErrorCode());

    }

    @Test
    @DisplayName("요리사 등록 실패 - 3개 미만")
    void fail_registerChef_less_three_recipe(){

        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder()
                        .id(3L)
                        .email("test3@test.com")
                        .nickname("nickname3")
                        .build())
                );

        given(chefMemberRepository.existsByMember(any()))
                .willReturn(false);

        given(recipeRepository.countByMember(any()))
                .willReturn(2);

        //when
        ChefMemberException exception = assertThrows(ChefMemberException.class,
                () -> chefMemberService
                        .registerChef(3L, new ChefIntroduceForm("소개")));

        //then
        assertEquals(CANNOT_REGISTER_LESS_THAN_THREE_RECIPE, exception.getErrorCode());

    }

    @Test
    @DisplayName("요리사 정보 수정 실패 - 회원을 찾을 수 없음")
    void fail_updateChefIntroduce_member_not_found(){
        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(
                        Optional.empty()
                );

        //when
        MemberException exception = assertThrows(MemberException.class,
                () -> chefMemberService
                        .updateChefIntroduce(3L, new ChefIntroduceForm("소개")));

        //then
        assertEquals(MemberErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());

    }

    @Test
    @DisplayName("요리사 정보 수정 실패 - 요리사가 아님")
    void fail_updateChefIntroduce_chef_member_not_found(){
        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder()
                        .id(4L)
                        .email("test4@test.com")
                        .nickname("nickname4")
                        .chefMember(null)
                        .build())
                );
        //when
        ChefMemberException exception = assertThrows(ChefMemberException.class,
                () -> chefMemberService
                        .updateChefIntroduce(4L, new ChefIntroduceForm("소개")));

        //then
        assertEquals(CHEF_MEMBER_NOT_FOUND, exception.getErrorCode());

    }

}