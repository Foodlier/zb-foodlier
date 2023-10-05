package com.zerobase.foodlier.module.recipe.domain.vo;

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
public class RecipeStatistics {

    private static final int ZERO = 0;

    private int reviewCount;
    private int reviewStarSum;
    private double reviewStarAverage;

    public void plusStar(int star){
        this.reviewCount++;
        this.reviewStarSum += star;
        this.reviewStarAverage = this.calcStarAvg();
    }

    public void updateStar(int originStar, int newStar){
        this.reviewStarSum = Math.max(ZERO, this.reviewStarSum - originStar);
        this.reviewStarSum += newStar;
        this.reviewStarAverage = this.calcStarAvg();
    }

    public void minusStar(int star){
        this.reviewCount = Math.max(ZERO, --this.reviewCount);
        this.reviewStarSum = Math.max(ZERO, this.reviewStarSum - star);
        this.reviewStarAverage = this.calcStarAvg();
    }

    private double calcStarAvg(){
        if(this.reviewCount <= ZERO){
            return ZERO;
        }
        return  (double) this.reviewStarSum / this.reviewCount;
    }

}
