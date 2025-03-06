package com.example.outsourcing.domain.coupon.dto.request;

import com.example.outsourcing.domain.coupon.entity.Coupon;
import com.example.outsourcing.domain.coupon.enums.DiscountType;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Getter
public class CouponRequestDto {

    private String name;
    private String discountType;
    private int discountValue;
    private int minOrderPrice;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate validFrom;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate validUntil;

    public static Coupon toEntity(CouponRequestDto requestDto) {
        return new Coupon(
                requestDto.getName(),
                DiscountType.valueOf(requestDto.getDiscountType()),
                requestDto.getDiscountValue(),
                requestDto.getMinOrderPrice(),
                requestDto.getValidFrom(),
                requestDto.getValidUntil()
        );
    }
}
