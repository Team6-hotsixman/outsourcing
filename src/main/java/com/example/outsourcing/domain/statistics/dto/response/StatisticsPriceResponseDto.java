package com.example.outsourcing.domain.statistics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatisticsPriceResponseDto {

    private final String storeName;

    private final Integer totalPrice;
}
