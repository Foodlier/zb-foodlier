package com.zerobase.foodlier.module.dm.room.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DmRoomDto {

    private Long id;
    private String nickname;
    private String profileUrl;
    private Long requestId;
    private Long expectedPrice;
    private boolean isExit;
    private boolean isSuggested;
    private String role;
}
