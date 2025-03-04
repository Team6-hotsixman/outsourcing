package com.example.outsourcing.domain.order.dto;

import com.example.outsourcing.domain.order.entity.Orders;
import com.example.outsourcing.domain.order.enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class OrderResponseDto {

    private final Integer totalPriceAmount;
    private final LocalDateTime orderAt;

    @Enumerated(EnumType.STRING)
    private final OrderStatus orderStatus;

    public OrderResponseDto(
            Integer totalPriceAmount,
            LocalDateTime orderAt,
            OrderStatus orderStatus
    ) {
        this.totalPriceAmount = totalPriceAmount;
        this.orderAt = orderAt;
        this.orderStatus = orderStatus;
    }

    public static OrderResponseDto of(Orders order) {
        return new OrderResponseDto(
                order.getTotalPriceAmount(),
                order.getOrderAt(),
                order.getOrderStatus()
        );
    }
}
