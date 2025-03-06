package com.example.outsourcing.domain.store.dto.response;

import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import lombok.Builder;
import lombok.Getter;

;

@Getter
public class StoreListResponseDto {
    private final Long id;

    private final String imagePath;

    private final String categoryName;

    private final String storeName;

    private final StoreStatus storeStatus;

    private final Integer minOrderPrice;

    @Builder
    public StoreListResponseDto(Long id, String imagePath, String categoryName, String storeName, StoreStatus storeStatus, Integer minOrderPrice) {
        this.id = id;
        this.imagePath = imagePath;
        this.categoryName = categoryName;
        this.storeName = storeName;
        this.storeStatus = storeStatus;
        this.minOrderPrice = minOrderPrice;
    }
    public static StoreListResponseDto of(Store store) {
        return new StoreListResponseDto(
                store.getId(),
                store.getImage().getImagePath(),
                store.getCategory().getCategoryName(),
                store.getStoreName(),
                store.getStoreStatus(),
                store.getMinOrderPrice()
        );
    }
}
