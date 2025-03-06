package com.example.outsourcing.domain.coupon.service;

import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.coupon.dto.request.CouponRequestDto;
import com.example.outsourcing.domain.coupon.dto.response.CouponResponseDto;
import com.example.outsourcing.domain.coupon.entity.Coupon;
import com.example.outsourcing.domain.coupon.enums.DiscountType;
import com.example.outsourcing.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional
    public CouponResponseDto create(CouponRequestDto requestDto) {

        // 할인 유형에 따라 discountValue 검증
        if (requestDto.getDiscountType().equals(String.valueOf(DiscountType.FIXED))) {
            if (requestDto.getDiscountValue() <= 0) {
                throw new IllegalArgumentException("정액 할인 금액은 0보다 커야 합니다.");
            }
        } else if (requestDto.getDiscountType().equals(String.valueOf(DiscountType.RATE))) {
            if (requestDto.getDiscountValue() <= 0 || requestDto.getDiscountValue() > 100) {
                throw new IllegalArgumentException("정률 할인 비율은 0보다 크고 100 이하이어야 합니다.");
            }
        }

        // 쿠폰 생성 및 저장
        Coupon coupon = CouponRequestDto.toEntity(requestDto);
        couponRepository.save(coupon);

        return CouponResponseDto.of(coupon);
    }

    @Transactional(readOnly = true)
    public List<CouponResponseDto> findAll() {

        List<Coupon> coupons = couponRepository.findAll();
        return coupons.stream().map(CouponResponseDto::of).toList();
    }

    @Transactional(readOnly = true)
    public CouponResponseDto findById(Long couponId) {

        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_COUPON)
        );
        return CouponResponseDto.of(coupon);
    }

    @Transactional
    public void deleteById(Long couponId) {

        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new ApplicationException(ErrorCode.NOT_FOUND_COUPON)
        );
        couponRepository.deleteById(coupon.getId());
    }
}
