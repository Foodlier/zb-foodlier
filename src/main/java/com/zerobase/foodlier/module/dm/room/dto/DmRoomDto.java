package com.zerobase.foodlier.module.dm.room.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DmRoomDto {

    private Long roomId;
    private String nickname;
    private String profileUrl;
    private Long requestId;
    private Long expectedPrice;
    private Boolean isExit;
    private Boolean isSuggested;
    private String role;
}
