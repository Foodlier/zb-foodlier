package com.zerobase.foodlier.module.requestform.domain.model;

import com.zerobase.foodlier.common.jpa.audit.Audit;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.request.domain.vo.Ingredient;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "request_form")
public class RequestForm extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private Long expectPrice;
    @Column(nullable = false)
    private LocalDateTime expectedAt;

    @ElementCollection
    @CollectionTable(name = "request_from_ingredient")
    @Builder.Default
    private List<Ingredient> ingredientList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

}