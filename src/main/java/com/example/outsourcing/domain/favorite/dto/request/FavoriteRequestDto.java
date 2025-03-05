package com.example.outsourcing.domain.favorite.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FavoriteRequestDto {

    private final Long storeId;

    @Builder
    public FavoriteRequestDto(Long storeId) {
        this.storeId = storeId;
    }
}
