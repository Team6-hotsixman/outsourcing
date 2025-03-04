package com.example.outsourcing.domain.order.dto.request;

import com.example.outsourcing.domain.order.entity.Orders;
import com.example.outsourcing.domain.order.enums.OrderStatus;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderRequestDto {

    @NotNull
    private List<OrderItemRequestDto> orderItems;
    private Integer usedPoint;

    public static Orders toEntity(
            Integer totalPriceAmount,
            Integer usedPoint,
            LocalDateTime orderAt,
            OrderStatus orderStatus,
            Store store,
            User user
            ) {
        return Orders.builder()
                .totalPriceAmount(totalPriceAmount)
                .usedPoint(usedPoint)
                .orderAt(orderAt)
                .orderStatus(orderStatus)
                .store(store)
                .user(user)
                .build();
    }
}