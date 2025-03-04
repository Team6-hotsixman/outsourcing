package com.example.outsourcing.domain.order.dto.request;

import com.example.outsourcing.domain.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderStatusRequestDto {

    @NotNull
    public OrderStatus orderStatus;
}
