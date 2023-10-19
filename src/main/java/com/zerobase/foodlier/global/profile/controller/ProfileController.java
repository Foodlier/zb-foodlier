package com.zerobase.foodlier.global.profile.controller;

import com.zerobase.foodlier.common.response.ListResponse;
import com.zerobase.foodlier.common.security.provider.dto.MemberAuthDto;
import com.zerobase.foodlier.global.profile.facade.ProfileFacade;
import com.zerobase.foodlier.module.comment.comment.dto.MyPageCommentDto;
import com.zerobase.foodlier.module.comment.comment.service.CommentService;
import com.zerobase.foodlier.module.member.chef.dto.ChefIntroduceForm;
import com.zerobase.foodlier.module.member.chef.dto.ChefProfileDto;
import com.zerobase.foodlier.module.member.chef.dto.TopChefDto;
import com.zerobase.foodlier.module.member.chef.service.ChefMemberService;
import com.zerobase.foodlier.module.member.member.dto.DefaultProfileDtoResponse;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberPrivateProfileForm;
import com.zerobase.foodlier.module.member.member.profile.dto.MemberPrivateProfileResponse;
import com.zerobase.foodlier.module.member.member.profile.dto.PasswordChangeForm;
import com.zerobase.foodlier.module.member.member.service.MemberService;
import com.zerobase.foodlier.module.recipe.dto.recipe.RecipeDtoTopResponse;
import com.zerobase.foodlier.module.recipe.service.recipe.RecipeService;
import com.zerobase.foodlier.module.review.chef.dto.ChefReviewResponseDto;
import com.zerobase.foodlier.module.review.chef.service.ChefReviewService;
import com.zerobase.foodlier.module.review.recipe.dto.RecipeReviewResponseDto;
import com.zerobase.foodlier.module.review.recipe.service.RecipeReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
@Slf4j
public class ProfileController {
    private final MemberService memberService;
    private final ProfileFacade profileFacade;
    private final ChefMemberService chefMemberService;
    private final ChefReviewService chefReviewService;
    private final RecipeReviewService recipeReviewService;
    private final RecipeService recipeService;
    private final CommentService commentService;

    @GetMapping("/private")
    public ResponseEntity<MemberPrivateProfileResponse> getPrivateProfile(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto
    ) {
        return ResponseEntity.ok(memberService.getPrivateProfile(memberAuthDto));
    }

    @GetMapping("/private/heart/{pageIdx}/{pageSize}")
    public ResponseEntity<ListResponse<RecipeDtoTopResponse>> getRecipeForHeart(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable int pageIdx,
            @PathVariable int pageSize
    ){
        return ResponseEntity.ok(
                recipeService.getRecipeForHeart(memberAuthDto.getId(),
                        PageRequest.of(pageIdx, pageSize))
        );
    }

    @GetMapping("/private/comment/{pageIdx}/{pageSize}")
    public ResponseEntity<ListResponse<MyPageCommentDto>> getMyCommentList(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable int pageIdx,
            @PathVariable int pageSize
    ){
        return ResponseEntity.ok(
                commentService.getMyCommentList(
                memberAuthDto.getId(),
                PageRequest.of(pageIdx, pageSize)
        ));
    }

    @PutMapping(value = "/private")
    public ResponseEntity<String> updatePrivateProfile(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @ModelAttribute @Valid MemberPrivateProfileForm form
    ) {
        profileFacade.deleteProfileUrlAndGetAddressUpdateProfile(
                memberAuthDto.getEmail(), form);
        return ResponseEntity.ok("내 정보 수정 완료");
    }

    @PostMapping("/private/registerchef")
    public ResponseEntity<String> registerChef(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @RequestBody ChefIntroduceForm chefIntroduceForm
    ) {
        chefMemberService.registerChef(memberAuthDto.getId(), chefIntroduceForm);
        return ResponseEntity.ok("요리사가 되었습니다.");
    }

    @PutMapping("/private/introduce")
    public ResponseEntity<String> updateChefIntroduce(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @RequestBody ChefIntroduceForm chefIntroduceForm
    ) {
        chefMemberService.updateChefIntroduce(memberAuthDto.getId(), chefIntroduceForm);
        return ResponseEntity.ok("요리사 소개가 수정되었습니다.");
    }

    @GetMapping("public/{memberId}")
    public ResponseEntity<DefaultProfileDtoResponse> getPublicDefaultProfile(
            @PathVariable Long memberId
    ){
        return ResponseEntity.ok(
                memberService.getDefaultProfile(memberId)
        );
    }

    @GetMapping("public/chef/{chefMemberId}")
    public ResponseEntity<ChefProfileDto> getChefProfile(
            @PathVariable Long chefMemberId
    ){
        return ResponseEntity.ok(
            chefMemberService.getChefProfile(chefMemberId)
        );
    }

    @GetMapping("/public/chefreview/{pageIdx}/{pageSize}/{chefMemberId}")
    public ResponseEntity<ListResponse<ChefReviewResponseDto>> getChefReviewList(
            @PathVariable int pageIdx,
            @PathVariable int pageSize,
            @PathVariable Long chefMemberId
    ) {
        return ResponseEntity.ok(
                chefReviewService.getChefReviewList(chefMemberId,
                        PageRequest.of(pageIdx, pageSize))
        );
    }

    @GetMapping("/public/recipereview/{pageIdx}/{pageSize}/{memberId}")
    public ResponseEntity<ListResponse<RecipeReviewResponseDto>> getRecipeReviewListForProfile(
            @PathVariable int pageIdx,
            @PathVariable int pageSize,
            @PathVariable Long memberId
    ){
        return ResponseEntity.ok(
                recipeReviewService.getRecipeReviewForProfile(memberId,
                        PageRequest.of(pageIdx, pageSize))
        );
    }


    @GetMapping("/public/recipe/{pageIdx}/{pageSize}/{memberId}")
    public ResponseEntity<ListResponse<RecipeDtoTopResponse>> getRecipeListByMemberId(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @PathVariable int pageIdx,
            @PathVariable int pageSize,
            @PathVariable("memberId") Long targetMemberId
    ){
        return ResponseEntity.ok(
                recipeService.getRecipeListByMemberId(memberAuthDto.getId(),
                        targetMemberId, PageRequest.of(pageIdx, pageSize))
        );
    }

    @GetMapping("/public/topchef")
    public ResponseEntity<List<TopChefDto>> getTopChefList(){
        return ResponseEntity.ok(
            chefMemberService.getTopChefList()
        );
    }

    @PutMapping("/private/password")
    public ResponseEntity<String> updatePassword(
            @AuthenticationPrincipal MemberAuthDto memberAuthDto,
            @RequestBody @Valid PasswordChangeForm form
    ) {
        return ResponseEntity.ok(memberService.updatePassword(memberAuthDto, form));
    }
}
