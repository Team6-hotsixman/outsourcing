package com.example.outsourcing.domain.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 불필요한 필드 무시
public class KaKaoLatLngResponse {
    private List<LatLng> documents;
}
