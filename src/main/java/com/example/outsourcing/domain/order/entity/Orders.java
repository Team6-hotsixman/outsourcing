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
public class Orders {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer totalPriceAmount;
    private Integer usedPoint;
    @Column(updatable = false)
    private LocalDateTime orderAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Orders(
            Integer totalPriceAmount,
            Integer usedPoint,
            LocalDateTime orderAt,
            OrderStatus orderStatus,
            Store store,
            User user
    ) {
        this.totalPriceAmount = totalPriceAmount;
        this.usedPoint = usedPoint;
        this.orderAt = orderAt;
        this.orderStatus = orderStatus;
        this.store = store;
        this.user = user;
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
