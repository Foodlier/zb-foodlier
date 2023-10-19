package com.zerobase.foodlier.global.profile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.config.SecurityConfig;
import com.zerobase.foodlier.global.profile.facade.ProfileFacade;
import com.zerobase.foodlier.mockuser.WithCustomMockUser;
import com.zerobase.foodlier.module.comment.comment.dto.MyPageCommentDto;
import com.zerobase.foodlier.module.comment.comment.service.CommentService;
import com.zerobase.foodlier.module.member.chef.dto.ChefIntroduceForm;
import com.zerobase.foodlier.module.member.chef.dto.ChefProfileDto;
import com.zerobase.foodlier.module.member.chef.dto.TopChefDto;
import com.zerobase.foodlier.module.member.chef.service.ChefMemberService;
import com.zerobase.foodlier.module.member.chef.type.GradeType;
import com.zerobase.foodlier.module.member.member.domain.vo.Address;
import com.zerobase.foodlier.module.member.member.dto.DefaultProfileDtoResponse;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberPrivateProfileForm;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberPrivateProfileResponse;
import com.zerobase.foodlier.module.member.member.profile.dto.PasswordChangeForm;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import com.zerobase.foodlier.module.recipe.dto.recipe.RecipeDtoTopResponse;
import com.zerobase.foodlier.module.recipe.service.recipe.RecipeService;
import com.zerobase.foodlier.module.review.chef.dto.ChefReviewResponseDto;
import com.zerobase.foodlier.module.review.chef.service.ChefReviewService;
import com.zerobase.foodlier.module.review.recipe.dto.RecipeReviewResponseDto;
import com.zerobase.foodlier.module.review.recipe.service.RecipeReviewService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = ProfileController.class, excludeFilters =
@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class}))
class ProfileControllerTest {

