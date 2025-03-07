package com.example.outsourcing.domain.order.entity;

import com.example.outsourcing.domain.order.enums.OrderStatus;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Orders {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer totalPriceAmount;
    private Integer usedPoint;

    @Temporal(value=TemporalType.TIMESTAMP)
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Orders(
            Integer totalPriceAmount,
            Integer usedPoint,
            LocalDateTime orderAt,
            OrderStatus orderStatus,
            Store store,
            User user,
            List<OrderItem> orderItems
    ) {
        this.totalPriceAmount = totalPriceAmount;
        this.usedPoint = usedPoint;
        this.orderAt = orderAt;
        this.orderStatus = orderStatus;
        this.store = store;
        this.user = user;
        this.orderItems = orderItems;
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void updateOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}