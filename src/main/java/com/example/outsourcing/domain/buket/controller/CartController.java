package com.example.outsourcing.domain.buket.controller;

import com.example.outsourcing.domain.buket.dto.request.MenuRequest;
import com.example.outsourcing.domain.buket.dto.response.CartResponseDto;
import com.example.outsourcing.domain.buket.service.CartService;
import com.example.outsourcing.domain.common.annotation.Auth;
import com.example.outsourcing.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<List<CartResponseDto>> addMenu(@Auth AuthUser authUser, @RequestBody MenuRequest addMenuRequest) {
        List<CartResponseDto> cartResponseDtos = cartService.addMenu(authUser.getId(), addMenuRequest);
        return ResponseEntity.ok(cartResponseDtos);
    }

    @GetMapping
    public ResponseEntity<List<CartResponseDto>> getCart(@Auth AuthUser authUser) {
        return ResponseEntity.ok(cartService.getCart(authUser.getId()));
    }

    @DeleteMapping
    public ResponseEntity<List<CartResponseDto>> deleteCart(@Auth AuthUser authUser, @RequestBody MenuRequest deleteMenuRequest) {
        List<CartResponseDto> cartResponseDtos = cartService.deleteMenu(authUser.getId(), deleteMenuRequest);
        return ResponseEntity.ok(cartResponseDtos);
    }
}
