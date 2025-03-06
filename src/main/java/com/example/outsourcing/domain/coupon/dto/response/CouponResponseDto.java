package com.example.outsourcing.domain.coupon.dto.response;

import com.example.outsourcing.domain.coupon.entity.Coupon;
import com.example.outsourcing.domain.coupon.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CouponResponseDto {

    private final Long id;
    private final String name;
    private final DiscountType discountType;
    private final int discountValue;
    private final int minOrderPrice;
    private final LocalDate validFrom;
    private final LocalDate validUntil;

    public static CouponResponseDto of(Coupon coupon) {
        return new CouponResponseDto(
                coupon.getId(),
                coupon.getName(),
                coupon.getDiscountType(),
                coupon.getDiscountValue(),
                coupon.getMinOrderPrice(),
                coupon.getValidFrom(),
                coupon.getValidUntil()
        );
    }
}
