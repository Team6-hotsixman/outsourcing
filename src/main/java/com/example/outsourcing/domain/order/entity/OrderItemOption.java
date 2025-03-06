package com.example.outsourcing.domain.order.entity;

import com.example.outsourcing.domain.menu.menuoption.entity.MenuOption;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class OrderItemOption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_option_id", nullable = false)
    private MenuOption menuOption;

    @Builder
    public OrderItemOption(int quantity, OrderItem orderItem, MenuOption menuOption) {
        this.quantity = quantity;
        this.orderItem = orderItem;
        this.menuOption = menuOption;
    }
}