package com.example.outsourcing.domain.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class LatLng {
    @JsonProperty("x")
    private double longitude;
    @JsonProperty("y")
    private double latitude;
}
