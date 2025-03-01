package com.example.outsourcing.domain.store.dto.response;

import com.example.outsourcing.domain.store.dto.request.StoreSaveRequestDto;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.store.enums.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StoreResponseDto {
    private final long id;

    private final long imageId;

    private final long catogoryId;

    private final String storeName;

    private final StoreStatus storeStatus;

    private final String storeNotice;

    private final String address;

    private final int minOrderPrice;

    private final LocalDateTime openTime;

    private final LocalDateTime closeTime;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public static StoreResponseDto of(Store store) {
        return new StoreResponseDto(
                store.getId(),
                store.getImage().getId(),
                store.getCategory().getId(),
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
