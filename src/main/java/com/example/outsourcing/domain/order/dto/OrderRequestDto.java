package com.example.outsourcing.domain.order.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class OrderRequestDto {

    private List<OrderItemRequestDto> orderItems;
    private Integer usedPoint;
}