    @MockBean
    private MemberService memberService;
    @MockBean
    private ProfileFacade profileFacade;
    @MockBean
    private ChefMemberService chefMemberService;
    @MockBean
    private ChefReviewService chefReviewService;
    @MockBean
    private RecipeReviewService recipeReviewService;
    @MockBean
    private RecipeService recipeService;
    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithCustomMockUser
    @DisplayName("마이프로필 정보 가져오기 성공")
    void success_getPrivateProfile() throws Exception {

        //given
        MemberPrivateProfileResponse response = MemberPrivateProfileResponse
                .builder()
                .nickName("유저1")
                .email("test@test.com")
                .phoneNumber("01011112222")
                .point(10000L)
                .isChef(true)
                .address(Address.builder()
                        .lat(37.1)
                        .lnt(127.1)
                        .roadAddress("제주특별차치도 서울시 제로베이스길 25")
                        .addressDetail("지하 5층")
                        .build())
                .build();

        given(memberService.getPrivateProfile(anyString()))
                .willReturn(response);

        //when
        ResultActions perform = mockMvc.perform(get("/profile/private"));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.nickName").value(response.getNickName()),
                        jsonPath("$.email").value(response.getEmail()),
                        jsonPath("$.phoneNumber").value(response.getPhoneNumber()),
                        jsonPath("$.point").value(response.getPoint()),
                        jsonPath("$.isChef").value(response.getIsChef()),
                        jsonPath("$.address.lat").value(response.getAddress().getLat()),
                        jsonPath("$.address.lnt").value(response.getAddress().getLnt()),
                        jsonPath("$.address.roadAddress").value(response.getAddress().getRoadAddress()),
                        jsonPath("$.address.addressDetail").value(response.getAddress().getAddressDetail())
                );

    }

    @Test
    @WithCustomMockUser
    @DisplayName("좋아요를 누른 레시피 목록 가져오기")
    void success_getRecipeForHeart() throws Exception {

        //given
        RecipeDtoTopResponse response = RecipeDtoTopResponse
                .builder()
                .recipeId(1L)
                .mainImageUrl("https://s3.test.com/main.png")
                .title("제로베이스 왕갈비 치킨")
                .content("이건 치킨인가? 갈비인가?")
                .heartCount(100000)
                .isHeart(true)
                .build();

        given(recipeService.getRecipeForHeart(anyLong(), any()))
                .willReturn(ListResponse.from(
                        new PageImpl<>(
                                List.of(
                                        response
                                )
                        )
                ));

        //when
        ResultActions perform = mockMvc.perform(get("/profile/private/heart/0/1"));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content.[0].recipeId").value(response.getRecipeId()),
                        jsonPath("$.content.[0].mainImageUrl").value(response.getMainImageUrl()),
                        jsonPath("$.content.[0].title").value(response.getTitle()),
                        jsonPath("$.content.[0].content").value(response.getContent()),
                        jsonPath("$.content.[0].heartCount").value(response.getHeartCount()),
                        jsonPath("$.content.[0].isHeart").value(response.getIsHeart())
                );

    }

    @Test
    @WithCustomMockUser
    @DisplayName("내가 작성한 댓글 및 대댓글 가져오기")
    void success_getMyCommentList() throws Exception {

        //given
        MyPageCommentDto dto = new MyPageCommentDto() {
            public Long getRecipeId() {
                return 1L;
            }
            public String getMessage() {
                return "이거이거 너무 맛있는 꿀조합인데요?";
            }
            public LocalDateTime getCreatedAt() {
                return LocalDateTime.of(2023, 10, 18,
                        13, 30, 50);
            }
        };
        given(commentService.getMyCommentList(anyLong(), any()))
                .willReturn(ListResponse.from(
                        new PageImpl<>(
                                List.of(
                                        dto
                                )
                        )
                ));

        //when
        ResultActions perform = mockMvc.perform(get("/profile/private/comment/0/10"));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content.[0].recipeId").value(dto.getRecipeId()),
                        jsonPath("$.content.[0].message").value(dto.getMessage()),
                        jsonPath("$.content.[0].createdAt").value("2023-10-18 13:30:50")
                );

    }

    @Test
    @WithCustomMockUser
    @DisplayName("회원 정보 수정")
    void success_updatePrivateProfile() throws Exception {

        //when
        String profile = "foodlier_logo.png";
        File file = new File("src/test/resources/foodlier_logo.png");

        MemberPrivateProfileForm form = MemberPrivateProfileForm
                .builder()
                .nickName("수정된 닉네임")
                .phoneNumber("01012123434")
                .roadAddress("경기도 수원시 제로베이스로 1000")
                .addressDetail("건물 1층")
                .profileImage(new MockMultipartFile(profile,
                        profile, MediaType.IMAGE_PNG_VALUE, new FileInputStream(file)))
                .build();

        mockMvc.perform(multipart("/profile/private")
                .file("profileImage", form.getProfileImage().getBytes())
                .param("nickName", form.getNickName())
                .param("phoneNumber", String.valueOf(form.getPhoneNumber()))
                .param("roadAddress", String.valueOf(form.getRoadAddress()))
                .param("addressDetail", String.valueOf(form.getAddressDetail()))
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                }).with(csrf())
                .contentType(MediaType.MULTIPART_FORM_DATA));

        //then
        ArgumentCaptor<MemberPrivateProfileForm> captor = ArgumentCaptor.forClass(MemberPrivateProfileForm.class);
        verify(profileFacade, times(1))
                .deleteProfileUrlAndGetAddressUpdateProfile(eq("test@test.com"), captor.capture());

        MemberPrivateProfileForm expectedForm = captor.getValue();

        assertAll(
                () -> assertEquals(form.getNickName(), expectedForm.getNickName()),
                () -> assertEquals(form.getPhoneNumber(), expectedForm.getPhoneNumber()),
                () -> assertEquals(form.getRoadAddress(), expectedForm.getRoadAddress()),
                () -> assertEquals(form.getAddressDetail(), expectedForm.getAddressDetail()),
                () -> assertEquals(form.getProfileImage().getSize(), expectedForm.getProfileImage().getSize())
        );

    }

    @Test
    @WithCustomMockUser
    @DisplayName("요리사 등록하기")
    void success_registerChef() throws Exception {
        //when
        ChefIntroduceForm form = new ChefIntroduceForm("요리사 소개입니다.");

        ResultActions perform = mockMvc.perform(post("/profile/private/registerchef")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("요리사가 되었습니다."));

        ArgumentCaptor<ChefIntroduceForm> captor = ArgumentCaptor.forClass(ChefIntroduceForm.class);
        verify(chefMemberService, times(1))
                .registerChef(eq(1L), captor.capture());

        assertEquals(form.getIntroduce(), captor.getValue().getIntroduce());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("요리사 정보 수정")
    void success_updateChefIntroduce() throws Exception {

        //when
        ChefIntroduceForm form = new ChefIntroduceForm("요리사 소개입니다.");

        ResultActions perform = mockMvc.perform(put("/profile/private/introduce")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("요리사 소개가 수정되었습니다."));

        ArgumentCaptor<ChefIntroduceForm> captor = ArgumentCaptor.forClass(ChefIntroduceForm.class);
        verify(chefMemberService, times(1))
                .updateChefIntroduce(eq(1L), captor.capture());

        assertEquals(form.getIntroduce(), captor.getValue().getIntroduce());

    }

    @Test
    @WithCustomMockUser
    @DisplayName("기본 공개 프로필 조회")
    void success_getPublicDefaultProfile() throws Exception {

        //given
        DefaultProfileDtoResponse response = DefaultProfileDtoResponse
                .builder()
                .memberId(1L)
                .nickname("요리왕")
                .profileUrl("https://s3.test.com/king.png")
                .receivedHeart(1000)
                .isChef(true)
                .chefMemberId(1L)
                .build();

        given(memberService.getDefaultProfile(anyLong()))
                .willReturn(response);

        //when
        ResultActions perform = mockMvc.perform(get("/profile/public/1"));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.memberId").value(response.getMemberId()),
                        jsonPath("$.nickname").value(response.getNickname()),
                        jsonPath("$.profileUrl").value(response.getProfileUrl()),
                        jsonPath("$.receivedHeart").value(response.getReceivedHeart()),
                        jsonPath("$.isChef").value(response.getIsChef()),
                        jsonPath("$.chefMemberId").value(response.getChefMemberId())
                );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("요리사 프로필 조회")
    void success_getChefProfile() throws Exception {
        //given
        ChefProfileDto profileDto = ChefProfileDto
                .builder()
                .exp(1000)
                .grade(GradeType.PLATINUM)
                .build();
        given(chefMemberService.getChefProfile(anyLong()))
                .willReturn(profileDto);

        //when
        ResultActions perform = mockMvc.perform(get("/profile/public/chef/1"));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.exp").value(profileDto.getExp()),
                        jsonPath("$.grade").value(profileDto.getGrade().name())
                );

    }

    @Test
    @WithCustomMockUser
    @DisplayName("요리사 후기 목록 조회")
    void success_getChefReviewList() throws Exception {
        //given
        ChefReviewResponseDto responseDto = ChefReviewResponseDto
                .builder()
                .nickname("일반인A")
                .profileUrl("https://s3.test.com/profile.png")
                .content("이런 저런 내용임")
                .star(3)
                .build();
        given(chefReviewService.getChefReviewList(anyLong(), any()))
                .willReturn(ListResponse.from(
                        new PageImpl<>(
                                List.of(
                                        responseDto
                                )
                        )
                ));

        //when
        ResultActions perform = mockMvc.perform(get("/profile/public/chefreview/0/10/1"));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content.[0].nickname").value(responseDto.getNickname()),
                        jsonPath("$.content.[0].profileUrl").value(responseDto.getProfileUrl()),
                        jsonPath("$.content.[0].content").value(responseDto.getContent()),
                        jsonPath("$.content.[0].star").value(responseDto.getStar())
                );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("공개 프로필 꿀조합 리뷰 목록 조회")
    void success_getRecipeReviewListForProfile() throws Exception {
        //given
        RecipeReviewResponseDto responseDto = RecipeReviewResponseDto
                .builder()
                .recipeId(1L)
                .recipeReviewId(1L)
                .nickname("일반인B")
                .content("나만 아는 꿀조합임")
                .star(5)
                .cookUrl("https://s3.test.com/gguljohap.png")
                .createdAt(LocalDateTime.of(2023, 10, 18,
                        18, 30, 21))
                .build();
        given(recipeReviewService.getRecipeReviewForProfile(anyLong(), any()))
                .willReturn(ListResponse.from(
                        new PageImpl<>(
                                List.of(
                                        responseDto
                                )
                        )
                ));
        //when
        ResultActions perform = mockMvc.perform(get("/profile/public/recipereview/0/10/1"));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content.[0].recipeId").value(responseDto.getRecipeId()),
                        jsonPath("$.content.[0].recipeReviewId").value(responseDto.getRecipeReviewId()),
                        jsonPath("$.content.[0].nickname").value(responseDto.getNickname()),
                        jsonPath("$.content.[0].content").value(responseDto.getContent()),
                        jsonPath("$.content.[0].star").value(responseDto.getStar()),
                        jsonPath("$.content.[0].cookUrl").value(responseDto.getCookUrl()),
                        jsonPath("$.content.[0].createdAt").value("2023-10-18 18:30:21")
                );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("공개 프로필 올린 꿀조합 목록 조회")
    void success_getRecipeListByMemberId() throws Exception {
        //given
        RecipeDtoTopResponse response = RecipeDtoTopResponse
                .builder()
                .recipeId(1L)
                .mainImageUrl("https://s3.test.com/main.png")
                .title("제로베이스 왕갈비 치킨")
                .content("이건 치킨인가? 갈비인가?")
                .heartCount(100000)
                .isHeart(true)
                .build();

        given(recipeService.getRecipeListByMemberId(anyLong(), anyLong(), any()))
                .willReturn(ListResponse.from(
                        new PageImpl<>(
                                List.of(
                                        response
                                )
                        )
                ));

        //when
        ResultActions perform = mockMvc.perform(get("/profile/public/recipe/0/10/1"));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content.[0].recipeId").value(response.getRecipeId()),
                        jsonPath("$.content.[0].mainImageUrl").value(response.getMainImageUrl()),
                        jsonPath("$.content.[0].title").value(response.getTitle()),
                        jsonPath("$.content.[0].content").value(response.getContent()),
                        jsonPath("$.content.[0].heartCount").value(response.getHeartCount()),
                        jsonPath("$.content.[0].isHeart").value(response.getIsHeart())
                );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("상위 5명 탑 요리사 조회")
    void success_getTopChefList() throws Exception {
        //given
        TopChefDto chefOne = TopChefDto
                .builder()
                .memberId(1L)
                .chefMemberId(1L)
                .nickname("요리의신")
                .profileUrl("https://s3.test.com/chefOne.png")
                .build();
        TopChefDto chefTwo = TopChefDto
                .builder()
                .memberId(2L)
                .chefMemberId(2L)
                .nickname("반찬의신")
                .profileUrl("https://s3.test.com/chefTwo.png")
                .build();
        TopChefDto chefThree = TopChefDto
                .builder()
                .memberId(3L)
                .chefMemberId(3L)
                .nickname("찌개의신")
                .profileUrl("https://s3.test.com/chefThree.png")
                .build();
        TopChefDto chefFour = TopChefDto
                .builder()
                .memberId(4L)
                .chefMemberId(4L)
                .nickname("고기의신")
                .profileUrl("https://s3.test.com/chefFour.png")
                .build();
        TopChefDto chefFive = TopChefDto
                .builder()
                .memberId(5L)
                .chefMemberId(5L)
                .nickname("떡볶이연구가")
                .profileUrl("https://s3.test.com/chefFive.png")
                .build();

        given(chefMemberService.getTopChefList())
                .willReturn(List.of(
                        chefOne,
                        chefTwo,
                        chefThree,
                        chefFour,
                        chefFive
                ));

        //when
        ResultActions perform = mockMvc.perform(get("/profile/public/topchef"));

        //then
        perform.andDo(print())
                .andExpectAll(
                        status().isOk(),

                        jsonPath("$.[0].memberId").value(chefOne.getMemberId()),
                        jsonPath("$.[0].chefMemberId").value(chefOne.getChefMemberId()),
                        jsonPath("$.[0].nickname").value(chefOne.getNickname()),
                        jsonPath("$.[0].profileUrl").value(chefOne.getProfileUrl()),

                        jsonPath("$.[1].memberId").value(chefTwo.getMemberId()),
                        jsonPath("$.[1].chefMemberId").value(chefTwo.getChefMemberId()),
                        jsonPath("$.[1].nickname").value(chefTwo.getNickname()),
                        jsonPath("$.[1].profileUrl").value(chefTwo.getProfileUrl()),

                        jsonPath("$.[2].memberId").value(chefThree.getMemberId()),
                        jsonPath("$.[2].chefMemberId").value(chefThree.getChefMemberId()),
                        jsonPath("$.[2].nickname").value(chefThree.getNickname()),
                        jsonPath("$.[2].profileUrl").value(chefThree.getProfileUrl()),

                        jsonPath("$.[3].memberId").value(chefFour.getMemberId()),
                        jsonPath("$.[3].chefMemberId").value(chefFour.getChefMemberId()),
                        jsonPath("$.[3].nickname").value(chefFour.getNickname()),
                        jsonPath("$.[3].profileUrl").value(chefFour.getProfileUrl()),

                        jsonPath("$.[4].memberId").value(chefFive.getMemberId()),
                        jsonPath("$.[4].chefMemberId").value(chefFive.getChefMemberId()),
                        jsonPath("$.[4].nickname").value(chefFive.getNickname()),
                        jsonPath("$.[4].profileUrl").value(chefFive.getProfileUrl())
                );
    }

    @Test
    @WithCustomMockUser
    @DisplayName("비밀번호 변경")
    void success_updatePassword() throws Exception {
        //given
        given(memberService.updatePassword(any(), any()))
                .willReturn("비밀번호 변경 완료");

        //when
        PasswordChangeForm form = PasswordChangeForm
                .builder()
                .currentPassword("(pass2worD)")
                .newPassword("(pass$2worD)")
                .build();

        ResultActions perform = mockMvc.perform(put("/profile/private/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))
                .with(csrf()));

        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("비밀번호 변경 완료"));

        ArgumentCaptor<PasswordChangeForm> captor = ArgumentCaptor.forClass(PasswordChangeForm.class);
        verify(memberService, times(1)).updatePassword(any(), captor.capture());

        assertAll(
                () -> assertEquals(form.getCurrentPassword(), captor.getValue().getCurrentPassword()),
                () -> assertEquals(form.getNewPassword(), captor.getValue().getNewPassword())
        );
    }
}