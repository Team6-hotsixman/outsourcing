package com.example.outsourcing.domain.store.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StoreUpdateRequestDto {
    private final Long categoryId;

    private final String storeName;

    private final Integer minOrderPrice;

    private final LocalTime openTime;

    private final LocalTime closeTime;

    @Builder
    public StoreUpdateRequestDto(
            Long categoryId,
            String storeName,
            Integer minOrderPrice,
            LocalTime openTime,
            LocalTime closeTime
    ) {
        this.categoryId = categoryId;
        this.storeName = storeName;
        this.minOrderPrice = minOrderPrice;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}
