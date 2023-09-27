package com.zerobase.foodlier.module.dm.room.service;

import com.zerobase.foodlier.module.dm.room.domain.model.DmRoom;
import com.zerobase.foodlier.module.dm.room.repository.DmRoomRepository;
import com.zerobase.foodlier.module.member.chef.domain.model.ChefMember;
import com.zerobase.foodlier.module.member.member.domain.model.Member;
import com.zerobase.foodlier.module.recipe.domain.model.Recipe;
import com.zerobase.foodlier.module.request.domain.model.Request;
import com.zerobase.foodlier.module.request.domain.vo.Ingredient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DmRoomServiceImplTest {
    @Mock
    private DmRoomRepository dmRoomRepository;

    @InjectMocks
    private DmRoomServiceImpl dmRoomService;

    @Test
    void success_createDmRoom() {
        //given
        LocalDateTime now = LocalDateTime.now();

        Request request = Request.builder()
                .id(1L)
                .title("test")
                .content("test recipe")
                .expectPrice(10000L)
                .expectedAt(now)
                .isPaid(false)
                .ingredientList(new ArrayList<>(List.of(new Ingredient("감자"), new Ingredient("아이스크림"))))
                .member(Member.builder()
                        .id(1L)
                        .build())
                .recipe(Recipe.builder()
                        .build())
                .chefMember(ChefMember.builder()
                        .id(1L)
                        .build())
                .build();

        given(dmRoomRepository.save(any()))
                .willReturn(DmRoom.builder()
                        .request(request)
                        .isExist(false)
                        .build());

        ArgumentCaptor<DmRoom> captor = ArgumentCaptor.forClass(DmRoom.class);

        //when
        dmRoomService.createDmRoom(request);

        //then
        verify(dmRoomRepository, times(1))
                .save(captor.capture());
        DmRoom dmRoom = captor.getValue();

        assertAll(
                () -> assertEquals("test",
                        dmRoom.getRequest().getTitle()),
                () -> assertEquals("test recipe",
                        dmRoom.getRequest().getContent()),
                () -> assertEquals(10000L,
                        dmRoom.getRequest().getExpectPrice()),
                () -> assertEquals(now,
                        dmRoom.getRequest().getExpectedAt()),
                () -> assertEquals(false,
                        dmRoom.getRequest().isPaid()),
                () -> assertEquals("감자",
                        dmRoom.getRequest().getIngredientList().get(0).getIngredientName()),
                () -> assertEquals("아이스크림",
                        dmRoom.getRequest().getIngredientList().get(1).getIngredientName()),
                () -> assertEquals(1L,
                        dmRoom.getRequest().getMember().getId()),
                () -> assertEquals(1L,
                        dmRoom.getRequest().getChefMember().getId())
        );
    }

}