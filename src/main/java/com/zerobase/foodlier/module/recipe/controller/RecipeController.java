package com.zerobase.foodlier.module.recipe.controller;

import com.zerobase.foodlier.module.recipe.domain.dto.RecipeDto;
import com.zerobase.foodlier.module.recipe.facade.RecipeFacade;
import com.zerobase.foodlier.module.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeFacade recipeFacade;
    private final RecipeService recipeService;

    @PostMapping
    public void createRecipe(RecipeDto recipeDto) {

    }

}
