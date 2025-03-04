package com.example.outsourcing.domain.order.controller;

import com.example.outsourcing.domain.order.dto.request.OrderRequestDto;
import com.example.outsourcing.domain.order.dto.response.OrderResponseDto;
import com.example.outsourcing.domain.order.dto.request.OrderStatusRequestDto;
import com.example.outsourcing.domain.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public OrderResponseDto placeOrder(
            HttpServletRequest httpServletRequest,
            @RequestParam Long storeId,
            @Validated @RequestBody OrderRequestDto requestDto
            ) {
        Long userId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userID")));
        return orderService.placeOrder(userId, storeId, requestDto);
    }

    @PutMapping("/orders/status")
    public OrderResponseDto updateOrderStatus(
            HttpServletRequest httpServletRequest,
            @RequestParam Long storeId,
            @RequestParam Long orderId,
            @Validated @RequestBody OrderStatusRequestDto requestDto
            ) {
        Long userId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userID")));
        return orderService.updateOrderStatus(userId, storeId, orderId, requestDto);
    }

    @DeleteMapping("/orders")
    public void cancelOrder(
            HttpServletRequest httpServletRequest,
            @RequestParam Long storeId,
            @RequestParam Long orderId
    ) {
        Long userId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userID")));
        orderService.cancelOrder(userId, orderId, storeId);
    }

    @GetMapping("/orders")
    public Page<OrderResponseDto> getOrders(
            HttpServletRequest httpServletRequest,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long userId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userID")));
        return orderService.findAll(page, size, userId);
    }
}
