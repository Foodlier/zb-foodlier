package com.zerobase.foodlier.module.member.member.local.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LocalApiResponse {
    private List<Documents> documents;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Documents{
        @JsonProperty(value = "x")
        private double lnt;
        @JsonProperty(value = "y")
        private double lat;
    }
}
