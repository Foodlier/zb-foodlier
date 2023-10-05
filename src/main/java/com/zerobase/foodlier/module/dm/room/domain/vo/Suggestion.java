package com.zerobase.foodlier.module.dm.room.domain.vo;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Suggestion {
    private Integer suggestedPrice;
    private Boolean isAccept;
    private Boolean isSuggested;
}
