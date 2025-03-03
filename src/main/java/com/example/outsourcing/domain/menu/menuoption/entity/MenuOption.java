package com.example.outsourcing.domain.menu.menuoption.entity;

import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.menu.entity.Menu;
import com.example.outsourcing.domain.menu.menuoption.dto.request.MenuOptionUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
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

    private String description;

    private boolean isAvailable;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Builder
    public MenuOption(String optionName, Integer price, String description, boolean isAvailable, Image image, Menu menu) {
        this.optionName = optionName;
        this.price = price;
        this.description = description;
        this.isAvailable = isAvailable;
        this.image = image;
        this.menu = menu;
    }

    public void updateMenuOption(MenuOptionUpdateRequestDto requestDto, Image image) {
        this.optionName = requestDto.getOptionName();
        this.price = requestDto.getPrice();
        this.description = requestDto.getDescription();
        this.image = image;
        this.isAvailable = requestDto.isAvailable();
    }
}
