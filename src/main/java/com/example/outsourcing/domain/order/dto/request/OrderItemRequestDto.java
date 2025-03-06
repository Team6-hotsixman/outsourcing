package com.example.outsourcing.domain.order.dto.request;

import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.order.entity.OrderItem;
import com.example.outsourcing.domain.order.entity.Orders;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderItemRequestDto {

    private Long menuId;
    private int quantity;
    private List<OrderItemOptionRequestDto> options;

    public static OrderItem toEntity(int quantity, Orders order, Menu menu) {
        return OrderItem.builder()
                .quantity(quantity)
                .order(order)
                .menu(menu)
                .build();
    }
}