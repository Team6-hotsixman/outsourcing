package com.example.outsourcing.domain.menu.entity;

import com.example.outsourcing.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.outsourcing.domain.store.entity.Store;
import com.example.outsourcing.domain.common.entity.Image;
import com.example.outsourcing.domain.category.entity.Category;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu")
@Getter
@NoArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String menuName;

    private Integer price;

    private String description;

    private boolean isAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public Menu(String menuName,
                Integer price,
                String description,
                boolean isAvailable,
                Store store,
                Image image,
                Category category) {
        this.menuName = menuName;
        this.price = price;
        this.description = description;
        this.isAvailable = isAvailable;
        this.store = store;
        this.image = image;
        this.category = category;
    }

    public void updateMenu(MenuUpdateRequestDto requestDto, Category category) {
        this.menuName = requestDto.getMenuName();
        this.price = requestDto.getPrice();
        this.description = requestDto.getDescription();
        this.category = category;
        this.isAvailable = requestDto.isAvailable();
        this.image = requestDto.getImage();
    }

}
