package com.example.outsourcing.domain.order.controller;

import com.example.outsourcing.domain.order.dto.OrderRequestDto;
import com.example.outsourcing.domain.order.dto.OrderResponseDto;
import com.example.outsourcing.domain.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public OrderResponseDto placeOrder(
            HttpServletRequest httpServletRequest,
            @RequestParam Long storeId,
            @RequestBody OrderRequestDto requestDto
            ) {
        Long userId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userID")));
        return orderService.placeOrder(userId, storeId, requestDto);
    }
}
