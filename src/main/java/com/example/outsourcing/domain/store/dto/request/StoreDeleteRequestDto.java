package com.example.outsourcing.domain.store.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreDeleteRequestDto {
    private final String password;

    @Builder
    public StoreDeleteRequestDto(String password) {
        this.password = password;
    }
}
