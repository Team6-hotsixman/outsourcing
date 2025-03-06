package com.example.outsourcing.domain.coupon.entity;

import com.example.outsourcing.domain.coupon.enums.DiscountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// 쿠폰 정보 엔티티
@Entity
@Getter
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; // 쿠폰 이름

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private int discountValue; // 할인 금액 또는 비율
    private int minOrderPrice; // 쿠폰을 사용하기 위한 최소 주문 금액
    private LocalDate validFrom; // 쿠폰 유효 기간 시작일
    private LocalDate validUntil; // 쿠폰 유효 기간 만료일

    public Coupon(
            String name,
            DiscountType discountType,
            int discountValue,
            int minOrderPrice,
            LocalDate validFrom,
            LocalDate validUntil
    ) {
        this.name = name;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minOrderPrice = minOrderPrice;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }
}
