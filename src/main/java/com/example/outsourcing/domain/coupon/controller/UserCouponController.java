package com.example.outsourcing.domain.coupon.controller;

import com.example.outsourcing.domain.coupon.dto.response.UserCouponIssuseResponseDto;
import com.example.outsourcing.domain.coupon.dto.response.UserCouponResponseDto;
import com.example.outsourcing.domain.coupon.service.UserCouponService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserCouponController {

    private final UserCouponService userCouponService;

    // user가 coupon을 발급 받는 메소드
    @PostMapping("/user-coupons")
    public UserCouponIssuseResponseDto issue(
            HttpServletRequest httpServletRequest,
            @RequestParam Long couponId
    ) {
        long userId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userId")));
        return userCouponService.issueCoupon(userId, couponId);
    }

    @GetMapping("/user-coupons")
    public List<UserCouponResponseDto> get(HttpServletRequest httpServletRequest) {
        long userId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userId")));
        return userCouponService.findAll(userId);
    }

    @GetMapping("/user-coupons/{userCouponId}")
    public UserCouponResponseDto get(
            HttpServletRequest httpServletRequest,
            @PathVariable Long userCouponId
    ) {
        long userId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userId")));
        return userCouponService.findById(userId, userCouponId);
    }

    @DeleteMapping("/user-coupons/{userCouponId}")
    public void delete(
            HttpServletRequest httpServletRequest,
            @PathVariable Long userCouponId
    ) {
        long userId = Long.parseLong(String.valueOf(httpServletRequest.getAttribute("userId")));
        userCouponService.deleteById(userId, userCouponId);
    }
}
