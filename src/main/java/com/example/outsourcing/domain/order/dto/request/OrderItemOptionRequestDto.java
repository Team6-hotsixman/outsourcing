package com.example.outsourcing.domain.order.dto.request;

import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.menu.menuoption.entity.MenuOption;
import com.example.outsourcing.domain.order.entity.OrderItem;
import com.example.outsourcing.domain.order.entity.OrderItemOption;
import com.example.outsourcing.domain.order.entity.Orders;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderItemOptionRequestDto {

    private Long menuOptionId;
    private int quantity;

    public static OrderItemOption toEntity(int quantity, OrderItem orderItem, MenuOption menuOption) {
        return OrderItemOption.builder()
                .quantity(quantity)
                .orderItem(orderItem)
                .menuOption(menuOption)
                .build();
    }
}