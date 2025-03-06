package com.example.outsourcing.domain.order.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderItemResponseDto {

    private final Long menuId;
    private final String menuName;
    private final int quantity;
    private final List<OrderItemOptionResponseDto> options;

    public OrderItemResponseDto(Long menuId, String menuName, int quantity, List<OrderItemOptionResponseDto> options) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.quantity = quantity;
        this.options = options;
    }
}
