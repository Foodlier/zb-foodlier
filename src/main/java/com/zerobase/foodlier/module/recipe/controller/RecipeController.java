package com.zerobase.foodlier.module.recipe.controller;

import com.zerobase.foodlier.global.recipe.facade.RecipeFacade;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.dto.RecipeDto;
import com.zerobase.foodlier.module.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeFacade recipeFacade;
    private final RecipeService recipeService;

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createRecipe(Authentication authentication,
                                          @RequestBody RecipeDto recipeDto) {
        String email = ((Member) authentication.getPrincipal()).getEmail();
        recipeFacade.createRecipe(email, recipeDto);
        return ResponseEntity.ok("ok");
    }
}
