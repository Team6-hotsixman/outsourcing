package com.example.outsourcing.domain.store.dto.request;

import com.example.outsourcing.domain.store.enums.StoreStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StoreSaveRequestDto {
    private final Long imageId;

    private final Long categoryId;

    private final String storeName;

    private final StoreStatus storeStatus;

    private final String storeNotice;

    private final String address;

    private final int minOrderPrice;

    private final LocalTime openTime;

    private final LocalTime closeTime;

    @Builder
    public StoreSaveRequestDto(
            Long imageId,
            Long categoryId,
            String storeName,
            StoreStatus storeStatus,
            String storeNotice,
            String address,
            Integer minOrderPrice,
            LocalTime openTime,
            LocalTime closeTime
    ) {
        this.imageId = imageId;
        this.categoryId = categoryId;
        this.storeName = storeName;
        this.storeStatus = storeStatus;
        this.storeNotice = storeNotice;
        this.address = address;
        this.minOrderPrice = minOrderPrice;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}
