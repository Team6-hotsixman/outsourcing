package com.example.outsourcing.domain.store.dto.request;

import com.example.outsourcing.domain.store.enums.StoreStatus;
import lombok.Builder;
import lombok.Getter;


@Getter
public class StoreStatusUpdateRequestDto {
    private final StoreStatus storeStatus;

    @Builder
    public StoreStatusUpdateRequestDto(StoreStatus storeStatus) {
        this.storeStatus = storeStatus;
    }
}
