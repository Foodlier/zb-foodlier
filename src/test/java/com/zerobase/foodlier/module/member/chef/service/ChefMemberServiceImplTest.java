package com.zerobase.foodlier.module.member.chef.service;

import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.chef.dto.AroundChefDto;
import com.zerobase.foodlier.module.member.chef.dto.ChefIntroduceForm;
import com.zerobase.foodlier.module.member.chef.dto.RequestedChefDto;
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

        given(chefMemberRepository.findRequestedChef(anyLong(), anyInt(), anyInt()))
                .willReturn(
                    chefList
                );
        //when
        List<RequestedChefDto> responseChefList = chefMemberService
                .getRequestedChefList(1L, 0, 10);

        //then
        assertAll(
                () -> assertEquals(chefList.get(0).getChefId(), responseChefList.get(0).getChefId()),
                () -> assertEquals(chefList.get(0).getIntroduce(), responseChefList.get(0).getIntroduce()),
                () -> assertEquals(chefList.get(0).getStarAvg(), responseChefList.get(0).getStarAvg()),
                () -> assertEquals(chefList.get(0).getReviewCount(), responseChefList.get(0).getReviewCount()),
                () -> assertEquals(chefList.get(0).getProfileUrl(), responseChefList.get(0).getProfileUrl()),
                () -> assertEquals(chefList.get(0).getNickname(), responseChefList.get(0).getNickname()),
                () -> assertEquals(chefList.get(0).getDistance(), responseChefList.get(0).getDistance()),
                () -> assertEquals(chefList.get(0).getRecipeCount(), responseChefList.get(0).getRecipeCount()),
                () -> assertEquals(chefList.get(0).getRequestId(), responseChefList.get(0).getRequestId()),
                () -> assertEquals(chefList.get(0).getIsQuotation(), responseChefList.get(0).getIsQuotation()),
                () -> assertEquals(chefList.get(0).getQuotationId(), responseChefList.get(0).getQuotationId())
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
                anyLong(), anyDouble(), anyDouble(), anyDouble(), anyInt(), anyInt()
        )).willReturn(
                List.of(
                        chef2, chef1
                )
        );

        //when
        List<AroundChefDto> response = chefMemberService
                .getAroundChefList(1L, 0, 10,
                        ChefSearchType.DISTANCE);

        //then
        assertAll(
                () -> assertEquals(chef2.getChefId(), response.get(0).getChefId()),
                () -> assertEquals(chef2.getIntroduce(), response.get(0).getIntroduce()),
                () -> assertEquals(chef2.getStarAvg(), response.get(0).getStarAvg()),
                () -> assertEquals(chef2.getReviewCount(), response.get(0).getReviewCount()),
                () -> assertEquals(chef2.getProfileUrl(), response.get(0).getProfileUrl()),
                () -> assertEquals(chef2.getDistance(), response.get(0).getDistance()),
                () -> assertEquals(chef2.getNickname(), response.get(0).getNickname()),
                () -> assertEquals(chef2.getRecipeCount(), response.get(0).getRecipeCount()),

                () -> assertEquals(chef1.getChefId(), response.get(1).getChefId()),
                () -> assertEquals(chef1.getIntroduce(), response.get(1).getIntroduce()),
                () -> assertEquals(chef1.getStarAvg(), response.get(1).getStarAvg()),
                () -> assertEquals(chef1.getReviewCount(), response.get(1).getReviewCount()),
                () -> assertEquals(chef1.getProfileUrl(), response.get(1).getProfileUrl()),
                () -> assertEquals(chef1.getDistance(), response.get(1).getDistance()),
                () -> assertEquals(chef1.getNickname(), response.get(1).getNickname()),
                () -> assertEquals(chef1.getRecipeCount(), response.get(1).getRecipeCount())
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
                anyLong(), anyDouble(), anyDouble(), anyDouble(), anyInt(), anyInt()
        )).willReturn(
                List.of(
                        chef1, chef2
                )
        );

        //when
        List<AroundChefDto> response = chefMemberService
                .getAroundChefList(1L, 0, 10,
                        ChefSearchType.STAR);

        //then
        assertAll(
                () -> assertEquals(chef1.getChefId(), response.get(0).getChefId()),
                () -> assertEquals(chef1.getIntroduce(), response.get(0).getIntroduce()),
                () -> assertEquals(chef1.getStarAvg(), response.get(0).getStarAvg()),
                () -> assertEquals(chef1.getReviewCount(), response.get(0).getReviewCount()),
                () -> assertEquals(chef1.getProfileUrl(), response.get(0).getProfileUrl()),
                () -> assertEquals(chef1.getDistance(), response.get(0).getDistance()),
                () -> assertEquals(chef1.getNickname(), response.get(0).getNickname()),
                () -> assertEquals(chef1.getRecipeCount(), response.get(0).getRecipeCount()),

                () -> assertEquals(chef2.getChefId(), response.get(1).getChefId()),
                () -> assertEquals(chef2.getIntroduce(), response.get(1).getIntroduce()),
                () -> assertEquals(chef2.getStarAvg(), response.get(1).getStarAvg()),
                () -> assertEquals(chef2.getReviewCount(), response.get(1).getReviewCount()),
                () -> assertEquals(chef2.getProfileUrl(), response.get(1).getProfileUrl()),
                () -> assertEquals(chef2.getDistance(), response.get(1).getDistance()),
                () -> assertEquals(chef2.getNickname(), response.get(1).getNickname()),
                () -> assertEquals(chef2.getRecipeCount(), response.get(1).getRecipeCount())
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
                anyLong(), anyDouble(), anyDouble(), anyDouble(), anyInt(), anyInt()
        )).willReturn(
                List.of(
                        chef1, chef2
                )
        );

        //when
        List<AroundChefDto> response = chefMemberService
                .getAroundChefList(1L, 0, 10,
                        ChefSearchType.REVIEW);

        //then
        assertAll(
                () -> assertEquals(chef1.getChefId(), response.get(0).getChefId()),
                () -> assertEquals(chef1.getIntroduce(), response.get(0).getIntroduce()),
                () -> assertEquals(chef1.getStarAvg(), response.get(0).getStarAvg()),
                () -> assertEquals(chef1.getReviewCount(), response.get(0).getReviewCount()),
                () -> assertEquals(chef1.getProfileUrl(), response.get(0).getProfileUrl()),
                () -> assertEquals(chef1.getDistance(), response.get(0).getDistance()),
                () -> assertEquals(chef1.getNickname(), response.get(0).getNickname()),
                () -> assertEquals(chef1.getRecipeCount(), response.get(0).getRecipeCount()),

                () -> assertEquals(chef2.getChefId(), response.get(1).getChefId()),
                () -> assertEquals(chef2.getIntroduce(), response.get(1).getIntroduce()),
                () -> assertEquals(chef2.getStarAvg(), response.get(1).getStarAvg()),
                () -> assertEquals(chef2.getReviewCount(), response.get(1).getReviewCount()),
                () -> assertEquals(chef2.getProfileUrl(), response.get(1).getProfileUrl()),
                () -> assertEquals(chef2.getDistance(), response.get(1).getDistance()),
                () -> assertEquals(chef2.getNickname(), response.get(1).getNickname()),
                () -> assertEquals(chef2.getRecipeCount(), response.get(1).getRecipeCount())
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
                anyLong(), anyDouble(), anyDouble(), anyDouble(), anyInt(), anyInt()
        )).willReturn(
                List.of(
                        chef2, chef1
                )
        );

        //when
        List<AroundChefDto> response = chefMemberService
                .getAroundChefList(1L, 0, 10,
                        ChefSearchType.RECIPE);

        //then
        assertAll(
                () -> assertEquals(chef2.getChefId(), response.get(0).getChefId()),
                () -> assertEquals(chef2.getIntroduce(), response.get(0).getIntroduce()),
                () -> assertEquals(chef2.getStarAvg(), response.get(0).getStarAvg()),
                () -> assertEquals(chef2.getReviewCount(), response.get(0).getReviewCount()),
                () -> assertEquals(chef2.getProfileUrl(), response.get(0).getProfileUrl()),
                () -> assertEquals(chef2.getDistance(), response.get(0).getDistance()),
                () -> assertEquals(chef2.getNickname(), response.get(0).getNickname()),
                () -> assertEquals(chef2.getRecipeCount(), response.get(0).getRecipeCount()),

                () -> assertEquals(chef1.getChefId(), response.get(1).getChefId()),
                () -> assertEquals(chef1.getIntroduce(), response.get(1).getIntroduce()),
                () -> assertEquals(chef1.getStarAvg(), response.get(1).getStarAvg()),
                () -> assertEquals(chef1.getReviewCount(), response.get(1).getReviewCount()),
                () -> assertEquals(chef1.getProfileUrl(), response.get(1).getProfileUrl()),
                () -> assertEquals(chef1.getDistance(), response.get(1).getDistance()),
                () -> assertEquals(chef1.getNickname(), response.get(1).getNickname()),
                () -> assertEquals(chef1.getRecipeCount(), response.get(1).getRecipeCount())
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
                        0, 10, ChefSearchType.DISTANCE));

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
            public double getDistance() {
                return 0.5;
            }

            @Override
            public int getRecipeCount() {
                return 7;
            }
        };
    }




}