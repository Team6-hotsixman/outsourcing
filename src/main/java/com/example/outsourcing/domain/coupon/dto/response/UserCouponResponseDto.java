package com.example.outsourcing.domain.coupon.dto.response;

import com.example.outsourcing.domain.coupon.entity.UserCoupon;
import com.example.outsourcing.domain.coupon.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserCouponResponseDto {

    private final Long id;
    private final String name;
    private final DiscountType discountType;
    private final int discountValue;
    private final int minOrderPrice;
    private final LocalDate validFrom;
    private final LocalDate validUntil;
    private final LocalDateTime issuedAt;

    public static UserCouponResponseDto of(UserCoupon userCoupon) {
        return new UserCouponResponseDto(
                userCoupon.getId(),
                userCoupon.getCoupon().getName(),
                userCoupon.getCoupon().getDiscountType(),
                userCoupon.getCoupon().getDiscountValue(),
                userCoupon.getCoupon().getMinOrderPrice(),
                userCoupon.getCoupon().getValidFrom(),
                userCoupon.getCoupon().getValidUntil(),
                userCoupon.getIssuedAt()
        );
    }
}
