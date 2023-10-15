package com.zerobase.foodlier.module.member.member.social.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RequestBodyNaver {
    private String grant_type;
    private String client_id;
    private String client_secret;
}