package com.example.outsourcing.domain.order.entity;

import com.example.outsourcing.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrderItemOption> options = new ArrayList<>();

    @Builder
    public OrderItem(int quantity, Orders order, Menu menu, List<OrderItemOption> options) {
        this.quantity = quantity;
        this.order = order;
        this.menu = menu;
        this.options = options;
    }
}