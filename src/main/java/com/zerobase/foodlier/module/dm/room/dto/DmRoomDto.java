package com.zerobase.foodlier.module.dm.room.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DmRoomDto {

    private Long dmRoomId;
    private String nickname;
    private String profileUrl;
    private Long requestId;
    private Long expectedPrice;
    private Boolean isExit;
    private Boolean isSuggested;
    private String role;
}
