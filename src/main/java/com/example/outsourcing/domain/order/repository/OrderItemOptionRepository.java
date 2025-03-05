package com.example.outsourcing.domain.order.repository;

import com.example.outsourcing.domain.order.entity.OrderItemOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemOptionRepository extends JpaRepository<OrderItemOption, Long> {
}
