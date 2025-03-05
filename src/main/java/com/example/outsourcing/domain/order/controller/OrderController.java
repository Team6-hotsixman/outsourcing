package com.example.outsourcing.domain.order.controller;

import com.example.outsourcing.domain.common.annotation.Auth;
import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.order.dto.request.OrderRequestDto;
import com.example.outsourcing.domain.order.dto.response.OrderSimpleResponseDto;
import com.example.outsourcing.domain.order.dto.response.OrderResponseDto;
import com.example.outsourcing.domain.order.dto.request.OrderStatusRequestDto;
import com.example.outsourcing.domain.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public OrderSimpleResponseDto placeOrder(
            HttpServletRequest httpServletRequest,
            @RequestParam Long storeId,
            @Validated @RequestBody OrderRequestDto requestDto
            ) {
        Long userId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userID")));
        return orderService.placeOrder(userId, storeId, requestDto);
    }

    @GetMapping("/orders")
    public List<OrderResponseDto> getOrders(
            HttpServletRequest httpServletRequest
    ) {
        Long userId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userID")));
        return orderService.findAll(userId);
    }

    @PutMapping("/orders/status")
    public OrderSimpleResponseDto updateOrderStatus(
            @Auth AuthUser authUser,
            @RequestParam Long storeId,
            @RequestParam Long orderId,
            @Validated @RequestBody OrderStatusRequestDto requestDto
            ) {
        return orderService.updateOrderStatus(authUser, storeId, orderId, requestDto);
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
}
