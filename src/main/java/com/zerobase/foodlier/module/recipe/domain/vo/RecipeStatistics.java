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

    private int reviewCount;
    private int reviewStarSum;
    private double reviewStarAverage;

    public void plusStar(int star){
        this.reviewCount++;
        this.reviewStarSum += star;
        this.calcStarAvg();
    }

    public void updateStar(int originStar, int newStar){
        this.reviewStarSum -= originStar;

        if(this.reviewStarSum < 0){
            this.reviewStarSum = 0;
        }

        this.reviewStarSum += newStar;
        this.calcStarAvg();
    }

    public void minusStar(int star){
        this.reviewCount--;
        this.reviewStarSum -= star;

        if(this.reviewStarSum < 0){
            this.reviewStarSum = 0;
        }
        this.calcStarAvg();
    }

    private void calcStarAvg(){
        if(this.reviewCount <= 0){
            this.reviewStarAverage = 0;
            return;
        }
        this.reviewStarAverage = (double) reviewStarSum / reviewCount;
    }

}
