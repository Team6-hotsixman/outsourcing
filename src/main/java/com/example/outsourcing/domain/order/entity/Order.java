package com.example.outsourcing.domain.order.entity;

import com.example.outsourcing.domain.order.enums.OrderStatus;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int totalPriceAmount;
    private int usedPoints;
    private LocalDateTime orderAt;
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Order(
            int totalPriceAmount,
            int usedPoints,
            LocalDateTime orderAt,
            OrderStatus orderStatus,
            Store store,
            User user
    ) {
        this.totalPriceAmount = totalPriceAmount;
        this.usedPoints = usedPoints;
        this.orderAt = orderAt;
        this.orderStatus = orderStatus;
        this.store = store;
        this.user = user;
    }
}
