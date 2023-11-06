package com.zerobase.foodlier.global.recipe.controller;

import com.zerobase.foodlier.module.notification.domain.type.ActionType;
import com.zerobase.foodlier.module.notification.domain.type.PerformerType;
import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.common.validator.image.ImageFile;
import com.zerobase.foodlier.global.notification.facade.NotificationFacade;
import com.zerobase.foodlier.global.recipe.facade.RecipeFacade;
import com.zerobase.foodlier.module.heart.service.HeartService;
import com.zerobase.foodlier.module.notification.domain.type.NotificationType;
import com.zerobase.foodlier.module.notification.dto.notify.NotifyInfoDto;
import com.zerobase.foodlier.module.notification.dto.notify.Impl.HeartNotify;
import com.zerobase.foodlier.module.recipe.dto.recipe.*;
import com.zerobase.foodlier.module.recipe.service.recipe.RecipeService;
import com.zerobase.foodlier.module.recipe.type.OrderType;
import com.zerobase.foodlier.module.recipe.type.SearchType;
import com.zerobase.foodlier.module.recipe.validator.ValidOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
@Validated
public class RecipeController {

    private final RecipeFacade recipeFacade;
    private final RecipeService recipeService;
    private final HeartService heartService;
    private final NotificationFacade notificationFacade;

    @PostMapping("/image")
    public ResponseEntity<RecipeImageResponse> uploadRecipeImage(
            @Valid @ImageFile @RequestPart MultipartFile mainImage,
            @Valid @ImageFile @RequestPart List<MultipartFile> cookingOrderImageList) {

        return ResponseEntity.ok(recipeFacade.uploadRecipeImage(mainImage, cookingOrderImageList));
    }

    @PutMapping("/image/{recipeId}")
    public ResponseEntity<RecipeImageResponse> updateRecipeImage(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @Valid @ImageFile @RequestPart MultipartFile mainImage,
            @Valid @ImageFile @RequestPart List<MultipartFile> cookingOrderImageList,
            @PathVariable(name = "recipeId") Long recipeId) {
        return ResponseEntity.ok(recipeFacade.updateRecipeImage(memberAuthDto.getId(),
                mainImage, cookingOrderImageList, recipeId));
    }

    @PostMapping
    public ResponseEntity<String> createRecipe(@AuthenticationPrincipal MemberAuthDto memberAuthDto,
                                               @RequestBody @Valid RecipeDtoRequest recipeDto) {
        recipeFacade.createRecipe(memberAuthDto.getId(), recipeDto);
        return ResponseEntity.ok("게시글 작성을 성공했습니다.");
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<String> updateRecipe(@AuthenticationPrincipal MemberAuthDto memberAuthDto,
                                               @RequestBody @Valid RecipeDtoRequest recipeDto,
                                               @PathVariable(name = "recipeId") Long recipeId) {
        recipeFacade.updateRecipe(memberAuthDto.getId(), recipeDto, recipeId);
        return ResponseEntity.ok("게시글 수정을 성공했습니다.");
    }

    @GetMapping("/detail/{recipeId}")
    public ResponseEntity<RecipeDtoResponse> getRecipe(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable(name = "recipeId") Long recipeId
    ) {
        return ResponseEntity.ok(recipeService.getRecipeDetail(memberAuthDto, recipeId));
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<String> deleteRecipe(@AuthenticationPrincipal MemberAuthDto memberAuthDto,
                                               @PathVariable(name = "recipeId") Long recipeId) {
        recipeFacade.deleteRecipe(memberAuthDto.getId(), recipeId);
        return ResponseEntity.ok("레시피 삭제를 성공했습니다.");
    }

    @GetMapping("/permission/{recipeId}")
    public ResponseEntity<String> checkPermission(@AuthenticationPrincipal MemberAuthDto memberAuthDto,
                                                  @PathVariable(name = "recipeId") Long recipeId) {
        recipeFacade.checkPermission(memberAuthDto.getId(), recipeId);
        return ResponseEntity.ok("레시피 접근 가능합니다.");
    }

    @GetMapping("/search/{searchType}/{pageIdx}/{pageSize}")
    public ResponseEntity<ListResponse<RecipeCardDto>> getRecipeList(@AuthenticationPrincipal MemberAuthDto memberAuthDto,
                                                                     @PathVariable SearchType searchType,
                                                                     @PathVariable int pageIdx,
                                                                     @PathVariable int pageSize,
                                                                     @RequestParam String searchText) {
        return ResponseEntity.ok(recipeService.searchRecipeListBy(RecipeSearchRequest.builder()
                .searchType(searchType)
                .searchText(searchText)
                .memberId(memberAuthDto.getId())
                .pageable(PageRequest.of(pageIdx, pageSize))
                .build())
        );
    }

    @GetMapping("/search/{searchType}/{orderType}/{pageIdx}/{pageSize}")
    public ResponseEntity<ListResponse<RecipeCardDto>> getFilteredRecipeList(@AuthenticationPrincipal MemberAuthDto memberAuthDto,
                                                                             @PathVariable SearchType searchType,
                                                                             @PathVariable int pageIdx,
                                                                             @PathVariable int pageSize,
                                                                             @PathVariable OrderType orderType,
                                                                             @RequestParam String searchText) {
        return ResponseEntity.ok(recipeService.searchRecipeListBy(RecipeSearchRequest.builder()
                .searchType(searchType)
                .searchText(searchText)
                .memberId(memberAuthDto.getId())
                .pageable(PageRequest.of(pageIdx, pageSize))
                .orderType(orderType)
                .build())
        );
    }

    @PostMapping("/heart/{recipeId}")
    public ResponseEntity<String> createHeart(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable Long recipeId
    ) {
        HeartNotify heartNotify = HeartNotify.from(heartService.createHeart(memberAuthDto, recipeId),
                NotifyInfoDto.builder()
                        .notificationType(NotificationType.HEART)
                        .actionType(ActionType.HEART)
                        .performerType(PerformerType.PUSH_HEART)
                        .build()
        );
        notificationFacade.send(heartNotify);
        return ResponseEntity.ok("좋아요를 눌렀습니다");
    }

    @DeleteMapping("/heart/{recipeId}")
    public ResponseEntity<String> deleteHeart(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable Long recipeId
    ) {
        heartService.deleteHeart(memberAuthDto, recipeId);
        return ResponseEntity.ok("좋아요를 취소하였습니다.");
    }

    @GetMapping("/main")
    public ResponseEntity<List<RecipeCardDto>> getMainPageRecipeList(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto
    ) {
        return ResponseEntity.ok(recipeService.
                getRecentRecipeList(memberAuthDto));
    }

    @GetMapping("/default/{pageIdx}/{pageSize}")
    public ResponseEntity<ListResponse<RecipeCardDto>> getRecipePageRecipeList(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable int pageIdx,
            @PathVariable int pageSize,
            @RequestParam @ValidOrder OrderType orderType
    ) {
        return ResponseEntity.ok(recipeService
                .getDefaultRecipeList(memberAuthDto,
                        PageRequest.of(pageIdx, pageSize), orderType));
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<RecipeCardDto>> getRecommendedRecipeList(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto
    ) {
        return ResponseEntity.ok(recipeService.getRecommendedRecipeList(memberAuthDto));
    }
}
