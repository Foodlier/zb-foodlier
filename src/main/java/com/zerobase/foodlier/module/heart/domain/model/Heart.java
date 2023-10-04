package com.zerobase.foodlier.module.heart.domain.model;

import com.zerobase.foodlier.common.jpa.audit.Audit;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Heart extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Recipe.class, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


}
