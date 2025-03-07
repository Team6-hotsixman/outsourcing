package com.example.outsourcing.domain.order.dto.request;

import com.example.outsourcing.domain.buket.dto.response.CartResponseDto;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.order.entity.OrderItem;
import com.example.outsourcing.domain.order.entity.OrderItemOption;
import com.example.outsourcing.domain.order.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDto {

    private Long menuId;
    private int quantity;
    private List<OrderItemOptionRequestDto> options;

    public static OrderItem toEntity(int quantity, Orders order, Menu menu, List<OrderItemOption> options) {
        return OrderItem.builder()
                .quantity(quantity)
                .order(order)
                .menu(menu)
                .options(options)
                .build();
    }

    public static List<OrderItemRequestDto> fromCart(List<CartResponseDto> cart){
        return cart.stream()
                .map(v -> new OrderItemRequestDto(v.getMenuId(), v.getQuantity(), v.getOptions())
                ).toList();
    }
}