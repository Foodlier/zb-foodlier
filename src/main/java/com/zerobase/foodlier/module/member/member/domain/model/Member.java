package com.zerobase.foodlier.module.member.member.domain.model;

import com.zerobase.foodlier.common.jpa.audit.Audit;
import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.member.domain.vo.Address;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberUpdateDto;
import com.zerobase.foodlier.module.member.member.type.RegistrationType;
import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.zerobase.foodlier.module.member.member.type.RegistrationType.DOMAIN;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "member")
public class Member extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private ChefMember chefMember;
    @Column(unique = true, nullable = false)
    private String nickname;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = false)
    private String phoneNumber;
    private String profileUrl;
    @Embedded
    private Address address;
    private long point;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RegistrationType registrationType;
    @Builder.Default
    private boolean isDeleted = false;
    @Builder.Default
    private boolean isTemp = false;
    @ElementCollection
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    public void transaction(int point) {
        this.point += point;
    }

    public void updateNickname(String nickname) {
        if (StringUtils.hasText(nickname)) {
            this.nickname = nickname;
        }
    }

    public void updatePhoneNumber(String phoneNumber) {
        if (StringUtils.hasText(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        }
    }

    public void updateAddress(MemberUpdateDto memberUpdateDto) {
        this.address = Address.builder()
                .roadAddress(memberUpdateDto.getRoadAddress())
                .addressDetail(memberUpdateDto.getAddressDetail() != null ?
                        memberUpdateDto.getAddressDetail() :
                        this.address.getAddressDetail())
                .lat(memberUpdateDto.getLat())
                .lnt(memberUpdateDto.getLnt())
                .build();
    }

    public void updateProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void deleteMember() {
        this.isDeleted = true;
    }

    public void updateTemp() {
        if (this.registrationType != DOMAIN && this.isTemp()) {
            this.isTemp = false;
        }
    }
}
