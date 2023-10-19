package com.zerobase.foodlier.module.recipe.dto.recipe;

import com.zerobase.foodlier.module.recipe.type.OrderType;
import com.zerobase.foodlier.module.recipe.type.SearchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeSearchRequest {
    private Long memberId;
    private SearchType searchType;
    private Pageable pageable;
    private String searchText;
    @Builder.Default
    private OrderType orderType = OrderType.CREATED_AT;


    public Pageable getPageable(){
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Order.desc(this.orderType.getOrderKey())));
    }

}
