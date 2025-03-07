package com.example.outsourcing.domain.order.controller;

import com.example.outsourcing.domain.buket.dto.response.CartResponseDto;
import com.example.outsourcing.domain.buket.service.CartService;
import com.example.outsourcing.domain.common.annotation.Auth;
import com.example.outsourcing.domain.common.annotation.Owner;
import com.example.outsourcing.domain.common.dto.AuthUser;
import com.example.outsourcing.domain.order.dto.request.OrderItemRequestDto;
import com.example.outsourcing.domain.order.dto.request.OrderRequestDto;
import com.example.outsourcing.domain.order.dto.response.OrderSimpleResponseDto;
import com.example.outsourcing.domain.order.dto.response.OrderResponseDto;
import com.example.outsourcing.domain.order.dto.request.OrderStatusRequestDto;
import com.example.outsourcing.domain.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    @PostMapping("/orders")
    public ResponseEntity<OrderSimpleResponseDto> placeOrder(
            @Auth AuthUser authUser,
            @RequestParam Long storeId,
            @Validated @RequestBody OrderRequestDto requestDto
            ) {
        if(requestDto.getOrderItems() == null || requestDto.getOrderItems().isEmpty()) {
            requestDto.setOrderItems(OrderItemRequestDto.fromCart(cartService.getCart(authUser.getId())));
        }
        return new ResponseEntity<>(orderService.placeOrder(authUser.getId(), storeId, requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/orders")
    public List<OrderResponseDto> getOrders(
            @Auth AuthUser authUser,
            HttpServletRequest httpServletRequest
    ) {
        Long userId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userId")));
        return orderService.findAll(authUser.getId());
    }

    @GetMapping("/orders/{orderId}")
    public OrderResponseDto getOrder(
            @Auth AuthUser authUser,
            @PathVariable Long orderId
    ) {
        return orderService.findOne(orderId, authUser.getId());
    }

    @Owner
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
            @Auth AuthUser authUser,
            @RequestParam Long storeId,
            @RequestParam Long orderId
    ) {
        orderService.cancelOrder(authUser.getId(), orderId, storeId);
    }
}
