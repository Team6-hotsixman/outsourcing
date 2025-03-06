package com.example.outsourcing.domain.order.dto.response;

import com.example.outsourcing.domain.order.entity.Orders;
import com.example.outsourcing.domain.order.enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderSimpleResponseDto {

    private final Long id;
    private final Integer totalPriceAmount;
    private final LocalDateTime orderAt;

    @Enumerated(EnumType.STRING)
    private final OrderStatus orderStatus;

    public OrderSimpleResponseDto(
            Long id,
            Integer totalPriceAmount,
            LocalDateTime orderAt,
            OrderStatus orderStatus
    ) {
        this.id = id;
        this.totalPriceAmount = totalPriceAmount;
        this.orderAt = orderAt;
        this.orderStatus = orderStatus;
    }

    public static OrderSimpleResponseDto of(Orders order) {
        return new OrderSimpleResponseDto(
                order.getId(),
                order.getTotalPriceAmount(),
                order.getOrderAt(),
                order.getOrderStatus()
        );
    }
}
