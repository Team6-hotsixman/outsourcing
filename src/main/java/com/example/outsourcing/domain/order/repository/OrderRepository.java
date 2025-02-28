package com.example.outsourcing.domain.order.repository;

import com.example.outsourcing.domain.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
