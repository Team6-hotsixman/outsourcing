package com.example.outsourcing.domain.coupon.controller;

import com.example.outsourcing.domain.common.annotation.Admin;
import com.example.outsourcing.domain.coupon.dto.request.CouponRequestDto;
import com.example.outsourcing.domain.coupon.dto.response.CouponResponseDto;
import com.example.outsourcing.domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @Admin
    @PostMapping("/coupons")
    public ResponseEntity<CouponResponseDto> create(@RequestBody CouponRequestDto requestDto) {
        return new ResponseEntity<>(couponService.create(requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/coupons")
    public List<CouponResponseDto> get() {
        return couponService.findAll();
    }

    @GetMapping("/coupons/{couponId}")
    public CouponResponseDto get(@PathVariable Long couponId) {
        return couponService.findById(couponId);
    }

    @Admin
    @DeleteMapping("/coupons/{couponId}")
    public void delete(@PathVariable Long couponId) {
        couponService.deleteById(couponId);
    }
}
