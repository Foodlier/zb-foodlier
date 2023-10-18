package com.zerobase.foodlier.module.dm.room.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Suggestion {
    private Integer suggestedPrice;
    private Boolean isAccept;
    private Boolean isSuggested;

    public void updateAccept(boolean value) {
        this.isAccept = value;
    }
}
