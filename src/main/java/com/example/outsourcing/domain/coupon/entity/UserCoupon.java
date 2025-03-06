package com.example.outsourcing.domain.coupon.entity;

import com.example.outsourcing.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// 사용자가 발급받은 쿠폰 엔티티
@Entity
@Getter
@NoArgsConstructor
public class UserCoupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    private boolean used; // 사용 여부
    private LocalDateTime issuedAt; //발급 받은 날짜

    public UserCoupon(User user, Coupon coupon, boolean used, LocalDateTime issuedAt) {
        this.user = user;
        this.coupon = coupon;
        this.used = used;
        this.issuedAt = issuedAt;
    }

    public void useCoupon() {
        this.used = true;
    }
}
