package com.zerobase.foodlier.module.member.member.service;

import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.member.member.domain.vo.Address;
import com.zerobase.foodlier.module.member.member.dto.MemberRegisterDto;
import com.zerobase.foodlier.module.member.member.exception.MemberException;
import com.zerobase.foodlier.module.member.member.repository.MemberRepository;
import com.zerobase.foodlier.module.member.member.type.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.zerobase.foodlier.module.member.member.exception.MemberErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(MemberRegisterDto memberRegisterDto) {
        validateRegister(memberRegisterDto);
        memberRepository.save(
                Member.builder()
                        .nickname(memberRegisterDto.getNickname())
                        .email(memberRegisterDto.getEmail())
                        .password(passwordEncoder.encode(memberRegisterDto.getPassword()))
                        .phoneNumber(memberRegisterDto.getPhoneNumber())
                        .profileUrl(memberRegisterDto.getPhoneNumber())
                        .address(
                                Address.builder()
                                        .roadAddress(memberRegisterDto.getRoadAddress())
                                        .addressDetail(memberRegisterDto.getAddressDetail())
                                        .lat(memberRegisterDto.getLat())
                                        .lnt(memberRegisterDto.getLnt())
                                        .build()
                        )
                        .registrationType(memberRegisterDto.getRegistrationType())
                        .roles(new ArrayList<>(
                                List.of(RoleType.ROLE_USER.name())
                        ))
                        .build()
        );
    }


    //======================= Validates =========================

    private void validateRegister(MemberRegisterDto memberRegisterDto){
        if(memberRepository.existsByEmail(memberRegisterDto.getEmail())){
            throw new MemberException(EMAIL_IS_ALREADY_EXIST);
        }
        if(memberRepository.existsByNickname(memberRegisterDto.getNickname())){
            throw new MemberException(NICKNAME_IS_ALREADY_EXIST);
        }
        if(memberRepository.existsByPhoneNumber(memberRegisterDto.getPhoneNumber())){
            throw new MemberException(PHONE_NUMBER_IS_ALREADY_EXIST);
        }
    }
}
