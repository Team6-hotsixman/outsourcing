package com.example.outsourcing.domain.order.controller;

import com.example.outsourcing.domain.order.dto.OrderRequestDto;
import com.example.outsourcing.domain.order.dto.OrderResponseDto;
import com.example.outsourcing.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders/{userId}")
    public OrderResponseDto placeOrder(
            //jwt 적용 시 바뀔 부분 -> PathVariable userId
            @PathVariable Long userId,
            @RequestParam Long storeId,
            @RequestBody OrderRequestDto requestDto
            ) {
        return orderService.placeOrder(userId, storeId, requestDto);
    }
}
