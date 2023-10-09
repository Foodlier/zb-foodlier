package com.zerobase.foodlier.module.dm.room.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
