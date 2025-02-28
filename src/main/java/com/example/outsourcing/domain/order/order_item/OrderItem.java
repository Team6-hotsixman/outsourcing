package com.example.outsourcing.domain.order.order_item;

import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.*;

@Entity
@Getter
@NoArgsConstructor
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Builder
    public OrderItem(Order order, Menu menu) {
        this.order = order;
        this.menu = menu;
    }
}
