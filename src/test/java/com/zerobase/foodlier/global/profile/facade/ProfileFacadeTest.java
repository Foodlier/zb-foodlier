package com.zerobase.foodlier.global.profile.facade;

import com.zerobase.foodlier.common.s3.service.S3Service;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.domain.vo.Address;
import com.zerobase.foodlier.module.member.member.local.dto.CoordinateResponseDto;
import com.zerobase.foodlier.module.member.member.local.service.LocalService;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberPrivateProfileForm;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberUpdateDto;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProfileFacadeTest {
    @Mock
    private S3Service s3Service;

    @Mock
    private MemberService memberService;

    @Mock
    private LocalService localService;

    @InjectMocks
    private ProfileFacade profileFacade;

    @Test
    @DisplayName("프로필 파서드 성공 케이스 " +
            "- 입력받은 값을 통해 s3에 이미지가 존재하면 해당 이미지를 삭제하고 입력받은 이미지를 s3에 등록, 입력받은 도로명 주소가 있다면 해당 주소의 위도,경도를 받아서 전달, 변경된 값을 멤버의 값에 업데이트해준다.")
    void success_deleteProfileUrlAndGetAddressUpdateProfile() {
        //given
        Member member = Member.builder()
                .email("test@test.com")
                .password("1")
                .phoneNumber("010-1234-5678")
                .profileUrl("https://test.s3/s3Image.png")
                .address(Address.builder()
                        .roadAddress("경기도 성남시 분당구 판교역로 166")
                        .addressDetail("카카오 판교")
                        .lat(37.1)
                        .lnt(127.1)
                        .build())
                .nickname("test")
                .build();

        String fileName = "test";
        String content = "";
        MultipartFile multipartFile =
                new MockMultipartFile(fileName, fileName,
                        "image/png", content.getBytes());
        MemberPrivateProfileForm form = MemberPrivateProfileForm.builder()
                .nickName("test2")
                .profileImage(multipartFile)
                .phoneNumber("010-8765-4321")
                .roadAddress("경기도 연천군 청산면 청창로 471")
                .addressDetail("신교대")
                .build();

        CoordinateResponseDto coordinateResponseDto =
                new CoordinateResponseDto(38.0, 127.1);

        given(memberService.findByEmail(anyString()))
                .willReturn(member);
        given(s3Service.getImageUrl(any()))
                .willReturn("https://test.s3/s3Image2.png");
        given(localService.getCoordinate(anyString()))
                .willReturn(coordinateResponseDto);

        ArgumentCaptor<MemberUpdateDto> memberUpdateDtoCaptor =
                ArgumentCaptor.forClass(MemberUpdateDto.class);
        ArgumentCaptor<Member> memberCaptor =
                ArgumentCaptor.forClass(Member.class);

        //when
        profileFacade.deleteProfileUrlAndGetAddressUpdateProfile(member.getEmail(),
                form);

        //then
        verify(memberService, times(1))
                .updatePrivateProfile(memberUpdateDtoCaptor.capture(),
                        memberCaptor.capture());
        MemberUpdateDto memberUpdateDtoCaptorValue = memberUpdateDtoCaptor.getValue();
        Member memberCaptorValue = memberCaptor.getValue();

        assertAll(
                () -> assertEquals("test",
                        memberCaptorValue.getNickname()),
                () -> assertEquals("1",
                        memberCaptorValue.getPassword()),
                () -> assertEquals("010-1234-5678",
                        memberCaptorValue.getPhoneNumber()),
                () -> assertEquals("경기도 성남시 분당구 판교역로 166",
                        memberCaptorValue.getAddress().getRoadAddress()),
                () -> assertEquals("카카오 판교",
                        memberCaptorValue.getAddress().getAddressDetail()),
                () -> assertEquals("test@test.com",
                        memberCaptorValue.getEmail()),
                () -> assertEquals("https://test.s3/s3Image.png",
                        memberCaptorValue.getProfileUrl()),
                () -> assertEquals(37.1,
                        memberCaptorValue.getAddress().getLat()),
                () -> assertEquals(127.1,
                        memberCaptorValue.getAddress().getLnt())
        );

        assertAll(
                () -> assertEquals("test2",
                        memberUpdateDtoCaptorValue.getNickName()),
                () -> assertEquals("https://test.s3/s3Image2.png",
                        memberUpdateDtoCaptorValue.getProfileUrl()),
                () -> assertEquals("010-8765-4321",
                        memberUpdateDtoCaptorValue.getPhoneNumber()),
                () -> assertEquals("경기도 연천군 청산면 청창로 471",
                        memberUpdateDtoCaptorValue.getRoadAddress()),
                () -> assertEquals("신교대",
                        memberUpdateDtoCaptorValue.getAddressDetail()),
                () -> assertEquals(38.0,
                        memberUpdateDtoCaptorValue.getLat()),
                () -> assertEquals(127.1,
                        memberUpdateDtoCaptorValue.getLnt())
        );
    }
}