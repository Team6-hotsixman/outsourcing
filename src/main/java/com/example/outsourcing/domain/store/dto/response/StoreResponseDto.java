package com.example.outsourcing.domain.store.dto.response;

import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StoreResponseDto {
    private final Long id;

    private final Long userId;

    private final Long imageId;

    private final String categoryName;

    private final String storeName;

    private final StoreStatus storeStatus;

    private final String storeNotice;

    private final String address;

    private final Integer minOrderPrice;

    private final LocalTime openTime;

    private final LocalTime closeTime;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    private double distance;
    private double rate;

    public StoreResponseDto(Store store, double distance, double rate) {
        this.id = store.getId();
        this.userId = store.getUser().getId();
        this.imageId = store.getImage().getId();
        this.categoryName = store.getCategory().getCategoryName();
        this.storeName = store.getStoreName();
        this.storeStatus = store.getStoreStatus();
        this.storeNotice = store.getStoreNotice();
        this.address = store.getAddress();
        this.minOrderPrice = store.getMinOrderPrice();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
        this.createdAt = store.getCreatedAt();
        this.modifiedAt = store.getModifiedAt();
        this.distance = distance;
        this.rate = rate;
    }

    public static StoreResponseDto of(Store store) {
        return new StoreResponseDto(
                store.getId(),
                store.getUser().getId(),
                store.getImage().getId(),
                store.getCategory().getCategoryName(),
                store.getStoreName(),
                store.getStoreStatus(),
                store.getStoreNotice(),
                store.getAddress(),
                store.getMinOrderPrice(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getCreatedAt(),
                store.getModifiedAt(),
                0,
                0
                );
    }
}
