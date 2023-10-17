package com.zerobase.foodlier.module.comment.comment.domain.model;

import com.zerobase.foodlier.common.aop.notification.dto.Notify;
import com.zerobase.foodlier.common.jpa.audit.Audit;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comment")
public class Comment extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @Column(nullable = false)
    private String message;

    @Builder.Default
    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted = false;

    public void updateMessage(String message){
        this.message = message;
    }

    public void delete(){
        if(!this.isDeleted){
            this.isDeleted = true;
        }
    }
}
