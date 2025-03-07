package com.example.outsourcing.domain.buket.dto.request;

import com.example.outsourcing.domain.order.dto.request.OrderItemOptionRequestDto;
import com.example.outsourcing.domain.order.dto.request.OrderItemRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MenuRequest {
    private long menuId;
    private String menuName;
    private List<OrderItemOptionRequestDto> options;
    private int quantity;
}
