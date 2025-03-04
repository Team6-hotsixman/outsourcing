package com.example.outsourcing.domain.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @JsonProperty("address")
    private Map<String, String> detail;

    public LatLng getLatLng() {
        LatLng latLng = new LatLng();
        latLng.setLongitude(Double.parseDouble(detail.get("x")));
        latLng.setLatitude(Double.parseDouble(detail.get("y")));
        return latLng;
    }
}
