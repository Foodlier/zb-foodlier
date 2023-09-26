package com.zerobase.foodlier.module.request.domain.model;

import com.zerobase.foodlier.common.jpa.audit.Audit;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.request.domain.vo.Ingredient;
import com.zerobase.foodlier.module.review.chef.domain.model.ChefReview;
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
@Table(name = "cook_request")
public class Request extends Audit {
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
    @Column(nullable = false)
    private boolean isPaid;
    private Long paidPrice;
    @ElementCollection
    @CollectionTable(name = "ingredient")
    @Builder.Default
    private List<Ingredient> ingredientList = new ArrayList<>();

    @OneToOne
    private ChefReview chefReview;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}
