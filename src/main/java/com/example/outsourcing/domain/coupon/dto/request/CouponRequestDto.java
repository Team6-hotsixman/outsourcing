package com.example.outsourcing.domain.coupon.dto.request;

import com.example.outsourcing.domain.coupon.entity.Coupon;
import com.example.outsourcing.domain.coupon.enums.DiscountType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Getter
public class CouponRequestDto {

    @NotNull
    private String name;
    @NotNull
    private String discountType;
    @NotNull
    private int discountValue;
    @NotNull
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
