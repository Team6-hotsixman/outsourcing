package com.example.outsourcing.domain.store.dto.response;

import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class StoreSaveResponseDto {
    private final Long id;

    private final Long userId;

    private final String imagePath;

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

    public static StoreSaveResponseDto of(Store store) {
        return new StoreSaveResponseDto(
                store.getId(),
                store.getUser().getId(),
                store.getImage().getImagePath(),
                store.getCategory().getCategoryName(),
                store.getStoreName(),
                store.getStoreStatus(),
                store.getStoreNotice(),
                store.getAddress(),
                store.getMinOrderPrice(),
                store.getOpenTime(),
                store.getCloseTime(),
                store.getCreatedAt(),
                store.getModifiedAt()
                );
    }
}
