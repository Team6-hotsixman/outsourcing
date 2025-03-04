package com.example.outsourcing.domain.order.dto.request;

import com.example.outsourcing.domain.order.enums.OrderStatus;
import lombok.Getter;

@Getter
public class OrderStatusRequestDto {

    public OrderStatus orderStatus;
}
