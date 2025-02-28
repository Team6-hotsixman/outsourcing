package com.example.outsourcing.domain.menu.menuoption.entity;

import com.example.outsourcing.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu_option")
@Getter
@NoArgsConstructor
public class MenuOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionName;

    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    public MenuOption(String optionName, Integer price, Menu menu) {
        this.optionName = optionName;
        this.price = price;
        this.menu = menu;
    }
}
