package com.example.outsourcing.domain.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LatLng {

    @JsonProperty("y")
    private double latitude;

    @JsonProperty("x")
    private double longitude;
}
