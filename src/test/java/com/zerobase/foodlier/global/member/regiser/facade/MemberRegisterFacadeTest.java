package com.zerobase.foodlier.global.member.regiser.facade;

import com.zerobase.foodlier.common.redis.service.EmailVerificationService;
import com.zerobase.foodlier.common.s3.service.S3Service;
import com.zerobase.foodlier.module.member.member.dto.MemberInputDto;
import com.zerobase.foodlier.module.member.member.dto.MemberRegisterDto;
import com.zerobase.foodlier.module.member.member.local.dto.CoordinateResponseDto;
import com.zerobase.foodlier.module.member.member.local.service.LocalService;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import com.zerobase.foodlier.module.member.member.type.RegistrationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberRegisterFacadeTest {

    @Mock
    private EmailVerificationService emailVerificationService;
    @Mock
    private MemberService memberService;
    @Mock
    private LocalService localService;
    @Mock
    private S3Service s3Service;
    @InjectMocks
    private MemberRegisterFacade memberRegisterFacade;

    @Test
    @DisplayName("도메인 회원가입 성공 - 사진 O")
    void success_domainRegister_exists_image(){

        //given
        String fileName = "test";
        String content = "";
        MultipartFile imageFile = new MockMultipartFile(fileName,
                fileName, "image/jpeg", content.getBytes());

        MemberInputDto memberInputDto = MemberInputDto.builder()
                .nickname("nickname")
                .profileImage(imageFile)
                .email("test@test.com")
                .password("password")
                .phoneNumber("010-1111-1111")
                .roadAddress("경기도 성남시 분당구 판교역로 99999")
                .addressDetail("상세 주소")
                .build();

        given(localService.getCoordinate(anyString()))
                .willReturn(
                        new CoordinateResponseDto(
                                37.1, 127.1
                        )
                );

        given(s3Service.getImageUrl(imageFile))
                .willReturn("https://test.s3/s3image.png");

        //when
        memberRegisterFacade.domainRegister(memberInputDto);

        //then
        ArgumentCaptor<MemberRegisterDto> captor = ArgumentCaptor.forClass(MemberRegisterDto.class);

        verify(memberService, times(1)).register(captor.capture());
        MemberRegisterDto memberRegisterDto = captor.getValue();

        assertAll(
                () -> assertEquals(memberInputDto.getNickname(), memberRegisterDto.getNickname()),
                () -> assertEquals("https://test.s3/s3image.png", memberRegisterDto.getProfileUrl()),
                () -> assertEquals(memberInputDto.getEmail(), memberRegisterDto.getEmail()),
                () -> assertEquals(memberInputDto.getPassword(), memberRegisterDto.getPassword()),
                () -> assertEquals(memberInputDto.getPhoneNumber(), memberRegisterDto.getPhoneNumber()),
                () -> assertEquals(memberInputDto.getRoadAddress(), memberRegisterDto.getRoadAddress()),
                () -> assertEquals(memberInputDto.getAddressDetail(), memberRegisterDto.getAddressDetail()),
                () -> assertEquals(37.1, memberRegisterDto.getLat()),
                () -> assertEquals(127.1, memberRegisterDto.getLnt()),
                () -> assertEquals(RegistrationType.DOMAIN, memberRegisterDto.getRegistrationType())
        );

    }

    @Test
    @DisplayName("도메인 회원가입 성공 - 사진 X")
    void success_domainRegister_not_exists_image(){
        //given
        String fileName = "test";
        String content = "";

        MemberInputDto memberInputDto = MemberInputDto.builder()
                .nickname("nickname")
                .profileImage(null)
                .email("test@test.com")
                .password("password")
                .phoneNumber("010-1111-1111")
                .roadAddress("경기도 성남시 분당구 판교역로 99999")
                .addressDetail("상세 주소")
                .build();

        given(localService.getCoordinate(anyString()))
                .willReturn(
                        new CoordinateResponseDto(
                                37.1, 127.1
                        )
                );

        //when
        memberRegisterFacade.domainRegister(memberInputDto);

        //then
        ArgumentCaptor<MemberRegisterDto> captor = ArgumentCaptor.forClass(MemberRegisterDto.class);

        verify(memberService, times(1)).register(captor.capture());
        MemberRegisterDto memberRegisterDto = captor.getValue();

        assertAll(
                () -> assertEquals(memberInputDto.getNickname(), memberRegisterDto.getNickname()),
                () -> assertNull(memberRegisterDto.getProfileUrl()),
                () -> assertEquals(memberInputDto.getEmail(), memberRegisterDto.getEmail()),
                () -> assertEquals(memberInputDto.getPassword(), memberRegisterDto.getPassword()),
                () -> assertEquals(memberInputDto.getPhoneNumber(), memberRegisterDto.getPhoneNumber()),
                () -> assertEquals(memberInputDto.getRoadAddress(), memberRegisterDto.getRoadAddress()),
                () -> assertEquals(memberInputDto.getAddressDetail(), memberRegisterDto.getAddressDetail()),
                () -> assertEquals(37.1, memberRegisterDto.getLat()),
                () -> assertEquals(127.1, memberRegisterDto.getLnt()),
                () -> assertEquals(RegistrationType.DOMAIN, memberRegisterDto.getRegistrationType())
        );
    }

}