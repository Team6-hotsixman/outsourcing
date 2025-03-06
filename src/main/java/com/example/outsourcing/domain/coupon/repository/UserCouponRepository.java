package com.example.outsourcing.domain.coupon.repository;

import com.example.outsourcing.domain.coupon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    List<UserCoupon> findAllByUserId(long userId);
}
