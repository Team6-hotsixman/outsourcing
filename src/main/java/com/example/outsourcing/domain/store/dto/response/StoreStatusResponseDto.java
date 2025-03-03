package com.example.outsourcing.domain.store.dto.response;

import com.example.outsourcing.domain.store.enums.StoreStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreStatusResponseDto {
    private final StoreStatus storeStatus;
}
