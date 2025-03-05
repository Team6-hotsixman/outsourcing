package com.example.outsourcing.domain.store.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreNoticeUpdateRequestDto {
    private final String storeNotice;

    @Builder
    public StoreNoticeUpdateRequestDto(String storeNotice) {
        this.storeNotice = storeNotice;
    }
}
