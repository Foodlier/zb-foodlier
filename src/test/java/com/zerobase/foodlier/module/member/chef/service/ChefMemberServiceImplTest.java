package com.zerobase.foodlier.module.member.chef.service;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.chef.dto.*;
import com.zerobase.foodlier.module.member.chef.exception.ChefMemberException;
import com.zerobase.foodlier.module.member.chef.repository.ChefMemberRepository;
import com.zerobase.foodlier.module.member.chef.type.ChefSearchType;
import com.zerobase.foodlier.module.member.chef.type.GradeType;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.domain.vo.Address;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.zerobase.foodlier.module.member.chef.exception.ChefMemberErrorCode.*;
import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
        assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());

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
        assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());

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

    @Test
    @DisplayName("요청된 요리사 목록 가져오기")
    void success_getRequestedChefList(){
        //given

        List<RequestedChefDto> chefList = List.of(
                new RequestedChefDto() {
                    @Override
                    public Long getChefId() {
                        return 1L;
                    }

                    @Override
                    public String getIntroduce() {
                        return "요리사 소개";
                    }

                    @Override
                    public double getStarAvg() {
                        return 3.0;
                    }

                    @Override
                    public int getReviewCount() {
                        return 3;
                    }

                    @Override
                    public String getProfileUrl() {
                        return "https://s3.com/test.png";
                    }

                    @Override
                    public String getNickname() {
                        return "nickname";
                    }

                    @Override
                    public double getLat() {
                        return 37.1;
                    }

                    @Override
                    public double getLnt() {
                        return 127.1;
                    }

                    @Override
                    public double getDistance() {
                        return 1.12;
                    }

                    @Override
                    public int getRecipeCount() {
                        return 2;
                    }

                    @Override
                    public Long getRequestId() {
                        return 1L;
                    }

                    @Override
                    public int getIsQuotation() {
                        return 0;
                    }

                    @Override
                    public Long getQuotationId() {
                        return 1L;
                    }
                }
        );

        given(chefMemberRepository.findRequestedChef(anyLong(), any()))
                .willReturn(
                        new PageImpl<>(
                                new ArrayList<>(
                                        chefList
                                )
                        )
                );
        //when
        ListResponse<RequestedChefDto> responseChefList = chefMemberService
                .getRequestedChefList(1L, PageRequest.of(0, 10));

        //then
        assertAll(
                () -> assertEquals(chefList.get(0).getChefId(),
                        responseChefList.getContent().get(0).getChefId()),
                () -> assertEquals(chefList.get(0).getIntroduce(),
                        responseChefList.getContent().get(0).getIntroduce()),
                () -> assertEquals(chefList.get(0).getStarAvg(),
                        responseChefList.getContent().get(0).getStarAvg()),
                () -> assertEquals(chefList.get(0).getReviewCount(),
                        responseChefList.getContent().get(0).getReviewCount()),
                () -> assertEquals(chefList.get(0).getProfileUrl(),
                        responseChefList.getContent().get(0).getProfileUrl()),
                () -> assertEquals(chefList.get(0).getNickname(),
                        responseChefList.getContent().get(0).getNickname()),
                () -> assertEquals(chefList.get(0).getDistance(),
                        responseChefList.getContent().get(0).getDistance()),
                () -> assertEquals(chefList.get(0).getLat(),
                        responseChefList.getContent().get(0).getLat()),
                () -> assertEquals(chefList.get(0).getLnt(),
                        responseChefList.getContent().get(0).getLnt()),
                () -> assertEquals(chefList.get(0).getRecipeCount(),
                        responseChefList.getContent().get(0).getRecipeCount()),
                () -> assertEquals(chefList.get(0).getRequestId(),
                        responseChefList.getContent().get(0).getRequestId()),
                () -> assertEquals(chefList.get(0).getIsQuotation(),
                        responseChefList.getContent().get(0).getIsQuotation()),
                () -> assertEquals(chefList.get(0).getQuotationId(),
                        responseChefList.getContent().get(0).getQuotationId())
        );

    }

    @Test
    @DisplayName("주변 요리사 조회하기 - 거리순")
    void success_getAroundChefList_by_distance(){
        //given
        AroundChefDto chef1 = getChef1();
        AroundChefDto chef2 = getChef2();

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder()
                        .id(1L)
                        .address(
                                Address.builder()
                                        .lat(37.1)
                                        .lnt(127.1)
                                        .build()
                        )
                        .build()
                ));

        given(chefMemberRepository.findAroundChefOrderByDistance(
                anyLong(), anyDouble(), anyDouble(), anyDouble(), any()
        )).willReturn(
                new PageImpl<>(
                        new ArrayList<>(
                                Arrays.asList(
                                        chef2, chef1
                                )
                        )
                )
        );

        //when
        ListResponse<AroundChefDto> response = chefMemberService
                .getAroundChefList(1L, PageRequest.of(0, 10),
                        ChefSearchType.DISTANCE);

        //then
        assertAll(
                () -> assertEquals(chef2.getChefId(), response.getContent().get(0).getChefId()),
                () -> assertEquals(chef2.getIntroduce(), response.getContent().get(0).getIntroduce()),
                () -> assertEquals(chef2.getStarAvg(), response.getContent().get(0).getStarAvg()),
                () -> assertEquals(chef2.getReviewCount(), response.getContent().get(0).getReviewCount()),
                () -> assertEquals(chef2.getProfileUrl(), response.getContent().get(0).getProfileUrl()),
                () -> assertEquals(chef2.getDistance(), response.getContent().get(0).getDistance()),
                () -> assertEquals(chef2.getLat(), response.getContent().get(0).getLat()),
                () -> assertEquals(chef2.getLnt(), response.getContent().get(0).getLnt()),
                () -> assertEquals(chef2.getNickname(), response.getContent().get(0).getNickname()),
                () -> assertEquals(chef2.getRecipeCount(), response.getContent().get(0).getRecipeCount()),

                () -> assertEquals(chef1.getChefId(), response.getContent().get(1).getChefId()),
                () -> assertEquals(chef1.getIntroduce(), response.getContent().get(1).getIntroduce()),
                () -> assertEquals(chef1.getStarAvg(), response.getContent().get(1).getStarAvg()),
                () -> assertEquals(chef1.getReviewCount(), response.getContent().get(1).getReviewCount()),
                () -> assertEquals(chef1.getProfileUrl(), response.getContent().get(1).getProfileUrl()),
                () -> assertEquals(chef1.getDistance(), response.getContent().get(1).getDistance()),
                () -> assertEquals(chef1.getLat(), response.getContent().get(1).getLat()),
                () -> assertEquals(chef1.getLnt(), response.getContent().get(1).getLnt()),
                () -> assertEquals(chef1.getNickname(), response.getContent().get(1).getNickname()),
                () -> assertEquals(chef1.getRecipeCount(), response.getContent().get(1).getRecipeCount())
        );
    }

    @Test
    @DisplayName("주변 요리사 조회하기 - 별점순")
    void success_getAroundChefList_by_star(){
        //given
        AroundChefDto chef1 = getChef1();
        AroundChefDto chef2 = getChef2();

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder()
                        .id(1L)
                        .address(
                                Address.builder()
                                        .lat(37.1)
                                        .lnt(127.1)
                                        .build()
                        )
                        .build()
                ));

        given(chefMemberRepository.findAroundChefOrderByStar(
                anyLong(), anyDouble(), anyDouble(), anyDouble(), any()
        )).willReturn(
                new PageImpl<>(
                        new ArrayList<>(
                                Arrays.asList(
                                        chef1, chef2
                                )
                        )
                )
        );

        //when
        ListResponse<AroundChefDto> response = chefMemberService
                .getAroundChefList(1L, PageRequest.of(0, 10),
                        ChefSearchType.STAR);

        //then
        assertAll(
                () -> assertEquals(chef1.getChefId(), response.getContent().get(0).getChefId()),
                () -> assertEquals(chef1.getIntroduce(), response.getContent().get(0).getIntroduce()),
                () -> assertEquals(chef1.getStarAvg(), response.getContent().get(0).getStarAvg()),
                () -> assertEquals(chef1.getReviewCount(), response.getContent().get(0).getReviewCount()),
                () -> assertEquals(chef1.getProfileUrl(), response.getContent().get(0).getProfileUrl()),
                () -> assertEquals(chef1.getDistance(), response.getContent().get(0).getDistance()),
                () -> assertEquals(chef1.getLat(), response.getContent().get(0).getLat()),
                () -> assertEquals(chef1.getLnt(), response.getContent().get(0).getLnt()),
                () -> assertEquals(chef1.getNickname(), response.getContent().get(0).getNickname()),
                () -> assertEquals(chef1.getRecipeCount(), response.getContent().get(0).getRecipeCount()),

                () -> assertEquals(chef2.getChefId(), response.getContent().get(1).getChefId()),
                () -> assertEquals(chef2.getIntroduce(), response.getContent().get(1).getIntroduce()),
                () -> assertEquals(chef2.getStarAvg(), response.getContent().get(1).getStarAvg()),
                () -> assertEquals(chef2.getReviewCount(), response.getContent().get(1).getReviewCount()),
                () -> assertEquals(chef2.getProfileUrl(), response.getContent().get(1).getProfileUrl()),
                () -> assertEquals(chef2.getDistance(), response.getContent().get(1).getDistance()),
                () -> assertEquals(chef2.getLat(), response.getContent().get(1).getLat()),
                () -> assertEquals(chef2.getLnt(), response.getContent().get(1).getLnt()),
                () -> assertEquals(chef2.getNickname(), response.getContent().get(1).getNickname()),
                () -> assertEquals(chef2.getRecipeCount(), response.getContent().get(1).getRecipeCount())
        );
    }

    @Test
    @DisplayName("주변 요리사 조회하기 - 리뷰순")
    void success_getAroundChefList_by_review(){
        //given
        AroundChefDto chef1 = getChef1();
        AroundChefDto chef2 = getChef2();

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder()
                        .id(1L)
                        .address(
                                Address.builder()
                                        .lat(37.1)
                                        .lnt(127.1)
                                        .build()
                        )
                        .build()
                ));

        given(chefMemberRepository.findAroundChefOrderByReview(
                anyLong(), anyDouble(), anyDouble(), anyDouble(), any()
        )).willReturn(
                new PageImpl<>(
                        new ArrayList<>(
                                Arrays.asList(
                                        chef1, chef2
                                )
                        )
                )
        );

        //when
        ListResponse<AroundChefDto> response = chefMemberService
                .getAroundChefList(1L, PageRequest.of(0, 10),
                        ChefSearchType.REVIEW);

        //then
        assertAll(
                () -> assertEquals(chef1.getChefId(), response.getContent().get(0).getChefId()),
                () -> assertEquals(chef1.getIntroduce(), response.getContent().get(0).getIntroduce()),
                () -> assertEquals(chef1.getStarAvg(), response.getContent().get(0).getStarAvg()),
                () -> assertEquals(chef1.getReviewCount(), response.getContent().get(0).getReviewCount()),
                () -> assertEquals(chef1.getProfileUrl(), response.getContent().get(0).getProfileUrl()),
                () -> assertEquals(chef1.getDistance(), response.getContent().get(0).getDistance()),
                () -> assertEquals(chef1.getLat(), response.getContent().get(0).getLat()),
                () -> assertEquals(chef1.getLnt(), response.getContent().get(0).getLnt()),
                () -> assertEquals(chef1.getNickname(), response.getContent().get(0).getNickname()),
                () -> assertEquals(chef1.getRecipeCount(), response.getContent().get(0).getRecipeCount()),

                () -> assertEquals(chef2.getChefId(), response.getContent().get(1).getChefId()),
                () -> assertEquals(chef2.getIntroduce(), response.getContent().get(1).getIntroduce()),
                () -> assertEquals(chef2.getStarAvg(), response.getContent().get(1).getStarAvg()),
                () -> assertEquals(chef2.getReviewCount(), response.getContent().get(1).getReviewCount()),
                () -> assertEquals(chef2.getProfileUrl(), response.getContent().get(1).getProfileUrl()),
                () -> assertEquals(chef2.getDistance(), response.getContent().get(1).getDistance()),
                () -> assertEquals(chef2.getLat(), response.getContent().get(1).getLat()),
                () -> assertEquals(chef2.getLnt(), response.getContent().get(1).getLnt()),
                () -> assertEquals(chef2.getNickname(), response.getContent().get(1).getNickname()),
                () -> assertEquals(chef2.getRecipeCount(), response.getContent().get(1).getRecipeCount())
        );
    }

    @Test
    @DisplayName("주변 요리사 조회하기 - 레시피 많은순")
    void success_getAroundChefList_by_recipe(){
        //given
        AroundChefDto chef1 = getChef1();
        AroundChefDto chef2 = getChef2();

        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(Member.builder()
                        .id(1L)
                        .address(
                                Address.builder()
                                        .lat(37.1)
                                        .lnt(127.1)
                                        .build()
                        )
                        .build()
                ));

        given(chefMemberRepository.findAroundChefOrderByRecipeCount(
                anyLong(), anyDouble(), anyDouble(), anyDouble(), any()
        )).willReturn(
                new PageImpl<>(
                        new ArrayList<>(
                                Arrays.asList(
                                        chef2, chef1
                                )
                        )
                )
        );

        //when
        ListResponse<AroundChefDto> response = chefMemberService
                .getAroundChefList(1L, PageRequest.of(0, 10),
                        ChefSearchType.RECIPE);

        //then
        assertAll(
                () -> assertEquals(chef2.getChefId(), response.getContent().get(0).getChefId()),
                () -> assertEquals(chef2.getIntroduce(), response.getContent().get(0).getIntroduce()),
                () -> assertEquals(chef2.getStarAvg(), response.getContent().get(0).getStarAvg()),
                () -> assertEquals(chef2.getReviewCount(), response.getContent().get(0).getReviewCount()),
                () -> assertEquals(chef2.getProfileUrl(), response.getContent().get(0).getProfileUrl()),
                () -> assertEquals(chef2.getDistance(), response.getContent().get(0).getDistance()),
                () -> assertEquals(chef2.getLat(), response.getContent().get(0).getLat()),
                () -> assertEquals(chef2.getLnt(), response.getContent().get(0).getLnt()),
                () -> assertEquals(chef2.getNickname(), response.getContent().get(0).getNickname()),
                () -> assertEquals(chef2.getRecipeCount(), response.getContent().get(0).getRecipeCount()),

                () -> assertEquals(chef1.getChefId(), response.getContent().get(1).getChefId()),
                () -> assertEquals(chef1.getIntroduce(), response.getContent().get(1).getIntroduce()),
                () -> assertEquals(chef1.getStarAvg(), response.getContent().get(1).getStarAvg()),
                () -> assertEquals(chef1.getReviewCount(), response.getContent().get(1).getReviewCount()),
                () -> assertEquals(chef1.getProfileUrl(), response.getContent().get(1).getProfileUrl()),
                () -> assertEquals(chef1.getDistance(), response.getContent().get(1).getDistance()),
                () -> assertEquals(chef1.getLat(), response.getContent().get(1).getLat()),
                () -> assertEquals(chef1.getLnt(), response.getContent().get(1).getLnt()),
                () -> assertEquals(chef1.getNickname(), response.getContent().get(1).getNickname()),
                () -> assertEquals(chef1.getRecipeCount(), response.getContent().get(1).getRecipeCount())
        );
    }

    @Test
    @DisplayName("주변 요리사 조회하기 실패 - 회원 X")
    void fail_success_getAroundChefList_member_not_found(){
        //given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        MemberException exception = assertThrows(MemberException.class,
                () -> chefMemberService.getAroundChefList(1L,
                        PageRequest.of(0, 10), ChefSearchType.DISTANCE));

        //then
        assertEquals(MEMBER_NOT_FOUND, exception.getErrorCode());
    }

    private AroundChefDto getChef1(){
        return new AroundChefDto() {
            @Override
            public Long getChefId() {
                return 1L;
            }

            @Override
            public String getIntroduce() {
                return "요리사1 소개";
            }

            @Override
            public double getStarAvg() {
                return 5.0;
            }

            @Override
            public int getReviewCount() {
                return 5;
            }

            @Override
            public String getProfileUrl() {
                return "https://s3.test.com/image1.png";
            }

            @Override
            public String getNickname() {
                return "chef1";
            }

            @Override
            public double getLat() {
                return 37.1;
            }

            @Override
            public double getLnt() {
                return 127.1;
            }

            @Override
            public double getDistance() {
                return 1.1;
            }

            @Override
            public int getRecipeCount() {
                return 2;
            }
        };
    }

    private AroundChefDto getChef2(){
        return new AroundChefDto() {
            @Override
            public Long getChefId() {
                return 2L;
            }

            @Override
            public String getIntroduce() {
                return "요리사2 소개";
            }

            @Override
            public double getStarAvg() {
                return 4.0;
            }

            @Override
            public int getReviewCount() {
                return 4;
            }

            @Override
            public String getProfileUrl() {
                return "https://s3.test.com/image2.png";
            }

            @Override
            public String getNickname() {
                return "chef2";
            }

            @Override
            public double getLat() {
                return 37.2;
            }

            @Override
            public double getLnt() {
                return 127.2;
            }

            @Override
            public double getDistance() {
                return 0.5;
            }

            @Override
            public int getRecipeCount() {
                return 7;
            }
        };
    }

    @Test
    @DisplayName("요리사의 경험치를 올림 성공")
    void success_plusExp(){
        //given
        given(chefMemberRepository.findById(anyLong()))
                .willReturn(Optional.of(ChefMember.builder()
                        .id(1L)
                        .exp(0)
                        .build()));

        //when
        chefMemberService.plusExp(1L, 5);

        //then
        ArgumentCaptor<ChefMember> captor = ArgumentCaptor.forClass(ChefMember.class);
        verify(chefMemberRepository, times(1)).save(captor.capture());

        assertEquals(500, captor.getValue().getExp());
    }

    @Test
    @DisplayName("요리사의 경험치를 올림 실패 - 요리사 X")
    void fail_plusExp_chef_member_not_found(){
        //given
        given(chefMemberRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        ChefMemberException exception = assertThrows(ChefMemberException.class,
                () -> chefMemberService.plusExp(1L, 5));
        //then
        assertEquals(CHEF_MEMBER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("요리사의 별점 추가 성공")
    void success_plusStar(){
        //given
        given(chefMemberRepository.findById(anyLong()))
                .willReturn(Optional.of(ChefMember.builder()
                        .id(1L)
                        .starSum(0)
                        .starAvg(0)
                        .reviewCount(0)
                        .build()));

        //when
        chefMemberService.plusStar(1L, 5);

        //then
        ArgumentCaptor<ChefMember> captor = ArgumentCaptor.forClass(ChefMember.class);
        verify(chefMemberRepository, times(1)).save(captor.capture());

        assertAll(
                () -> assertEquals(5, captor.getValue().getStarAvg()),
                () -> assertEquals(5, captor.getValue().getStarSum()),
                () -> assertEquals(1, captor.getValue().getReviewCount())
        );

    }

    @Test
    @DisplayName("요리사의 별점 추가 실패 - 요리사 X")
    void fail_plusStar_chef_member_not_found(){
        //given
        given(chefMemberRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        ChefMemberException exception = assertThrows(ChefMemberException.class,
                () -> chefMemberService.plusStar(1L, 5));
        //then
        assertEquals(CHEF_MEMBER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("TOP 요리사 5명 조회 성공")
    void success_getTopChefList(){
        //given

        Member member1 = Member.builder()
                .id(1L)
                .nickname("a")
                .profileUrl("http://s3.test.com/1.png")
                .build();
        Member member2 = Member.builder()
                .id(2L)
                .nickname("b")
                .profileUrl("http://s3.test.com/2.png")
                .build();
        Member member3 = Member.builder()
                .id(3L)
                .nickname("c")
                .profileUrl("http://s3.test.com/3.png")
                .build();
        Member member4 = Member.builder()
                .id(4L)
                .nickname("d")
                .profileUrl("http://s3.test.com/4.png")
                .build();
        Member member5 = Member.builder()
                .id(1L)
                .nickname("e")
                .profileUrl("http://s3.test.com/5.png")
                .build();


        ChefMember chef1 = ChefMember.builder()
                .id(1L)
                .exp(100)
                .member(member1)
                .gradeType(GradeType.BRONZE)
                .build();
        ChefMember chef2 = ChefMember.builder()
                .id(2L)
                .exp(200)
                .member(member2)
                .gradeType(GradeType.BRONZE)
                .build();
        ChefMember chef3 = ChefMember.builder()
                .id(3L)
                .exp(250)
                .member(member3)
                .gradeType(GradeType.SILVER)
                .build();
        ChefMember chef4 = ChefMember.builder()
                .id(4L)
                .exp(600)
                .member(member4)
                .gradeType(GradeType.GOLD)
                .build();
        ChefMember chef5 = ChefMember.builder()
                .id(5L)
                .exp(950)
                .member(member5)
                .gradeType(GradeType.PLATINUM)
                .build();

        given(chefMemberRepository.findTop5ByOrderByExpDesc())
                .willReturn(
                        List.of(
                                chef5, chef4, chef3, chef2, chef1
                        )
                );

        //when
        List<TopChefDto> topChefDtoList = chefMemberService.getTopChefList();

        //then

        assertAll(
                () -> assertEquals(member5.getId(), topChefDtoList.get(0).getMemberId()),
                () -> assertEquals(chef5.getId(), topChefDtoList.get(0).getChefMemberId()),
                () -> assertEquals(member5.getNickname(), topChefDtoList.get(0).getNickname()),
                () -> assertEquals(member5.getProfileUrl(), topChefDtoList.get(0).getProfileUrl()),

                () -> assertEquals(member4.getId(), topChefDtoList.get(1).getMemberId()),
                () -> assertEquals(chef4.getId(), topChefDtoList.get(1).getChefMemberId()),
                () -> assertEquals(member4.getNickname(), topChefDtoList.get(1).getNickname()),
                () -> assertEquals(member4.getProfileUrl(), topChefDtoList.get(1).getProfileUrl()),

                () -> assertEquals(member3.getId(), topChefDtoList.get(2).getMemberId()),
                () -> assertEquals(chef3.getId(), topChefDtoList.get(2).getChefMemberId()),
                () -> assertEquals(member3.getNickname(), topChefDtoList.get(2).getNickname()),
                () -> assertEquals(member3.getProfileUrl(), topChefDtoList.get(2).getProfileUrl()),

                () -> assertEquals(member2.getId(), topChefDtoList.get(3).getMemberId()),
                () -> assertEquals(chef2.getId(), topChefDtoList.get(3).getChefMemberId()),
                () -> assertEquals(member2.getNickname(), topChefDtoList.get(3).getNickname()),
                () -> assertEquals(member2.getProfileUrl(), topChefDtoList.get(3).getProfileUrl()),

                () -> assertEquals(member1.getId(), topChefDtoList.get(4).getMemberId()),
                () -> assertEquals(chef1.getId(), topChefDtoList.get(4).getChefMemberId()),
                () -> assertEquals(member1.getNickname(), topChefDtoList.get(4).getNickname()),
                () -> assertEquals(member1.getProfileUrl(), topChefDtoList.get(4).getProfileUrl())
        );

    }

    @Test
    @DisplayName("요리사 프로필 조회 성공")
    void success_getChefProfile(){

        //given
        ChefMember chefMember = ChefMember.builder()
                .id(1L)
                .exp(100)
                .gradeType(GradeType.BRONZE)
                .build();

        given(chefMemberRepository.findById(anyLong()))
                .willReturn(Optional.of(chefMember));

        //when
        ChefProfileDto chefProfileDto = chefMemberService.getChefProfile(1L);

        //then
        assertAll(
                () -> assertEquals(chefMember.getGradeType(), chefProfileDto.getGrade()),
                () -> assertEquals(chefMember.getExp(), chefProfileDto.getExp())
        );
    }

    @Test
    @DisplayName("요리사 프로필 조회 실패 - 요리사 X")
    void fail_getChefProfile_chef_member_not_found(){
        //given
        given(chefMemberRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        ChefMemberException exception = assertThrows(ChefMemberException.class,
                () -> chefMemberService.getChefProfile(1L));

        //then
        assertEquals(CHEF_MEMBER_NOT_FOUND, exception.getErrorCode());
    }


}