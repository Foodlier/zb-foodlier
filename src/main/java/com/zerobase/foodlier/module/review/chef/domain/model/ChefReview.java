package com.zerobase.foodlier.module.review.chef.domain.model;

import com.zerobase.foodlier.common.jpa.audit.Audit;
import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.request.domain.model.Request;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chef_review")
public class ChefReview extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private int star;

    @ManyToOne
    @JoinColumn(name = "chef_member_id")
    private ChefMember chefMember;

    @OneToOne
    private Request request;
}
