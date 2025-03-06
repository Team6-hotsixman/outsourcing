package com.example.outsourcing.domain.coupon.dto.response;

import com.example.outsourcing.domain.coupon.entity.UserCoupon;
import com.example.outsourcing.domain.coupon.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserCouponIssuseResponseDto {

    private final Long id;
    private final String name;
    private final LocalDate validFrom;
    private final LocalDate validUntil;
    private final LocalDateTime issuedAt;

    public static UserCouponIssuseResponseDto of(UserCoupon userCoupon) {
        return new UserCouponIssuseResponseDto(
                userCoupon.getId(),
                userCoupon.getCoupon().getName(),
                userCoupon.getCoupon().getValidFrom(),
                userCoupon.getCoupon().getValidUntil(),
                userCoupon.getIssuedAt()
        );
    }
}
