package com.example.outsourcing.domain.order.dto;

import lombok.Getter;

@Getter
public class OrderItemRequestDto {

    private Long menuId;
    private int quantity;
}