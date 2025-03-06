package com.example.outsourcing.domain.coupon.service;

import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.coupon.dto.response.UserCouponIssuseResponseDto;
import com.example.outsourcing.domain.coupon.dto.response.UserCouponResponseDto;
import com.example.outsourcing.domain.coupon.entity.Coupon;
import com.example.outsourcing.domain.coupon.entity.UserCoupon;
import com.example.outsourcing.domain.coupon.repository.CouponRepository;
import com.example.outsourcing.domain.coupon.repository.UserCouponRepository;
import com.example.outsourcing.domain.user.entity.User;
import com.example.outsourcing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCouponService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserCouponIssuseResponseDto issueCoupon(long userId, Long couponId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER)
        );

        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_COUPON)
        );

        UserCoupon userCoupon = new UserCoupon(
                user,
                coupon,
                false,
                LocalDateTime.now()
        );

        userCouponRepository.save(userCoupon);

        return UserCouponIssuseResponseDto.of(userCoupon);
    }

    @Transactional(readOnly = true)
    public List<UserCouponResponseDto> findAll(long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER)
        );

        List<UserCoupon> userCoupons = userCouponRepository.findAllByUserId(userId);

        // 보유중인 쿠폰이 없을 때
        if (userCoupons.isEmpty()) {
            throw new ApplicationException(ErrorCode.EMPTY_COUPON_LIST);
        }

        return userCoupons.stream().map(UserCouponResponseDto::of).toList();
    }

    @Transactional(readOnly = true)
    public UserCouponResponseDto findById(long userId, Long userCouponId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER)
        );

        UserCoupon userCoupon = userCouponRepository.findById(userCouponId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER_COUPON)
        );

        // userCoupon.getUser() 와 user가 다를 때
        if (!userCoupon.getUser().getId().equals(user.getId())) {
            throw new ApplicationException(ErrorCode.MISMATCHED_COUPON_WITH_USER);
        }

        return UserCouponResponseDto.of(userCoupon);
    }

    @Transactional
    public void deleteById(long userId, Long userCouponId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER)
        );

        UserCoupon userCoupon = userCouponRepository.findById(userCouponId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_USER_COUPON)
        );

        // userCoupon.getUser() 와 user가 다를 때
        if (!userCoupon.getUser().getId().equals(user.getId())) {
            throw new ApplicationException(ErrorCode.MISMATCHED_COUPON_WITH_USER);
        }

        userCouponRepository.deleteById(userCouponId);
    }
}
