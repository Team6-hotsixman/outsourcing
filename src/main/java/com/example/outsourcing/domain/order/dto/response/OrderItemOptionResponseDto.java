package com.example.outsourcing.domain.order.dto.response;

import lombok.Getter;

@Getter
public class OrderItemOptionResponseDto {

    private final Long menuOptionId;
    private final String menuOptionName;
    private final int quantity;

    public OrderItemOptionResponseDto(Long menuOptionId, String menuOptionName, int quantity) {
        this.menuOptionId = menuOptionId;
        this.menuOptionName = menuOptionName;
        this.quantity = quantity;
    }
}
