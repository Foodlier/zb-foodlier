package com.zerobase.foodlier.module.member.member.domain.model;

import com.zerobase.foodlier.common.jpa.audit.Audit;
import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.member.domain.vo.Address;
import com.zerobase.foodlier.module.member.member.type.RegistrationType;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @Column(nullable = false)
    private String nickname;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private String phoneNumber;
    @Column(nullable = false)
    private String profileUrl;
    @Embedded
    private Address address;
    private long point;
    @Column(nullable = false)
    private RegistrationType registrationType;
    private boolean isDeleted;
    @ElementCollection
    @Builder.Default
    private List<String> roles = new ArrayList<>();
}
